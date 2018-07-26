package gaia.server.networking;

import gaia.networking.IMessage;

/**
 * Represents a message sent from a client that includes the player id of the client.
 */
public class ClientMessage {
	/**
	 *  The player id of the client.
	 */
	private String playerId;
	/**
	 * The message sent by the client.
	 */
	private IMessage message;
	
	/**
	 * Create a new instance of the ClientMessage class.
	 * @param playerId The player id of the client.
	 * @param message The message sent by the client.
	 */
	public ClientMessage(String playerId, IMessage message) {
		this.playerId = playerId;
		this.message  = message;
	}
	
	/**
	 *  Get the player id of the client.
	 * @return The player id of the client.
	 */
	public String getPlayerId() {
		return this.playerId;
	}
	
	/**
	 * Get the message sent by the client.
	 * @return The message sent by the client.
	 */
	public IMessage getMessage() {
		return this.message;
	}
}
