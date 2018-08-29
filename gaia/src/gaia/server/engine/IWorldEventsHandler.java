package gaia.server.engine;

import java.util.ArrayList;
import gaia.server.world.placements.IPlacementDetails;
import gaia.world.items.ItemType;

/**
 * Handler for world events.
 */
public interface IWorldEventsHandler {
	
	/**
	 * Called when a player successfully joins.
	 * @param playerId The id of the joining player.
	 * @param welcomePackage The welcome package to give to the player.
	 */
	void onPlayerJoinSuccess(String playerId, WelcomePackage welcomePackage);
	
	/**
	 * Called when a player fails to join.
	 * @param playerId The id of the player that failed to join.
	 * @param reason The reason for the failure.
	 */
	void onPlayerJoinRejected(String playerId, String reason);
	
	/**
	 * Called when a player spawns.
	 * @param playerId The id of the spawning player.
	 * @param x The x position of the spawning player.
	 * @param y The y position of the spawning player.
	 */
	void onPlayerSpawn(String playerId, int x, int y);
	
	/**
	 * Called when a player changes positions.
	 * @param playerId The id of the player that changed positions.
	 * @param x The x position of the player.
	 * @param y The y position of the player.
	 */
	void onPlayerPositionChange(String playerId, int x, int y);
	
	/**
	 * Called when a slot in a player inventory changes.
	 * @param playerId The id of the player that has had their inventory change.
	 * @param item The item that is now in the inventory slot.
	 * @param slotIndex The index of the changed inventory slot.
	 */
	void onPlayerInventoryChange(String playerId, ItemType item, int slotIndex);
	
	/**
	 * Called when a chunk is loaded.
	 * @param x The x position of the chunk.
	 * @param y The y position of the chunk.
	 * @param placements The list of placements in the chunk.
	 * @param instigator The id of the player that instigated the chunk load.
	 */
	void onChunkLoad(int x, int y, ArrayList<IPlacementDetails> placements, String instigator);
	
	/**
	 * Called when the state of a placement changes.
	 * @param x The x position of the placement.
	 * @param y The y position of the placement.
	 * @param placement The placement details.
	 */
	void onPlacementChange(int x, int y, IPlacementDetails placement);
}
