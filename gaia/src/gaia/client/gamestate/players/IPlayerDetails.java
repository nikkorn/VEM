package gaia.client.gamestate.players;

import gaia.world.Direction;

/**
 * An interface to expose immutable player details.
 */
public interface IPlayerDetails {
	
	/**
	 * Get the x position of the player.
	 * @return The x position of the player.
	 */
	int getX();
	
	/**
	 * Get the y position of the player.
	 * @return The y position of the player.
	 */
	int getY();
	
	/**
	 * Get the direction that the player is facing.
	 * @return The direction that the player is facing.
	 */
	Direction getFacingDirection();
	
	/**
	 * Get the walking transition of the player, or null if the player is not currently walking.
	 * @return The walking transition of the player, or null if the player is not currently walking.
	 */
	WalkTransition getWalkingTransition();
	
	/**
	 * Get whether the player is currently walking.
	 * @return Whether the player is currently walking.
	 */
	boolean isWalking();
}
