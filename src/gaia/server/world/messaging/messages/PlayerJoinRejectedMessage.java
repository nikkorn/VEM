package gaia.server.world.messaging.messages;

/**
 * A message containing the detials of a failed join attempt.
 */
public class PlayerJoinRejectedMessage implements IWorldMessage {
	/**
	 * The player id.
	 */
	private String playerId;
	/**
	 * The client id.
	 */
	private String clientId;
	/**
	 * The reason for the rejection.
	 */
	private String reason;
	
	/**
	 * Create a new instance of the PlayerJoinRejectedMessage class.
	 * @param playerId The player id.
	 * @param clientId The client id.
	 * @param reason The reason for the rejection.
	 */
	public PlayerJoinRejectedMessage(String playerId, String clientId, String reason) {
		this.playerId = playerId;
		this.clientId = clientId;
		this.reason   = reason;
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
	 * Get the reason for the rejection.
	 * @return The reason for the rejection.
	 */
	public String getReason() {
		return this.reason;
	}
	
	@Override
	public WorldMessageType getMessageType() {
		return WorldMessageType.PLAYER_JOIN_REJECTED;
	}
}

