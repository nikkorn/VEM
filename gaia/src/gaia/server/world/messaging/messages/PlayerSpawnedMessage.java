package gaia.server.world.messaging.messages;

import gaia.server.engine.IWorldEventsHandler;
import gaia.world.Position;

/**
 * A message containing the details of a player spawn.
 */
public class PlayerSpawnedMessage implements IWorldMessage {
	/**
	 * The id of the spawning player.
	 */
	private String playerId;
	/**
	 * The position of the spawning player.
	 */
	private Position spawnPosition;
	
	/**
	 * Create a new instance of the PlayerSpawnMessage class.
	 * @param playerId The id of the player.
	 * @param position The position of the spawn.
	 */
	public PlayerSpawnedMessage(String playerId, Position position) {
		this.playerId      = playerId;
		this.spawnPosition = position;
	}
	
	/**
	 * Get the id of the spawning player.
	 * @return The id of the spawning player.
	 */
	public String getPlayerId() {
		return playerId;
	}

	/**
	 * Get the position of the spawning player.
	 * @return The position of the spawning player.
	 */
	public Position getSpawnPosition() {
		return spawnPosition;
	}
	
	@Override
	public void process(IWorldEventsHandler handler) {
		handler.onPlayerSpawn(playerId, spawnPosition);
	}
}
