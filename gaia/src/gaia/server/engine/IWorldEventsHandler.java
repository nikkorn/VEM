package gaia.server.engine;

import java.util.ArrayList;
import gaia.server.welcomepackage.WelcomePackage;
import gaia.server.world.placements.IPlacementDetails;
import gaia.world.Direction;
import gaia.world.IPositionDetails;
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
	 * @param welcomePackage The welcome package containing information to send to the player.
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
	 * @param position The position of the spawn.
	 */
	void onPlayerSpawn(String playerId, IPositionDetails position);
	
	/**
	 * Called when a player successfully moves to a position.
	 * @param playerId The id of the player that has changed positions.
	 * @param position The new position of the player.
	 * @param direction The direction that the player has moved in to reach the new position.
	 */
	void onPlayerMove(String playerId, IPositionDetails position, Direction direction);
	
	/**
	 * Called when a player attempts to move to a position but is blocked.
	 * @param playerId The id of the player that attempted to move.
	 * @param position The position of the player.
	 */
	void onPlayerBlocked(String playerId, IPositionDetails position);
	
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
	 * @param position The position of the placement.
	 * @param placement The placement details.
	 */
	void onPlacementCreate(String[] playerIds, IPositionDetails position, IPlacementDetails placement);
	
	/**
	 * Called when the state of a placement is updated.
	 * @param playerIds The ids of any players who are in the vicinity of the updated placement.
	 * @param position The position of the placement.
	 * @param placement The placement details.
	 */
	void onPlacementUpdate(String[] playerIds, IPositionDetails position, IPlacementDetails placement);

	/**
	 * Called when a placement is removed from the world.
	 * @param playerIds The ids of any players who are in the vicinity of the placement being removed.
	 * @param position The position of the placement.
	 * @param expectedType The expected placement type.
	 */
	void onPlacementRemove(String[] playerIds, IPositionDetails position, PlacementType expectedType);
}
