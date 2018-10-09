package gaia.server.world.chunk;

import org.json.JSONArray;
import org.json.JSONObject;
import gaia.Constants;
import gaia.server.world.PlacementModificationsHandler;
import gaia.server.world.messaging.WorldMessageQueue;
import gaia.world.TileType;
import gaia.world.items.ItemType;
import gaia.server.world.placements.IActionablePlacement;
import gaia.server.world.placements.IPlacementActionsExecutor;
import gaia.server.world.placements.Placement;
import gaia.server.world.placements.PlacementFactory;
import gaia.server.world.placements.Placements;
import gaia.server.world.placements.Priority;
import gaia.time.Time;
import gaia.world.Position;

/**
 * A world chunk.
 */
public class Chunk {
	/**
	 * The static tiles that this chunk is composed of.
	 */
	private TileType[][] tiles = new TileType[Constants.WORLD_CHUNK_SIZE][Constants.WORLD_CHUNK_SIZE];
	/**
	 * The placements that this chunk is composed of.
	 */
	private Placements placements;
	/**
	 * The x/y positions of the chunk.
	 */
	private short x, y;
	/**
	 * The world message queue.
	 */
	private WorldMessageQueue worldMessageQueue;
	
	/**
	 * Creates a new instance of the Chunk class.
	 * @param x The x position of this chunk.
	 * @param y The y position of this chunk.
	 * @param tiles The multi-dimensional array holding the tile types for the chunk.
	 * @param placements The placements that this chunk is composed of.
	 * @param worldMessageQueue The world message queue.
	 */
	public Chunk(short x, short y, TileType[][] tiles, Placements placements, WorldMessageQueue worldMessageQueue) {
		this.x                 = x;
		this.y                 = y;
		this.tiles             = tiles;
		this.placements        = placements;
		this.worldMessageQueue = worldMessageQueue;
	}
	
	/**
	 * Get the key of this chunk.
	 * @return The key of this chunk.
	 */
	public String getKey() {
		return Chunk.getChunkKey(this.x, this.y);
	}
	
	/**
	 * Get the x position of the chunk.
	 * @return The x position of the chunk.
	 */
	public short getX() {
		return x;
	}
	
	/**
	 * Get the y position of the chunk.
	 * @return The y position of the chunk.
	 */
	public short getY() {
		return y;
	}
	
	/**
	 * Get the placements.
	 * @return The placements.
	 */
	public Placements getPlacements() {
		return this.placements;
	}
	
	/**
	 * Gets whether this chunk has at least one high priority placements.
	 * A chunk with a high priority placement will always be active.
	 * @return Whether this chunk has at least one high priority placements.
	 */
	public boolean hasHighPriorityPlacement() {
		return this.placements.getHighestPriority() == Priority.HIGH;
	}
	
	/**
	 * Gets whether the specified position is within the vicinity of this chunk.
	 * @param position The position.
	 * @return Whether the position is within the vicinity of this chunk.
	 */
	public boolean isPositionInVicinity(Position position) {
		return position.getChunkX() >= (this.getX() - Constants.WORLD_CHUNK_VICINITY_RANGE) &&
					position.getChunkX() <= (this.getX() + Constants.WORLD_CHUNK_VICINITY_RANGE) && 
						position.getChunkY() >= (this.getY() - Constants.WORLD_CHUNK_VICINITY_RANGE) && 
							position.getChunkY() <= (this.getY() + Constants.WORLD_CHUNK_VICINITY_RANGE);
	}
	
	/**
	 * Get whether the specified position is walkable.
	 * A position is regarded as walkable if there isn't a blocking placements positioned there.
	 * @param x The local x position.
	 * @param y The local y position.
	 * @return Whether the specified position is walkable.
	 */
	public boolean isPositionWalkable(int x, int y) {
		// Get the placement at this position.
		Placement placement = placements.get(x, y);
		// A position is walkable if there is no placement there or if that placement is itself walkable.
		return placement == null || placement.isWalkable();
	}
	
