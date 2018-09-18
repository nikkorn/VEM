package gaia.client.gamestate.players;

import gaia.world.Direction;
import gaia.world.items.ItemType;

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
	 * Get the item at the specified slot index of the player's inventory. 
	 * @param slotIndex the slot index.
	 * @return The item at the specified slot index of the player's inventory. 
	 */
	ItemType getInventorySlot(int slotIndex);
	
	/**
	 * Get whether the player is currently walking.
	 * @return Whether the player is currently walking.
	 */
	boolean isWalking();
}
