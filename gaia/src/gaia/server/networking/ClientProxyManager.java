package gaia.server.networking;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
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
import gaia.server.welcomepackage.WelcomePackage;
import gaia.server.world.players.requests.JoinRequest;
import gaia.utils.Queue;

/**
 * Manages connected clients and listens for client handshakes.
 */
public class ClientProxyManager {
	/**
	 * The clients.
	 */
	private ArrayList<ClientProxy> clients = new ArrayList<ClientProxy>();
	/**
	 * The queue of join requests for newly connected clients.
	 */
	private Queue<JoinRequest> clientJoinRequestQueue = new Queue<JoinRequest>();
	/**
	 * The port on which client connections are made.
	 */
	private int port;
	
	/**
	 * Creates a new instance of the ClientProxyManager class.
	 * @param port The port on which client connections are made.
	 */
	public ClientProxyManager(int port) {
		this.port = port;
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
	 * Get a queue of any client join requests that need to be processed.
	 * @return A queue of any client join requests that need to be processed.
	 */
	public Queue<JoinRequest> getClientJoinRequestQueue() {
		// Create a new queue to hold the requests.
		Queue<JoinRequest> requests = new Queue<JoinRequest>();
		// We will need to synchronize this as clients can be added/removed on separate threads.
		synchronized(this.clients) {
			requests.add(clientJoinRequestQueue);
		}
		// Return the queued client join requests.
		return requests;
	}
	
	/**
	 * Accept a client waiting to join.
	 * @param clientId The id of the client that we have accepted.
	 * @param welcomePackage The welcome package containing details to send.
	 */
	public void acceptClient(String clientId, WelcomePackage welcomePackage) {
		// We will need to synchronize this as clients can be added/removed on separate threads.
		synchronized(this.clients) {
			for (ClientProxy client : this.clients) {
				// Is this the client we are looking for?
				if (client.getClientId().equals(clientId)) {
					// We found the client so set their status.
					client.setStatus(ClientProxyStatus.JOINED);
					// Let the client know that they have joined and pass them some info about the world! This completes a client-server handshake.
					client.sendMessage(new JoinSuccess(welcomePackage.getWorldSeed(), welcomePackage.getWorldTime(), welcomePackage.getPositionedPlayers()));
					break;
				}
			}
		}
	}
	
	/**
	 * Reject a client waiting to join.
	 * @param clientId The id of the client that we have rejected.
	 * @param reason The reason for the rejection.
	 */
	public void rejectClient(String clientId, String reason) {
		// We will need to synchronize this as clients can be added/removed on separate threads.
		synchronized(this.clients) {
			for (ClientProxy client : this.clients) {
				// Is this the client we are looking for?
				if (client.getClientId().equals(clientId)) {
					// We found the client so set their status.
					client.setStatus(ClientProxyStatus.JOIN_REJECTED);
					// Let the client know that they have not joined! This completes the handshake.
					client.sendMessage(new JoinFailure(reason));
					break;
				}
			}
		}
	}
	
	/**
	 * Send a message to a client, not including clients waiting to join.
	 * @param playerId The id of the player to send the message to.
	 * @param message The message to send.
	 */
	public void sendMessage(String playerId, IMessage message) {
		// We will need to synchronize this as clients can be added/removed on separate threads.
		synchronized(this.clients) {
			for (ClientProxy client : this.clients) {
				// We ONLY send messages to clients that have joined.
				if (client.getStatus() != ClientProxyStatus.JOINED) {
					continue;
				}
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
	 * Send a message to all clients, not including clients waiting to join.
	 * @param message The message to send.
	 */
	public void broadcastMessage(IMessage message) {
		// We will need to synchronize this as clients can be added/removed on separate threads.
		synchronized(this.clients) {
			for (ClientProxy client : this.clients) {
				// We ONLY send messages to clients that have joined.
				if (client.getStatus() == ClientProxyStatus.JOINED) {
					client.sendMessage(message);
				}
			}
		}
	}
	
	/**
	 * Check for client disconnections/rejections and remove them.
	 * @return A list of the player ids of any joined clients that have disconnected. 
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
				// Remove the client if their attempt to join was rejected.
				if (client.getStatus() == ClientProxyStatus.JOIN_REJECTED) {
					// Remove the client.
					iterator.remove();
					continue;
				}
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
			final ServerSocket serverSocket = new ServerSocket(this.port);
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
		} catch (BindException e) {
			// We failed to bind to the socket!
			ServerConsole.writeError("We failed to bind to the socket. Is a server already running?");
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
		// Generate a unique client id.
		String clientId = UUID.randomUUID().toString();
		// Create the new client.
		ClientProxy client = new ClientProxy(new QueuedMessageReader(messageInputStream), messageOutputStream, clientId, playerId);
		// Add the client to our client list and request for them to join the game.
		synchronized(this.clients) {
			// Add the client.
			this.clients.add(client);
			// Add the client join request to the queue.
			this.clientJoinRequestQueue.add(new JoinRequest(playerId, clientId));
		}
	}
}
