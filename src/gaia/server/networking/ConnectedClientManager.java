package gaia.server.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import gaia.server.engine.RequestQueue;

/**
 * Manages connected clients.
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
							handleNewConnection(serverSocket.accept());
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
	private void handleNewConnection(Socket clientSocket) {
		try {
			// Create a reader for the handshake string that will be sent by the connecting client.
			BufferedReader handshakeReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			// Read the handshake.
			String handshake = handshakeReader.readLine();
			
			// TODO Handle the handshake!
			System.out.println("Handshake: " + handshake);
			
			// TODO Verify that the client can connect. Return here if they cant.
			
			// Create the new client.
			Client client = new Client(clientSocket, "nikolas");
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
