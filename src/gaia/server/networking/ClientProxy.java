package gaia.server.networking;

import java.net.Socket;
import gaia.networking.MessageInputStream;
import gaia.networking.MessageOutputStream;

/**
 * A server-side representation of a connected client.
 */
public class ClientProxy {
	/**
	 * The input stream used to read messages from the client. 
	 */
	private MessageInputStream messageInputStream;
	/**
	 * The output stream used to write messages to the client. 
	 */
	private MessageOutputStream messageOutputStream;
	/**
	 * The client socket.
	 */
	private Socket socket;
	/**
	 * The player id.
	 */
	private String playerId;
	/**
	 * The client request queue.
	 */
	private ClientRequestQueue requestQueue = new ClientRequestQueue();
	
	/**
	 * Create a new instance of the Client class.
	 * @param messageInputStream The input stream used to read messages from the client. 
	 * @param messageOutputStream The output stream used to write messages to the client. 
	 * @param socket The client socket.
	 * @param id The player id.
	 */
	public ClientProxy(MessageInputStream messageInputStream, MessageOutputStream messageOutputStream, Socket socket, String playerId) {
		this.messageInputStream  = messageInputStream;
		this.messageOutputStream = messageOutputStream;
		this.socket              = socket;
		this.playerId            = playerId;
	}
	
	/**
	 * Get the player id.
	 * @return The player id.
	 */
	public String getPlayerId() {
		return this.playerId;
	}
	
	/**
	 * Get the client request queue.
	 * @return The client request queue.
	 */
	public ClientRequestQueue getClientRequestQueue() {
		return requestQueue;
	}
}
