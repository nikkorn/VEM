package gaia.server.world.chunk;

import org.json.JSONArray;
import org.json.JSONObject;
import gaia.Position;
import gaia.server.Constants;
import gaia.server.items.ItemType;
import gaia.server.world.messaging.WorldMessageQueue;
import gaia.server.world.messaging.messages.ContainerSlotChangedMessage;
import gaia.server.world.messaging.messages.PlacementOverlayChangedMessage;
import gaia.server.world.messaging.messages.PlacementUnderlayChangedMessage;
import gaia.server.world.tile.TileType;
import gaia.server.world.tile.placement.Placement;
import gaia.server.world.tile.placement.PlacementOverlay;
import gaia.server.world.tile.placement.PlacementUnderlay;
import gaia.server.world.tile.placement.Priority;
import gaia.time.Time;

/**
 * A world chunk.
 */
public class Chunk implements IChunkDetails {
	/**
	 * The static tiles that this chunk is composed of.
	 */
	private TileType[][] tiles = new TileType[Constants.WORLD_CHUNK_SIZE][Constants.WORLD_CHUNK_SIZE];
	/**
	 * The placements that this chunk is composed of.
	 */
	private Placement[][] placements = new Placement[Constants.WORLD_CHUNK_SIZE][Constants.WORLD_CHUNK_SIZE];
	/**
	 * The x/y positions of the chunk.
	 */
	private int x, y;
	/**
	 * Whether this chunk has at least one high priority placement.
	 * A chunk with a high priority placement will always be active.
	 */
	private boolean hasHighPriorityPlacement = false;
	
