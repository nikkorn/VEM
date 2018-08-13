package gaia.server.world.chunk;

import org.json.JSONArray;
import org.json.JSONObject;
import gaia.Constants;
import gaia.server.items.ItemType;
import gaia.server.world.messaging.WorldMessageQueue;
import gaia.server.world.messaging.messages.ContainerSlotChangedMessage;
import gaia.server.world.messaging.messages.PlacementOverlayChangedMessage;
import gaia.server.world.messaging.messages.PlacementUnderlayChangedMessage;
import gaia.world.TileType;
import gaia.server.world.placements.Placement;
import gaia.server.world.placements.Placements;
import gaia.server.world.placements.Priority;
import gaia.time.Time;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementUnderlay;
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
	 * Whether this chunk has at least one high priority placements.
	 * A chunk with a high priority placements will always be active.
	 */
	private boolean hasHighPriorityPlacement = false;
	
	/**
	 * Creates a new instance of the ChunkInformation class.
	 * @param x The x position of this chunk.
	 * @param y The y position of this chunk.
	 * @param tiles The multi-dimensional array holding the tile types for the chunk.
	 * @param placements The placements that this chunk is composed of.
	 */
	public Chunk(short x, short y, TileType[][] tiles, Placements placements) {
		this.x          = x;
		this.y          = y;
		this.tiles      = tiles;
		this.placements = placements;
		// Determine whether any placements is a high priority one.
		for (Placement placement : placements.getAll()) {
			// Is this a high priority placements?
			if (placement.getPriority() == Priority.HIGH) {
				this.hasHighPriorityPlacement = true;
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
	 * Get the tiles array.
	 * @return The tiles array.
	 */
	public TileType[][] getTiles() {
		return this.tiles;
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
	 * @return Whether this chunk has at least one high priority placements.
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
	 * A position is regarded as walkable if there isn't a blocking placements positioned there.
	 * @param x The x position.
	 * @param y The y position.
	 * @return Whether the specified position is walkable.
	 */
	public boolean isPositionWalkable(int x, int y) {
		// Get the placements at this position.
		Placement placement = placements.get(x, y);
		// If there is no placements at this position then we can say it is walkable.
		if (placement == null) {
			return true;
		}
		// A position with a placements is walkable if both the placements overlay and underlay are walkable.
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
		// Execute placements actions for each placements.
		for (Placement placement : this.placements.getAll()) {
			// Does this placements have no action associated with it?
			if (placement.getAction() == null) {
				// We cannot execute actions for a placements when there are none!
			} else if (arePlayersInChunkVicinity) {
				// Players are nearby so we will be be executing actions for both HIGH and MEDIUM priority placements.
				if (placement.getPriority() == Priority.HIGH || placement.getPriority() == Priority.MEDIUM) {
					// Execute the placements actions.
					executePlacementActions(placement, placement.getX(), placement.getY(), time, hasTimeChanged, worldMessageQueue);
				}
			} else {
				// Players are not nearby, so we will just be executing actions for HIGH priority placements only.
				if (placement.getPriority() == Priority.HIGH) {
					// Execute the placements actions.
					executePlacementActions(placement, placement.getX(), placement.getY(), time, hasTimeChanged, worldMessageQueue);
				}
			}
			// Is this a high priority placements?
			if (placement.getPriority() == Priority.HIGH) {
				highPriorityPlacementFound = true;
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
	 * Execute relevant actions for the specified placements.
	 * @param placement The placements.
	 * @param placementX The placements x position within this chunk.
	 * @param placementY The placements y position within this chunk.
	 * @param time the time.
	 * @param hasTimeChanged Whether the time has changed on this server tick.
	 * @param worldMessageQueue The world message queue.
	 */
	private void executePlacementActions(Placement placement, short placementX, short placementY, Time time, boolean hasTimeChanged, WorldMessageQueue worldMessageQueue) {
		// A side effect of executing placements actions could be changes to overlay, underlay and container state.
		// We need to respond to any of these changes and add a message to the world message queue to let people know.
		PlacementUnderlay preActionUnderlay    = placement.getUnderlay();
		PlacementOverlay preActionOverlay      = placement.getOverlay();
		ItemType[] preActionContainerItemTypes = null;
		// The placements may not even have a container.
		if (placement.getContainer() != null) {
			preActionContainerItemTypes = placement.getContainer().asItemTypeArray();
		}
		// Execute the placements action that is called once per server tick.
		placement.getAction().onServerTick(placement);
		// Execute the placements action that is called for a time change if it has.
		if (hasTimeChanged) {
			placement.getAction().onTimeUpdate(placement, time);
		}
		// Get the world position of the placements.
		short placementXPosition   = (short) ((this.x * Constants.WORLD_CHUNK_SIZE) + placementX);
		short placementYPosition   = (short) ((this.y * Constants.WORLD_CHUNK_SIZE) + placementY);
		Position placementPosition = new Position(placementXPosition, placementYPosition);
		// Has the underlay changed?
		if (placement.getUnderlay() != preActionUnderlay) {
			// Add a message to the world message queue to notify of the change.
			worldMessageQueue.add(new PlacementUnderlayChangedMessage(placement.getUnderlay(), placementPosition));
		}
		// Has the overlay changed?
		if (placement.getOverlay() != preActionOverlay) {
			// Add a message to the world message queue to notify of the change.
			worldMessageQueue.add(new PlacementOverlayChangedMessage(placement.getOverlay(), placementPosition));
		}
		// Handle changes to the state of the container if we have one.
		if (placement.getContainer() != null) {
			for (int containerItemIndex = 0; containerItemIndex < placement.getContainer().size(); containerItemIndex++) {
				// Has the item type at the current container slot changed?
				if (preActionContainerItemTypes[containerItemIndex] != placement.getContainer().get(containerItemIndex)) {
					// Add a message to the world message queue to notify of the change.
					worldMessageQueue.add(new ContainerSlotChangedMessage(containerItemIndex, placement.getContainer().get(containerItemIndex), placementPosition));
				}
			}
		}
	}
}
