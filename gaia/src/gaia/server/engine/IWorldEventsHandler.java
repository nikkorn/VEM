package gaia.server.engine;

import java.util.ArrayList;
import gaia.server.world.placements.IPlacementDetails;
import gaia.world.PlacementType;
import gaia.world.items.ItemType;

/**
 * Handler for world events.
 */
public interface IWorldEventsHandler {
	
	/**
	 * Called when a player successfully joins.
	 * @param clientId The id of the joining client.
	 * @param playerId The player id of the joining client.
	 * @param welcomePackage The welcome package to give to the player.
	 */
	void onPlayerJoinSuccess(String clientId, String playerId, WelcomePackage welcomePackage);
	
	/**
	 * Called when a player fails to join.
	 * @param clientId The id of the client that failed to join.
	 * @param reason The reason for the failure.
	 */
	void onPlayerJoinRejected(String clientId, String reason);
	
	/**
	 * Called when a player spawns.
	 * @param playerId The id of the spawning player.
	 * @param x The x position of the spawning player.
	 * @param y The y position of the spawning player.
	 */
	void onPlayerSpawn(String playerId, int x, int y);
	
	/**
	 * Called when a player moves to a position.
	 * @param playerId The id of the player that changed positions.
	 * @param x The x position of the player.
	 * @param y The y position of the player.
	 * @param isCorrection Whether this move mas made in order to correct the player's position.
	 */
	void onPlayerMove(String playerId, int x, int y, boolean isCorrection);
	
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
	 * Called when a placement is created.
	 * @param playerIds The ids of any players who are in the vicinity of the created placement.
	 * @param x The x position of the placement.
	 * @param y The y position of the placement.
	 * @param placement The placement details.
	 */
	void onPlacementCreate(String[] playerIds, int x, int y, IPlacementDetails placement);
	
	/**
	 * Called when the state of a placement is updated.
	 * @param playerIds The ids of any players who are in the vicinity of the updated placement.
	 * @param x The x position of the placement.
	 * @param y The y position of the placement.
	 * @param placement The placement details.
	 */
	void onPlacementUpdate(String[] playerIds, int x, int y, IPlacementDetails placement);

	/**
	 * Called when a placement is removed from the world.
	 * @param playerIds The ids of any players who are in the vicinity of the placement being removed.
	 * @param x The x position of the placement.
	 * @param y The y position of the placement.
	 * @param expectedType The expected placement type.
	 */
	void onPlacementRemove(String[] playerIds, int x, int y, PlacementType expectedType);
}
