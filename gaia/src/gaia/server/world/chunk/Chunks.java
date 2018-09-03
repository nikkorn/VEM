package gaia.server.world.chunk;

import java.util.ArrayList;
import java.util.HashMap;
import gaia.Constants;
import gaia.server.world.generation.WorldGenerator;
import gaia.server.world.messaging.WorldMessageQueue;
import gaia.server.world.placements.Placement;
import gaia.world.Position;

/**
 * Represents the collection of chunks that the world is composed of.
 */
public class Chunks {
	/**
	 * The cached chunks.
	 */
	private HashMap<String, Chunk> cachedChunks = new HashMap<String, Chunk>();
	/**
	 * The world generator instance to use in building chunks.
	 */
	private WorldGenerator worldGenerator;
	/**
	 * The world message queue.
	 */
	private WorldMessageQueue worldMessageQueue;
	
	/**
	 * Create a new instance of the Chunks class.
	 * @param worldGenerator The world generator instance to use in building chunks.
	 * @param worldMessageQueue The world message queue.
	 */
	public Chunks(WorldGenerator worldGenerator, WorldMessageQueue worldMessageQueue) {
		this.worldGenerator    = worldGenerator;
		this.worldMessageQueue = worldMessageQueue;
	}
	
	/**
	 * Create a new instance of the Chunks class.
	 * @param worldGenerator The world generator instance to use in building chunks.
	 * @param worldMessageQueue The world message queue.
	 * @param existingChunks The existing chunks to be added.
	 */
	public Chunks(WorldGenerator worldGenerator, WorldMessageQueue worldMessageQueue, ArrayList<Chunk> existingChunks) {
		this(worldGenerator, worldMessageQueue);
		for (Chunk chunk : existingChunks) {
			this.addCachedChunk(chunk);
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
	 * Get the cached chunk at the x/y position.
	 * An error will be thrown if the target chunk has not already been cached.
	 * @param x The x position of the chunk.
	 * @param y The y position of the chunk.
	 * @return The cached chunk.
	 */
	public Chunk getCachedChunk(int x, int y) {
		// Check to make sure that we are not trying to get a chunk for an invalid position.
		if (!isValidChunkPosition(x, y)) {
			throw new RuntimeException("error: invalid chunk position: x=" + x + " y=" + y);
		}
		// Create the key for the chunk we are looking for.
		String chunkKey = Chunk.getChunkKey(x, y);
		// Check whether we have already cached the chunk.
		if (this.cachedChunks.containsKey(chunkKey)) {
			// We already have this chunk!
			return this.cachedChunks.get(chunkKey);
		} else {
			// We have not loaded this chunk yet!
			throw new RuntimeException("error: chunk at position: x=" + x + " y=" + y + " is not cached");
		}
	}
	
	/**
	 * Get the chunk at the x/y position.
	 * If the chunk has not already been cached then it will be loaded.
	 * @param x The x position of the chunk.
	 * @param y The y position of the chunk.
	 * @return The chunk.
	 */
	public Chunk getChunk(int x, int y) {
		// Check to make sure that we are not trying to get a chunk for an invalid position.
		if (!isValidChunkPosition(x, y)) {
			throw new RuntimeException("error: invalid chunk position: x=" + x + " y=" + y);
		}
		// Create the key for the chunk we are looking for.
		String chunkKey = Chunk.getChunkKey(x, y);
		// Check whether we have already cached the chunk.
		if (this.cachedChunks.containsKey(chunkKey)) {
			// We already have this chunk!
			return this.cachedChunks.get(chunkKey);
		} else {
			// This chunk has not been cached yet, so it will need to be loaded.
			// Create the new chunk using the world generator.
			Chunk chunk = ChunkFactory.createNewChunk(worldGenerator, (short)x, (short)y, worldMessageQueue);
			// Cache this chunk so that we don't have to keep generating it.
			this.cachedChunks.put(chunk.getKey(), chunk);
			// Return our newly created chunk.
			return chunk;
		}
	}
	
	/**
	 * Get the placement at the x/y position.
	 * An error will be thrown if the chunk that the placement has not already been cached.
	 * @param x The x position of the placement.
	 * @param y The y position of the placement.
	 * @return The placement.
	 */
	public Placement getPlacement(int x, int y) {
		// Get the chunk that this placement is in, we expect it to be cached.
		Chunk targetChunk = this.getCachedChunk(Position.convertWorldToChunkPosition(x), Position.convertWorldToChunkPosition(y));
		// Get the placement at the position, or null if there is no placement.
		return targetChunk.getPlacements().get(Chunk.convertWorldToLocalPosition(x), Chunk.convertWorldToLocalPosition(y));
	}
	
	/**
	 * Get whether the chunk at the x/y position is cached.
	 * @param x The x position of the chunk.
	 * @param y The y position of the chunk.
	 * @return The cached chunk.
	 */
	public boolean isChunkCached(int x, int y) {
		return this.cachedChunks.containsKey(Chunk.getChunkKey(x, y));
	}
	
	/**
	 * Checks whether the specified position is a valid chunk position.
	 * @param x The chunk x position.
	 * @param y The chunk y position.
	 * @return Whether the specified position is a valid chunk position.
	 */
	public static boolean isValidChunkPosition(int x, int y) {
		// Get the number of chunks on either axis from the world origin to edge.
		int chunksToWorldEdge = Constants.WORLD_CHUNKS_PER_AXIS / 2;
		// Return whether either the x or y positions exceed the world boundaries.
		return x > -chunksToWorldEdge && x < chunksToWorldEdge && y > -chunksToWorldEdge && y < chunksToWorldEdge;
	}
	
	/**
	 * Called when a player visits a chunk.
	 * Any un-cached chunks that are in the vicinity of the visited chunk will have to be loaded.
	 * @param visitedChunkX The x position of the visited chunk.
	 * @param visitedChunkY The y position of the visited chunk.
	 */
	public void onPlayerChunkVisit(short visitedChunkX, short visitedChunkY) {
		// Get the chunk range that we regard as being the 'vicinity' of another chunk.
		short range = Constants.WORLD_CHUNK_VICINITY_RANGE;
		// Check every chunk position in the vicinity of the player.
		for (short chunkX = (short)(visitedChunkX - range); chunkX <= (visitedChunkX + range); chunkX++) {
			for (short chunkY = (short)(visitedChunkY - range); chunkY <= (visitedChunkY + range); chunkY++) {
				// Check to make sure that we are looking at a valid chunk position as we could be at the world edge.
				// Also, if this chunk has already been loaded then there is nothing left to do.
				if (!isValidChunkPosition(chunkX, chunkY) || this.isChunkCached(chunkX, chunkY)) {
					continue;
				}
				// The player has wandered into the vicinity of chunk that has never
				// been loaded before. Create the new chunk using the world generator
				Chunk chunk = ChunkFactory.createNewChunk(worldGenerator, chunkX, chunkY, worldMessageQueue);
				// Cache this chunk so that we don't have to keep generating it.
				this.cachedChunks.put(chunk.getKey(), chunk);
			}
		}
	}
	
	/**
	 * Add a cached chunk to the world.
	 * @param chunk The chunk to add.
     */
	private void addCachedChunk(Chunk chunk) {
		this.cachedChunks.put(Chunk.getChunkKey(chunk.getX(), chunk.getY()), chunk);
	}
}
