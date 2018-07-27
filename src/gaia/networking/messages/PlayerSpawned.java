package gaia.networking.messages;

import gaia.Position;
import gaia.networking.IMessage;

/**
 * The message sent to all clients when a player spawns.
 */
public class PlayerSpawned implements IMessage {
	/**
	 * The id of the spawning player.
	 */
	private String playerId;
	/**
	 * The position of the spawning player.
	 */
	private Position spawnPosition;
	
	/**
	 * Create a new instance of the PlayerSpawned class.
	 * @param playerId The id of the player.
	 * @param position The position of the spawn.
	 */
	public PlayerSpawned(String playerId, Position position) {
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
	public int getTypeId() {
		return MessageIdentifier.PLAYER_SPAWNED;
	}
}
