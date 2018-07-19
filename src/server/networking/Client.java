package server.networking;

import java.net.Socket;

/**
 * Represents a connected client.
 */
public class Client {
	/**
	 * The client socket.
	 */
	private Socket socket;
	/**
	 * The client id.
	 */
	private String id;
	/**
	 * The client request queue.
	 */
	private ClientRequestQueue requestQueue = new ClientRequestQueue();
	
	/**
	 * Create a new instance of the Client class.
	 * @param socket The client socket.
	 * @param id The client id.
	 */
	public Client(Socket socket, String id) {
		this.socket = socket;
		this.id     = id;
	}
	
	/**
	 * Get the client id.
	 * @return The client id.
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Get the client request queue.
	 * @return The client request queue.
	 */
	public ClientRequestQueue getClientRequestQueue() {
		return requestQueue;
	}
}
