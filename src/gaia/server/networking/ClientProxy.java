package gaia.server.networking;

import java.io.IOException;
import gaia.networking.IMessage;
import gaia.networking.MessageOutputStream;
import gaia.networking.QueuedMessageReader;

/**
 * A server-side representation of a connected client.
 */
public class ClientProxy {
	/**
	 * The message reader used to read messages from a message input stream into a queue.
	 */
	private QueuedMessageReader queuedMessageReader;
	/**
	 * The message output stream used to write messages to the client.
	 */
	private MessageOutputStream messageOutputStream;
	/**
	 * The player id.
	 */
	private String playerId;
	/**
	 * The client request queue.
	 */
	private ClientRequestQueue requestQueue = new ClientRequestQueue();
	
	/**
	 * Create a new instance of the ClientProxy class.
	 * @param queuedMessageReader The message reader used to read messages from a message input stream into a queue.
	 * @param messageOutputStream The output stream used to write messages to the client. 
	 * @param id The player id.
	 */
	public ClientProxy(QueuedMessageReader queuedMessageReader, MessageOutputStream messageOutputStream, String playerId) {
		this.queuedMessageReader  = queuedMessageReader;
		this.messageOutputStream  = messageOutputStream;
		this.playerId             = playerId;
		// Our queued message reader needs to start reading incoming messages.
		Thread messageReaderThread = new Thread(queuedMessageReader);
		messageReaderThread.setDaemon(true);
		messageReaderThread.start();
	}
	
	/**
	 * Get whether the client is still connected.
	 * @return Whether the client is still connected.
	 */
	public boolean isConnected() {
		// For now, we will check whether the client is connected by checking if our reader is still connected.
		return this.queuedMessageReader.isConnected();
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
	
	/**
	 * Send a message to the client.
	 * @param message The message to send.
	 * @throws IOException 
	 */
	public void sendMessage(IMessage message) throws IOException {
		this.messageOutputStream.writeMessage(message);
	}
}