	/**
	 * Tick this chunk.
	 * @param hasTimeChanged Whether the time has changed in the current server tick.
	 * @param time The current time.
	 * @param arePlayersInChunkVicinity Whether any players are in the vicinity of this chunk.
	 * @param placementModificationsHandler The placement modification handler.
	 */
	public void tick(boolean hasTimeChanged, Time time, boolean arePlayersInChunkVicinity, PlacementModificationsHandler placementModificationsHandler) {
		// Keep track of whether the priority of any placement changes as part of this tick.
		// If this happens then it could change whether the chunk should be considered during future ticks.
		boolean hasPlacementPriorityChanged = false;
		// Execute the placement actions for each placement.
		for (Placement placement : this.placements.getAll()) {
			// Ensure that the placement still exists within the placements collection.
			// It may have been removed by another placement as part of this chunk tick.
			// If it doesn't exist then we just need to move on to the next placement.
			if (!this.placements.has(placement)) {
				continue;
			}
			// Get the priority of the placement as this may change as a side-effect of using the item on it.
			Priority priority = placement.getPriority();
			// Are any players nearby?
			if (arePlayersInChunkVicinity) {
				// Players are nearby so we will be be executing actions for both HIGH and MEDIUM priority placements.
				if (placement.getPriority() == Priority.HIGH || placement.getPriority() == Priority.MEDIUM) {
					// Execute the placements actions.
					executePlacementTickActions(placement, time, hasTimeChanged, placementModificationsHandler);
				}
			} else {
				// Players are not nearby, so we will just be executing actions for HIGH priority placements only.
				if (placement.getPriority() == Priority.HIGH) {
					// Execute the placements actions.
					executePlacementTickActions(placement, time, hasTimeChanged, placementModificationsHandler);
				}
			}
			// Check whether the placement has been marked for deletion, and remove it from the placements collection if it has.
			if (placement.isMarkedForDeletion()) {
				this.placements.remove(placement);
			} else {
				// If the priority of the placement has changed then we will have to re-evaluate the highest priority of all placements.
				if (priority != placement.getPriority()) {
					hasPlacementPriorityChanged = true;
				}
			}
		}
		// If the priority of any placemetns have changed then the highest priority of this
		// chunk's placements will need to be re-evaluted in order to be kept up-to-date.
		if (hasPlacementPriorityChanged) {
			this.placements.evaluteHighestPriority();
		}
	}
	
	/**
	 * Use an item at the position within the chunk and return the modification made in its use.
	 * @param item The item to use.
	 * @param x The local x position.
	 * @param y The local y position.
	 * @param placementModificationsHandler The placement modification handler.
	 * @return Any modification made to the item.
	 */
	public ItemType useItem(ItemType item, int x, int y, PlacementModificationsHandler placementModificationsHandler) {
		// Get the placement at this position.
		Placement targetPlacement = placements.get(x, y);
		// If there is a placement at this position then we will use the item on it
		// unless the placement has no actions associated that can handle an item use.
		if (targetPlacement != null) {
			// Get the priority of the placement as this may change as a side-effect of using the item on it.
			Priority priority = targetPlacement.getPriority();
			// We are using the item on a placement, get the modification made to the used item.
			ItemType modification = this.executePlacementInteractionAction(targetPlacement, item, placementModificationsHandler);
			// Check whether the placement has been marked for deletion, and remove it from the placements collection if it has.
			if (targetPlacement.isMarkedForDeletion()) {
				this.placements.remove(targetPlacement);
			}
			// If the priority of the placement has changed then we will have to re-evaluate the highest priority of all placements.
			if (priority != targetPlacement.getPriority()) {
				this.placements.evaluteHighestPriority();
			}
			// Return the modification made to the used item.
			return modification;
		}
		// Try to use the item on a tile, this may create a placement.
		Placement createdPlacement = PlacementFactory.create(this.tiles[x][y], item, (short)x, (short)y);
		// If a placement was created in the process of using the item on the tile then add it to our placements collection.
		if (createdPlacement != null) {
			// Add the newly created placement.
			this.placements.add(createdPlacement);
			// Get the absolute world position of the placement.
			Position placementPosition = new Position((this.x * Constants.WORLD_CHUNK_SIZE) + createdPlacement.getX(), (this.y * Constants.WORLD_CHUNK_SIZE) + createdPlacement.getY());
			// Allow the placement modifications handler to take it from here.
			placementModificationsHandler.onPlacementCreated(createdPlacement, placementPosition);
		}
		// There was no way to use the item at the position.
		return item;
	}
	
