package gaia.server.world.messaging.messages;

/**
 * A message containing the details of a rejected player spawn.
 */
public class PlayerSpawnRejectedMessage implements IWorldMessage {
	/**
	 * The id of the spawning player.
	 */
	private String playerId;
	/**
	 * The reason for the rejection.
	 */
	private String reason;
	
	/**
	 * Create a new instance of the PlayerSpawnRejectedMessage class.
	 * @param playerId The id of the player.
	 * @param reason The reason for the rejection.
	 */
	public PlayerSpawnRejectedMessage(String playerId, String reason) {
		this.playerId = playerId;
		this.reason   = reason;
	}
	
	/**
	 * Get the id of the spawning player.
	 * @return The id of the spawning player.
	 */
	public String getPlayerId() {
		return playerId;
	}

	/**
	 * Get the reason for the rejection.
	 * @return The reason for the rejection.
	 */
	public String getRejectionReason() {
		return reason;
	}

	@Override
	public WorldMessageType getMessageType() {
		return WorldMessageType.PLAYER_SPAWN_REJECTED;
	}
}