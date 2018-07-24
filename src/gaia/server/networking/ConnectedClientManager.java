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
import gaia.networking.messages.Handshake;
import gaia.server.engine.RequestQueue;

/**
 * Manages connected clients and listens for client handshakes.
 */
public class ConnectedClientManager {
	/**
	 * The clients.
	 */
	private ArrayList<Client> clients = new ArrayList<Client>();
	/**
	 * The queue of requests to be processed by the engine.
	 */
	private RequestQueue requestQueue;
	/**
	 * The port on which client connections are made.
	 */
	private int port;
	
	/**
	 * Creates a new instance of the ConnectedClientManager class.
	 * @param port The port on which client connections are made.
	 * @param requestQueue The queue of requests to be processed by the engine.
	 */
	public ConnectedClientManager(int port, RequestQueue requestQueue) {
		this.port         = port;
		this.requestQueue = requestQueue;
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
							processHandshake(serverSocket.accept());
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
	 * Handle a new client connection.
	 * @param clientSocket The socket for the connecting client.
	 */
	private void processHandshake(Socket clientSocket) {
		try {
			// Create the message marshaller provider for our message stream.
			MessageMarshallerProvider marshallerProvider = ClientServerMessageMarshallerProviderFactory.create();
			// Create a message input stream for the new client.
			MessageInputStream messageInputStream = new MessageInputStream(clientSocket.getInputStream(), marshallerProvider);
			// Create a message input stream for the new client.
			MessageOutputStream messageOutputStream = new MessageOutputStream(clientSocket.getOutputStream(), marshallerProvider);
			// We expect a handshake to be sent by the client wishing to connect.
			IMessage initalMessage = messageInputStream.readMessage();
			// We got a message from the client! Check that it was the handshake.
			if (initalMessage.getTypeId() != 0) {
				throw new RuntimeException("Failed to get handshake from client");
			}
			// We got our handshake! Create the new client.
			Client client = new Client(messageInputStream, messageOutputStream, clientSocket, ((Handshake)initalMessage).getPlayerId());
			// Add the client to our client list.
			synchronized(this) {
				this.clients.add(client);
			}
		} catch (IOException e) {
			// An IO exception was thrown in accepting a handshake.
			e.printStackTrace();
		}
	}
}
