package gaia.server.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import gaia.networking.ClientServerMessageMarshallerProviderFactory;
import gaia.networking.IMessage;
import gaia.networking.MessageInputStream;
import gaia.networking.MessageMarshallerProvider;
import gaia.networking.MessageOutputStream;
import gaia.networking.QueuedMessageReader;
import gaia.networking.messages.Handshake;
import gaia.networking.messages.JoinFailure;
import gaia.networking.messages.JoinSuccess;
import gaia.server.engine.IJoinRequestProcessor;
import gaia.server.engine.RequestQueue;

/**
 * Manages connected clients and listens for client handshakes.
 */
public class ClientProxyManager {
	/**
	 * The clients.
	 */
	private ArrayList<ClientProxy> clients = new ArrayList<ClientProxy>();
	/**
	 * The queue of requests to be processed by the engine.
	 */
	private RequestQueue requestQueue;
	/**
	 * The processor for join requests.
	 */
	private IJoinRequestProcessor joinRequestProcessor;
	/**
	 * The port on which client connections are made.
	 */
	private int port;
	
	/**
	 * Creates a new instance of the ClientProxyManager class.
	 * @param port The port on which client connections are made.
	 * @param requestQueue The queue of requests to be processed by the engine.
	 * @param joinRequestProcessor The processor for join requests.
	 */
	public ClientProxyManager(int port, RequestQueue requestQueue, IJoinRequestProcessor joinRequestProcessor) {
		this.port                 = port;
		this.requestQueue         = requestQueue;
		this.joinRequestProcessor = joinRequestProcessor;
	}
	
	/**
	 * Process any requests that connected clients have made.
	 */
	public void processRequestsFromClients() {
		// TODO Process any requests that connected clients have made.
	}
	
	/**
	 * Start listening for new client connections.
	 */
	public void startListeningForConnections() {
		try {
			// Create the ServerSocket instance on which we will accept new connections.
			ServerSocket serverSocket = new ServerSocket(this.port);
			// Create a new thread on which to sit and listen for client connections.
			Thread connectionListenerThread = new Thread(new Runnable() {
				@Override
				public void run() {
					// We will just listen forever.
					while(true) {
						// Sit here and listen here for a connection attempt.
						try {
							Socket clientSocket = serverSocket.accept();
							// Create the message marshaller provider for our message stream.
							MessageMarshallerProvider marshallerProvider = ClientServerMessageMarshallerProviderFactory.create();
							// Create a message input stream for the new client.
							MessageInputStream messageInputStream = new MessageInputStream(clientSocket.getInputStream(), marshallerProvider);
							// We expect a handshake to be sent by the client wishing to connect.
							IMessage initalMessage = messageInputStream.readMessage();
							// We got a message from the client! Check that it was the handshake.
							if (initalMessage.getTypeId() != 0) {
								throw new RuntimeException("Failed to get handshake from client");
							}
							// We got a handshake! Process it!
							processHandshake((Handshake)initalMessage, clientSocket, 
									messageInputStream, new MessageOutputStream(clientSocket.getOutputStream(), marshallerProvider));
						} catch (IOException e) {
							// An IO exception was thrown in accepting a new client connection.
							// In this case just give up and go back to listening for new connections.
							continue;
						}
					}
				}
			});
			// This connection listener thread should not prevent the server from stopping.
			connectionListenerThread.setDaemon(true);
			// Start listening for client connections.
			connectionListenerThread.start();
		} catch (IOException e) {
			// An IO exception was thrown in creating a new ServerSocket instance.
			e.printStackTrace();
		}
	}
	
	/**
	 * Handle a handshake sent from a client attempting to connect.
	 * @param handshake The handshake sent from the client.
	 * @param clientSocket The client socket.
	 * @param messageInputStream The message input stream.
	 * @param messageOutputStream The message output stream.
	 */
	private void processHandshake(Handshake handshake, Socket clientSocket, MessageInputStream messageInputStream, MessageOutputStream messageOutputStream) throws IOException {
		// Get the player id from the handshake.
		String playerId = handshake.getPlayerId();
		// Attempt to join the world using the player id and handle the result.
		switch (this.joinRequestProcessor.join(playerId)) {
			case ALREADY_JOINED:
				// Return failure message over output stream!
				messageOutputStream.writeMessage(new JoinFailure("You have already joined!"));
				break;
			case BLACKLISTED:
				// Return failure message over output stream!
				messageOutputStream.writeMessage(new JoinFailure("You are on the blacklist!"));
				break;
			case SUCCESS:
				// Return success message over output stream!
				messageOutputStream.writeMessage(new JoinSuccess());
				// Create the new client.
				ClientProxy client = new ClientProxy(new QueuedMessageReader(messageInputStream), messageOutputStream, playerId);
				// Add the client to our client list.
				synchronized(this) {
					this.clients.add(client);
				}
				break;
			default:
				throw new RuntimeException("Unexpected join request result");
		}
	}
}
