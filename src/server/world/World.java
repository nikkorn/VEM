package server.world;

import java.util.ArrayList;
import java.util.HashMap;
import server.world.generation.WorldGenerator;

/**
 * Represents the game world.
 */
public class World {
	/**
	 * The world generator.
	 */
	private WorldGenerator worldGenerator;
	/**
	 * The world seed.
	 */
	private long seed;
	/**
	 * The cached chunks.
	 */
	private HashMap<String, Chunk> cachedChunks = new HashMap<String, Chunk>();
	
	/**
	 * Creates a new instance of the WorldInformation class.
	 * @param seed The world seed.
	 */
	public World(long seed) {
		// Set the seed.
		this.seed = seed;
		// Create the world generator.
		this.worldGenerator = new WorldGenerator(seed);
	}
	
	/**
	 * Get the world seed.
	 * @return The world seed.
	 */
	public long getSeed() {
		return this.seed;
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
			// Create the chunk.
			Chunk chunkInformation = new Chunk(x, y, this.worldGenerator);
			// Cache this chunk so that we don't have to keep generating it.
			this.cachedChunks.put(chunkKey, chunkInformation);
			// Return the chunk.
			return chunkInformation;
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
	 * Gets a unique hash key for an x/y position.
	 * @param x The x position.
	 * @param y The y position.
	 * @return The key.
	 */
	private static String getChunkKey(int x, int y) {
		return x + "-" + y;
	}
}
