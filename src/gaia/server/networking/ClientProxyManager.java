package gaia.server.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import gaia.networking.ClientServerMessageMarshallerProviderFactory;
import gaia.networking.IMessage;
import gaia.networking.MessageInputStream;
import gaia.networking.MessageMarshallerProvider;
import gaia.networking.MessageOutputStream;
import gaia.networking.MessageQueue;
import gaia.networking.QueuedMessageReader;
import gaia.networking.messages.Handshake;
import gaia.networking.messages.JoinFailure;
import gaia.networking.messages.JoinSuccess;
import gaia.server.ServerConsole;
import gaia.server.engine.IJoinRequestProcessor;

/**
 * Manages connected clients and listens for client handshakes.
 */
public class ClientProxyManager {
	/**
	 * The clients.
	 */
	private ArrayList<ClientProxy> clients = new ArrayList<ClientProxy>();
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
	 * @param joinRequestProcessor The processor for join requests.
	 */
	public ClientProxyManager(int port, IJoinRequestProcessor joinRequestProcessor) {
		this.port                 = port;
		this.joinRequestProcessor = joinRequestProcessor;
	}
	
	/**
	 * Get a client message queue populated with any messages received from any clients.
	 * Each of the queued messages defines the player id of the client that sent the message.
	 * @return A client message queue populated with any messages received from any clients.
	 */
	public ClientMessageQueue getReceivedMessageQueue() {
		// Create a new queue to hold the queued client messages.
		ClientMessageQueue clientMessages = new ClientMessageQueue();
		// Populate the message queue with any messages sent from the connected clients.
		// We will need to synchronize this as clients can be added/removed on separate threads.
		synchronized(this.clients) {
			for (ClientProxy client : this.clients) {
				// Get the messages received from the current client.
				MessageQueue clientMessageQueue = client.getReceivedMessageQueue();
				// Add the message to our client message queue, defining the player id of the client.
				while (clientMessageQueue.hasNext()) {
					clientMessages.add(new ClientMessage(client.getPlayerId(), clientMessageQueue.next()));
				}
			}
		}
		// Return the queued client messages.
		return clientMessages;
	}
	
	/**
	 * Send a message to a client.
	 * @param playerId The id of the player to send the message to.
	 * @param message The message to send.
	 */
	public void sendMessage(String playerId, IMessage message) {
		// We will need to synchronize this as clients can be added/removed on separate threads.
		synchronized(this.clients) {
			for (ClientProxy client : this.clients) {
				// Is this the client we are looking for?
				if (client.getPlayerId().equals(playerId)) {
					// We found the client that we want to send the message to.
					client.sendMessage(message);
					return;
				}
			}
		}
	}
	
	/**
	 * Send a message to all clients.
	 * @param message The message to send.
	 */
	public void broadcastMessage(IMessage message) {
		// We will need to synchronize this as clients can be added/removed on separate threads.
		synchronized(this.clients) {
			for (ClientProxy client : this.clients) {
				client.sendMessage(message);
			}
		}
	}
	
	/**
	 * Check for client disconnections and remove them.
	 * @return A list of the player ids of any clients that have disconnected. 
	 */
	public ArrayList<String> processDisconnections() {
		// Create a list to store the disconnected player ids.
		ArrayList<String> disconnected = new ArrayList<String>();
		// We will need to synchronize this as clients can be added/removed on separate threads.
		synchronized(this.clients) {
			Iterator<ClientProxy> iterator = this.clients.iterator();
			while (iterator.hasNext()) {
				// Get the next client.
				ClientProxy client = iterator.next();
				// Remove the client if they have disconnected.
				if (!client.isConnected()) {
					// Store the id of the disconnected player.
					disconnected.add(client.getPlayerId());
					// Remove the client.
					iterator.remove();
				}
			}
		}
		// Return the list of any disconnectd player ids.
		return disconnected;
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
								// Close the socket to kill the connection with the client, the client will know it is not wanted.
								serverSocket.close();
								// Spit out some some sensible output to the server console.
								ServerConsole.writeDebug("Client attempted connection but failed to send handshake.");
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
				// TODO Add stuff from joinRequestProcessor.getWelcomePackage()
				messageOutputStream.writeMessage(new JoinSuccess());
				// Write the connection details to the server console.
				ServerConsole.writeInfo("The player '" + playerId + "' has connected");
				// Create the new client.
				ClientProxy client = new ClientProxy(new QueuedMessageReader(messageInputStream), messageOutputStream, playerId);
				// Add the client to our client list.
				synchronized(this.clients) {
					this.clients.add(client);
				}
				break;
			default:
				throw new RuntimeException("Unexpected join request result");
		}
	}
}
