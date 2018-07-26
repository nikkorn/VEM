package gaia.server.world.messaging.messages;

/**
 * A message containing the details of a player despawn.
 */
public class PlayerDespawnedMessage implements IWorldMessage {
	/**
	 * The id of the despawning player.
	 */
	private String playerId;
	
	/**
	 * Create a new instance of the PlayerDespawnedMessage class.
	 * @param playerId The id of the player.
	 */
	public PlayerDespawnedMessage(String playerId) {
		this.playerId = playerId;
	}
	
	/**
	 * Get the id of the despawning player.
	 * @return The id of the despawning player.
	 */
	public String getPlayerId() {
		return playerId;
	}

	@Override
	public WorldMessageType getMessageType() {
		return WorldMessageType.PLAYER_DESPAWN;
	}
}
