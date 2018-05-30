package server.world.chunk;

import org.json.JSONArray;
import org.json.JSONObject;
import server.Constants;
import server.world.TileType;
import server.world.placement.Placement;
import server.world.placement.Priority;

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
	 */
	public void tick(boolean hasTimeChanged) {
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
				// We only care about placements with an action, excluding those with low priorities. 
				if (placement.getAction() != null && placement.getPriority() != Priority.LOW) {
					// Execute the placement action that is called once per server tick.
					placement.getAction().onServerTick(placement.getState(), placement.getContainer());
					// Execute the placement action that is called for a time change if it has.
					if (hasTimeChanged) {
						placement.getAction().onTimeUpdate(placement.getState(), placement.getContainer());
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
}