	/**
	 * Creates a new instance of the ChunkInformation class.
	 * @param x The x position of this chunk.
	 * @param y The y position of this chunk.
	 * @param tiles The multi-dimensional array holding the tile types for the chunk.
	 * @param placements The multi-dimensional array holding the placements that this chunk is composed of.
	 */
	public Chunk(int x, int y, TileType[][] tiles, Placement[][] placements) {
		this.x          = x;
		this.y          = y;
		this.tiles      = tiles;
		this.placements = placements;
		// Determine whether any placement is a high priority one.
		for (int placementX = 0; placementX < Constants.WORLD_CHUNK_SIZE; placementX++) {
			for (int placementY = 0; placementY < Constants.WORLD_CHUNK_SIZE; placementY++) {
				// Get the placement at the current position.
				Placement placement = placements[placementX][placementY];
				// Is this a high priority placement?
				if (placement != null && placement.getPriority() == Priority.HIGH) {
					this.hasHighPriorityPlacement = true;
				}
			}
		}
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
	public int getX() {
		return x;
	}
	
	/**
	 * Get the y position of the chunk.
	 * @return The y position of the chunk.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Get the tiles array.
	 * @return The tiles array.
	 */
	@Override
	public TileType[][] getTiles() {
		return this.tiles;
	}
	
	/**
	 * Get the placements array.
	 * @return The placements array.
	 */
	@Override
	public Placement[][] getPlacements() {
		return this.placements;
	}
	
	/**
	 * Gets whether this chunk has at least one high priority placement.
	 * @return Whether this chunk has at least one high priority placement.
	 */
	public boolean hasHighPriorityPlacement() {
		return this.hasHighPriorityPlacement;
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
	 * A position is regarded as walkable if there isn't a blocking placement positioned there.
	 * @param x The x position.
	 * @param y The y position.
	 * @return Whether the specified position is walkable.
	 */
	public boolean isPositionWalkable(int x, int y) {
		// Get the placement at this position.
		Placement placement = placements[x][y];
		// If there is no placement at this position then we can say it is walkable.
		if (placement == null) {
			return true;
		}
		// A position with a placement is walkable if both the placement overlay and underlay are walkable.
		return placement.getOverlay().isWalkable() && placement.getUnderlay().isWalkable();
	}
	
	/**
	 * Tick this chunk.
	 * @param hasTimeChanged Whether the time has changed in the current server tick.
	 * @param time The current time.
	 * @param arePlayersInChunkVicinity Whether any players are in the vicinity of this chunk.
	 * @param worldMessageQueue The world message queue.
	 */
	public void tick(boolean hasTimeChanged, Time time, boolean arePlayersInChunkVicinity, WorldMessageQueue worldMessageQueue) {
		boolean highPriorityPlacementFound = false;
		// Execute placement actions for each placement.
		for (int placementX = 0; placementX < Constants.WORLD_CHUNK_SIZE; placementX++) {
			for (int placementY = 0; placementY < Constants.WORLD_CHUNK_SIZE; placementY++) {
				// Get the placement at the current position.
				Placement placement = placements[placementX][placementY];
				// Do nothing if there is no placement at this position.
				if (placement == null) {
					continue;
				}
				// Does this placement have no action associated with it?
				if (placement.getAction() == null) {
					// We cannot execute actions for a placement when there are none!
				} else if (arePlayersInChunkVicinity) {
					// Players are nearby so we will be be executing actions for both HIGH and MEDIUM priority placements.
					if (placement.getPriority() == Priority.HIGH || placement.getPriority() == Priority.MEDIUM) {
						// Execute the placement actions.
						executePlacementActions(placement, placementX, placementY, time, hasTimeChanged, worldMessageQueue);
					}
				} else {
					// Players are not nearby, so we will just be executing actions for HIGH priority placements only.
					if (placement.getPriority() == Priority.HIGH) {
						// Execute the placement actions.
						executePlacementActions(placement, placementX, placementY, time, hasTimeChanged, worldMessageQueue);
					}
				}
				// Is this a high priority placement?
				if (placement.getPriority() == Priority.HIGH) {
					highPriorityPlacementFound = true;
				}
			}
		}
		// Set whether this chunk contains any high priority placements.
		// This will impact whether the chunk stays active when no player is in it.
		this.hasHighPriorityPlacement = highPriorityPlacementFound;
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
		// Create a JSON array to hold placement state.
		JSONArray placementArray = new JSONArray();
		// Add placement state.
		for (int placementX = 0; placementX < Constants.WORLD_CHUNK_SIZE; placementX++) {
			for (int placementY = 0; placementY < Constants.WORLD_CHUNK_SIZE; placementY++) {
				// Get the placement at the current position.
				Placement currentPlacement = placements[placementX][placementY];
				// Only serialise placements that actually exist.
				if (currentPlacement != null) {
					placementArray.put(currentPlacement.serialise());
				}
			}
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
	 * Execute relevant actions for the specified placement.
	 * @param placement The placement.
	 * @param placementX The placement x position within this chunk.
	 * @param placementY The placement y position within this chunk.
	 * @param time the time.
	 * @param hasTimeChanged Whether the time has changed on this server tick.
	 * @param worldMessageQueue The world message queue.
	 */
	private void executePlacementActions(Placement placement, int placementX, int placementY, Time time, boolean hasTimeChanged, WorldMessageQueue worldMessageQueue) {
		// A side effect of executing placement actions could be changes to overlay, underlay and container state.
		// We need to respond to any of these changes and add a message to the world message queue to let people know.
		PlacementUnderlay preActionUnderlay    = placement.getUnderlay();
		PlacementOverlay preActionOverlay      = placement.getOverlay();
		ItemType[] preActionContainerItemTypes = null;
		// The placement may not even have a container.
		if (placement.getContainer() != null) {
			preActionContainerItemTypes = placement.getContainer().asItemTypeArray();
		}
		// Execute the placement action that is called once per server tick.
		placement.getAction().onServerTick(placement);
		// Execute the placement action that is called for a time change if it has.
		if (hasTimeChanged) {
			placement.getAction().onTimeUpdate(placement, time);
		}
		// Has the underlay changed?
		if (placement.getUnderlay() != preActionUnderlay) {
			// Get the world position of the placement.
			Position placementPosition = new Position((this.x * Constants.WORLD_CHUNK_SIZE) + placementX, (this.y * Constants.WORLD_CHUNK_SIZE) + placementY);
			// Add a message to the world message queue to notify of the change.
			worldMessageQueue.add(new PlacementUnderlayChangedMessage(placement.getUnderlay(), placementPosition));
		}
		// Has the overlay changed?
		if (placement.getOverlay() != preActionOverlay) {
			// Get the world position of the placement.
			Position placementPosition = new Position((this.x * Constants.WORLD_CHUNK_SIZE) + placementX, (this.y * Constants.WORLD_CHUNK_SIZE) + placementY);
			// Add a message to the world message queue to notify of the change.
			worldMessageQueue.add(new PlacementOverlayChangedMessage(placement.getOverlay(), placementPosition));
		}
		// Handle changes to the state of the container if we have one.
		if (placement.getContainer() != null) {
			for (int containerItemIndex = 0; containerItemIndex < placement.getContainer().size(); containerItemIndex++) {
				// Has the item type at the current container slot changed?
				if (preActionContainerItemTypes[containerItemIndex] != placement.getContainer().get(containerItemIndex)) {
					// Get the world position of the placement.
					Position placementPosition = new Position((this.x * Constants.WORLD_CHUNK_SIZE) + placementX, (this.y * Constants.WORLD_CHUNK_SIZE) + placementY);
					// Add a message to the world message queue to notify of the change.
					worldMessageQueue.add(new ContainerSlotChangedMessage(containerItemIndex, placement.getContainer().get(containerItemIndex), placementPosition));
				}
			}
		}
	}
}