	/**
	 * Serialise the chunk to JSON to be persisted to disk.
	 * @return The chunk state as JSON.
	 */
	public JSONObject serialise() {
		// Create the JSON object that will represent this chunk.
		JSONObject chunkState = new JSONObject();
		// Set the position.
		chunkState.put("x", this.x);
		chunkState.put("y", this.y);
		// Create a JSON array to hold placements state.
		JSONArray placementArray = new JSONArray();
		// Add placements state.
		for (Placement placement : this.placements.getAll()) {
			placementArray.put(placement.serialise());
		}
		// Return the chunk state.
		return chunkState;
	}
	
	/**
	 * Gets a unique hash key for a chunk x/y position.
	 * @param x The chunk x position.
	 * @param y The chunk y position.
	 * @return The key.
	 */
	public static String getChunkKey(int x, int y) {
		return x + "-" + y;
	}
	
	/**
	 * Converts a world postition to a local chunk position.
	 * @param position The world position.
	 * @return The local chunk position.
	 */
	public static short convertWorldToLocalPosition(short position) {
		return (short)convertWorldToLocalPosition((int)position);
	}
	
	/**
	 * Converts a world postition to a local chunk position.
	 * @param position The world position.
	 * @return The local chunk position.
	 */
	public static int convertWorldToLocalPosition(int position) {
		return (position + Constants.WORLD_SIZE) % Constants.WORLD_CHUNK_SIZE;
	}
	
	/**
	 * Execute the server tick and time update actions for the specified placement.
	 * @param placement The placement.
	 * @param time the time.
	 * @param hasTimeChanged Whether the time has changed on this server tick.
	 * @param placementModificationsHandler The placement modification handler.
	 */
	private void executePlacementTickActions(Placement placement, Time time, boolean hasTimeChanged, PlacementModificationsHandler placementModificationsHandler) {
		// Get the absolute world position of the placement.
		Position placementPosition = new Position((this.x * Constants.WORLD_CHUNK_SIZE) + placement.getX(), (this.y * Constants.WORLD_CHUNK_SIZE) + placement.getY());
		// Create the executor that will execute the placement server tick and time update actions.	
		IPlacementActionsExecutor executor = new IPlacementActionsExecutor() {
			@Override
			public ItemType execute(IActionablePlacement action) {
				// Execute the placements action that is called once per server tick.
				action.onServerTick(placement);
				// Execute the placements action that is called for a time change if it has.
				if (hasTimeChanged) {
					action.onTimeUpdate(placement, time);
				}
				// There was no item used in the execution of these actions. 
				return ItemType.NONE;
			}
		};
		// Execute the server tick and time update placement actions.
		placement.executeActions(placementPosition, executor, placementModificationsHandler, worldMessageQueue);
	}
	
	/**
	 * Execute the interaction action with an item for the specified placement.
	 * @param placement The placement to interact with.
	 * @param item The item used in the interaction.
	 * @param placementModificationsHandler The placement modification handler.
	 * @return The modification made to the item used in the interaction.
	 */
	private ItemType executePlacementInteractionAction(Placement placement, ItemType item, PlacementModificationsHandler placementModificationsHandler) {
		// Get the absolute world position of the placement.
		Position placementPosition = new Position((this.x * Constants.WORLD_CHUNK_SIZE) + placement.getX(), (this.y * Constants.WORLD_CHUNK_SIZE) + placement.getY());
		// Create the executor that will execute the placement interaction action.	
		IPlacementActionsExecutor executor = new IPlacementActionsExecutor() {
			@Override
			public ItemType execute(IActionablePlacement action) {
				return action.onInteraction(placement, item);
			}
		};
		// Execute the interaction placement action, returning any modification made to the item use in the interaction.
		return placement.executeActions(placementPosition, executor, placementModificationsHandler, worldMessageQueue);
	}
}
