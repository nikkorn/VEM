package server.world;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;
import server.world.chunk.Chunk;
import server.world.chunk.ChunkFactory;
import server.world.generation.WorldGenerator;
import server.world.time.Time;

/**
 * Represents the game world composed of separate chunks.
 */
public class World {
	/**
	 * The world time.
	 */
	private Time time;
	/**
	 * The world generator.
	 */
	private WorldGenerator worldGenerator;
	/**
	 * The cached chunks.
	 */
	private HashMap<String, Chunk> cachedChunks = new HashMap<String, Chunk>();
	
	/**
	 * Creates a new instance of the World class.
	 * @param time The world time.
	 * @param worldGenerator The world generator.
	 */
	public World(Time time, WorldGenerator worldGenerator) {
		this.time           = time;
		this.worldGenerator = worldGenerator;
	}
	
	/**
	 * Get the world time.
	 * @return The world time.
	 */
	public Time getTime() {
		return this.time;
	}
	
	/**
	 * Get the chunk at the x/y position.
	 * @param x The x position of the chunk.
	 * @param y The y position of the chunk.
	 * @return The chunk.
	 */
	public Chunk getChunk(int x, int y) {
		// Create the key for the chunk we are looking for.
		String chunkKey = getChunkKey(x, y);
		// Check whether we have already cached the chunk.
		if (this.cachedChunks.containsKey(chunkKey)) {
			// We already have this chunk!
			return this.cachedChunks.get(chunkKey);
		} else {
			// Create the new chunk.
			Chunk chunk = ChunkFactory.createNewChunk(worldGenerator, x, y);
			// Cache this chunk so that we don't have to keep generating it.
			this.cachedChunks.put(chunkKey, chunk);
			// Return the chunk.
			return chunk;
		}
	}
	
	/**
	 * Get all cached chunks.
	 * @return All cached chunks.
	 */
	public ArrayList<Chunk> getCachedChunks() {
		return new ArrayList<Chunk>(this.cachedChunks.values());
	}

	/**
	 * Add a cached chunk to the world.
	 * @param chunk The chunk to add.
     */
	public void addCachedChunk(Chunk chunk) {
		this.cachedChunks.put(World.getChunkKey(chunk.getX(), chunk.getY()), chunk);
	}
	
	/**
	 * Get the world state as JSON.
	 * @return The world state as JSON.
	 */
	public JSONObject getState() {
		// Create the JSON object that will represent this world.
		JSONObject worldState = new JSONObject();
		// Set the world seed.
		worldState.put("seed", this.worldGenerator.getSeed());
		// Set the world time.
		worldState.put("time", this.time.getState());
		// Return the world state.
		return worldState;
	}
	
	/**
	 * Gets a unique hash key for an x/y position.
	 * @param x The x position.
	 * @param y The y position.
	 * @return The key.
	 */
	private static String getChunkKey(int x, int y) {
		return x + "-" + y;
	}
}
