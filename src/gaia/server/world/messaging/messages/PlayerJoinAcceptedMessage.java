package gaia.server.world.messaging.messages;

import gaia.server.engine.WelcomePackage;

/**
 * A message containing the detials of a successful join attempt.
 */
public class PlayerJoinAcceptedMessage implements IWorldMessage {
	/**
	 * The player id.
	 */
	private String playerId;
	/**
	 * The client id.
	 */
	private String clientId;
	/**
	 * The welcome package for the client.
	 */
	private WelcomePackage welcomePackage;
	
	/**
	 * Create a new instance of the PlayerJoinAcceptedMessage class.
	 * @param playerId The player id.
	 * @param clientId The client id.
	 * @param welcomePackage The welcome package for the client.
	 */
	public PlayerJoinAcceptedMessage(String playerId, String clientId, WelcomePackage welcomePackage) {
		this.playerId       = playerId;
		this.clientId       = clientId;
		this.welcomePackage = welcomePackage;
	}
	
	/**
	 * Get the id of the player.
	 * @return The id of the player.
	 */
	public String getPlayerId() {
		return playerId;
	}
	
	/**
	 * Get the id of the client.
	 * @return The id of the client.
	 */
	public String getClientId() {
		return clientId;
	}
	
	/**
	 * Get the welcome package for the client.
	 * @return The welcome package for the client.
	 */
	public WelcomePackage getWelcomePackage() {
		return this.welcomePackage;
	}
	
	@Override
	public WorldMessageType getMessageType() {
		return WorldMessageType.PLAYER_JOIN_SUCCESS;
	}
}
