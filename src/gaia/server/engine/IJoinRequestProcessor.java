package gaia.server.engine;

import gaia.server.world.players.PlayerJoinRequestResult;

/**
 * Processor for join requests.
 */
public interface IJoinRequestProcessor {
	
	/**
	 * Attempt to join the world.
	 * @param playerId The id of the player attempting to join the world.
	 * @return The result of the attempt.
	 */
	public PlayerJoinRequestResult join(String playerId);
	
	/**
	 * Get the welcome package containing details to be passed to joining players.
	 * @return The welcome package containing details to be passed to joining players.
	 */
	public WelcomePackage getWelcomePackage();
}
