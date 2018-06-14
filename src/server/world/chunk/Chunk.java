package server.world.chunk;

import org.json.JSONArray;
import org.json.JSONObject;
import server.Constants;
import server.world.Position;
import server.world.messaging.WorldMessageQueue;
import server.world.messaging.messages.PlacementOverlayChangedMessage;
import server.world.messaging.messages.PlacementUnderlayChangedMessage;
import server.world.tile.TileType;
import server.world.tile.placement.Placement;
import server.world.tile.placement.PlacementOverlay;
import server.world.tile.placement.PlacementUnderlay;
import server.world.tile.placement.Priority;
import server.world.time.Time;

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
	private Placement[][] placements = new Placement[Constants.WORLD_CHUNK_SIZE][Constants.WORLD_CHUNK_SIZE];
	/**
	 * The x/y positions of the chunk.
	 */
	private int x, y;
	/**
	 * Whether the chunk is dirty.
	 * Dirty chunks need to be persisted back to disk.
	 */
	private boolean isDirty = true;
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
	 * Gets whether this chunk has at least one high priority placement.
	 * @return Whether this chunk has at least one high priority placement.
	 */
	public boolean hasHighPriorityPlacement() {
		return this.hasHighPriorityPlacement;
	}
	
	/**
	 * Get whether the chunk is dirty.
	 * Dirty chunks need to be persisted back to disk.
	 * @return Whether the chunk is dirty.
	 */
	public boolean isDirty() {
		return this.isDirty;
	}
	
	/**
	 * Gets whether the specified position is within the vicinity of this chunk.
	 * @param position The position.
	 * @return Whether the position is within the vicinity of this chunk.
	 */
	public boolean isPositionInVicinity(Position position) {
		return position.getChunkX() == this.getX() && position.getChunkY() == this.getY();
	}
	
	/**
	 * Get the type of tile at the specified position.
	 * @param x The x position of the tile.
	 * @param y The y position of the tile.
	 * @return The type of tile at the specified position.
	 */
	public TileType getTileType(int x, int y) {
		return tiles[x % Constants.WORLD_CHUNK_SIZE][y % Constants.WORLD_CHUNK_SIZE];
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
		PlacementUnderlay preActionUnderlay = placement.getUnderlay();
		PlacementOverlay preActionOverlay   = placement.getOverlay();
		
		// TODO Handle changes to the state of the container.
		
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
		
		// TODO Handle changes to the state of the container.
	}
}
