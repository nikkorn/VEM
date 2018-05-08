package server.world.generation;

import java.util.HashMap;

/**
 * Provides world information derived from RNG and Perlin noise.
 */
public class WorldInformation {
	/**
	 * The world seed.
	 */
	private long seed;
	/**
	 * The cached chunk information.
	 */
	private HashMap<String, ChunkInformation> cachedChunkInfo = new HashMap<String, ChunkInformation>();
	
	/**
	 * Creates a new instance of the WorldInformation class.
	 * @param seed The world seed.
	 */
	public WorldInformation(long seed) {
		this.seed = seed;
	}
	
	/**
	 * Get information about the chunk at the x/y position.
	 * @param x The x position of the chunk.
	 * @param y The y position of the chunk.
	 * @return The chunk information.
	 */
	public ChunkInformation getChunkInformation(int x, int y) {
		// Create the key for the chunk we are looking for.
		String chunkKey = getChunkKey(x, y);
		// Check whether we have already cached the chunk information.
		if (this.cachedChunkInfo.containsKey(chunkKey)) {
			// We already have information for this chunk!
			return this.cachedChunkInfo.get(chunkKey);
		} else {
			// Create the chunk info.
			ChunkInformation chunkInformation = new ChunkInformation(x, y, this.seed);
			// Cache this chunk info so that we don't have to keep generating it.
			this.cachedChunkInfo.put(chunkKey, chunkInformation);
			// Return the chunk information.
			return chunkInformation;
		}
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
