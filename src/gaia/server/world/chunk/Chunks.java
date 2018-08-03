package gaia.server.world.chunk;

import java.util.ArrayList;
import java.util.HashMap;
import gaia.server.Constants;
import gaia.server.world.generation.WorldGenerator;
import gaia.server.world.messaging.WorldMessageQueue;
import gaia.server.world.messaging.messages.ChunkLoadedMessage;
import gaia.server.world.players.Player;

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
	 * Create a new instance of the Chunks class.
	 * @param worldGenerator The world generator instance to use in building chunks.
	 */
	public Chunks(WorldGenerator worldGenerator) {
		this.worldGenerator = worldGenerator;
	}
	
	/**
	 * Create a new instance of the Chunks class.
	 * @param worldGenerator The world generator instance to use in building chunks.
	 * @param existingChunks The existing chunks to be added.
	 */
	public Chunks(WorldGenerator worldGenerator, ArrayList<Chunk> existingChunks) {
		this(worldGenerator);
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
	 * Called when a player changes chunk positions.
	 * Any chunks that are in the vicinity of the player will have to be loaded.
	 * @param player The player that has changed chunk positions.
	 * @param worldMessageQueue The world message queue.
	 */
	public void onPlayerChunkChange(Player player, WorldMessageQueue worldMessageQueue) {
		// Get the position of the chunk that the player has moved to.
		short playerChunkX = player.getPositon().getChunkX();
		short playerChunkY = player.getPositon().getChunkY();
		// Get the chunk range that we regard as being the 'vicinity' of another chunk.
		short range = Constants.WORLD_CHUNK_VICINITY_RANGE;
		// Check every chunk position in the vicinity of the player.
		for (short chunkX = (short)(playerChunkX - range); chunkX <= (playerChunkX + range); chunkX++) {
			for (short chunkY = (short)(playerChunkY - range); chunkY <= (playerChunkY + range); chunkY++) {
				// Check to make sure that we are looking at a valid chunk position as we could be at the world edge.
				if (!isValidChunkPosition(chunkX, chunkY)) {
					continue;
				}
				// Has this chunk already been cached?
				if (this.isChunkCached(chunkX, chunkY)) {
					// Get the cached chunk.
					Chunk chunk = this.getCachedChunk(chunkX, chunkY);
					// Has the player previously been in the vicinity of the current chunk?
					// If not then we will need to notify the player of the chunk details.
					if (!player.hasVisitedChunk(chunk)) {
						// Add a world message to notify the player of the chunk details.
						worldMessageQueue.add(new ChunkLoadedMessage(player.getPlayerId(), chunk));
						// Now we can say that the player has visited the chunk.
						player.addVisitedChunk(chunk);
					}
				} else {
					// The player has wandered into the vicinity of chunk that has never
					// been loaded before. Create the new chunk using the world generator
					Chunk chunk = ChunkFactory.createNewChunk(worldGenerator, chunkX, chunkY);
					// Cache this chunk so that we don't have to keep generating it.
					this.cachedChunks.put(chunk.getKey(), chunk);
					// Add a world message to notify the player of the chunk details.
					worldMessageQueue.add(new ChunkLoadedMessage(player.getPlayerId(), chunk));
					// Now we can say that the player has visited the chunk.
					player.addVisitedChunk(chunk);
				}
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
