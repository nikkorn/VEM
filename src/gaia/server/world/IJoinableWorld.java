package gaia.server.world;

import gaia.server.world.players.PlayerJoinRequestResult;

/**
 * Represents a joinable world.
 */
public interface IJoinableWorld {
	
	/**
	 * Attempt to join the world.
	 * @param playerId The id of the player attempting to join the world.
	 * @return The result of the attempt.
	 */
	public PlayerJoinRequestResult join(String playerId);
	
	/**
	 * Get the world seed.
	 * @return The world seed.
	 */
	public long getSeed();
}
