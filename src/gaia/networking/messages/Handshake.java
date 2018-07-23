package gaia.networking.messages;

import gaia.networking.IMessage;

/*
 * A handshake sent by the client to start a connection.
 */
public class Handshake implements IMessage {
	/**
	 * The player id.
	 */
	private String playerId;
	
	/**
	 * Create an instance of the Handshake class.
	 * @param playerId The player id.
	 */
	public Handshake(String playerId) {
		this.playerId = playerId;
	}
	
	/**
	 * Get the player id.
	 * @return The player id.
	 */
	public String getPlayerId() {
		return this.playerId;
	}

	/**
	 * Get the message type id.
	 * @return The message type id.
	 */
	@Override
	public int getTypeId() {
		return 0;
	}
}
