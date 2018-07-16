package server.world;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;
import server.Constants;
import server.world.chunk.Chunk;
import server.world.chunk.ChunkFactory;
import server.world.generation.WorldGenerator;
import server.world.messaging.WorldMessageQueue;
import server.world.players.IPlayerEventHandler;
import server.world.players.Player;
import server.world.players.Players;
import server.world.time.Time;

/**
 * A game world composed of separate chunks.
 */
public class World {
	/**
	 * The players within the world.
	 */
	private Players players;
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
	 * The world message queue.
	 */
	private WorldMessageQueue worldMessageQueue = new WorldMessageQueue();
	
	/**
	 * Creates a new instance of the World class.
	 * @param time The world time.
	 * @param worldGenerator The world generator.
	 */
	public World(Time time, WorldGenerator worldGenerator) {
		this.time           = time;
		this.worldGenerator = worldGenerator;
		// Create the Players instance, passing handlers for player events.
		this.players = new Players(new IPlayerEventHandler() {
			@Override
			public void onChunkChange(Player player) {
				// TODO Handle player chunk change. This could be a spawn.
			}
		});
	}
	
	/**
	 * Get the world time.
	 * @return The world time.
	 */
	public Time getTime() {
		return this.time;
	}

	/**
	 * Get the world message queue.
	 * @return The world message queue.
     */
	public WorldMessageQueue getWorldMessageQueue() {
		return worldMessageQueue;
	}
	
	/**
	 * Get the players present in the world.
	 * @return The players present in the world.
	 */
	public Players getPlayers() {
		return this.players;
	}
	
	/**
	 * Get the chunk at the x/y position.
	 * @param x The x position of the chunk.
	 * @param y The y position of the chunk.
	 * @return The chunk.
	 */
	public Chunk getChunk(int x, int y) {
		// Check to make sure that we are not trying to get a chunk for an invalid position.
		if (!this.isValidChunkPosition(x, y)) {
			throw new RuntimeException("error: invalid chunk position: x=" + x + " y=" + y);
		}
		// Create the key for the chunk we are looking for.
		String chunkKey = Chunk.getChunkKey(x, y);
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
		this.cachedChunks.put(Chunk.getChunkKey(chunk.getX(), chunk.getY()), chunk);
	}
	
	/**
	 * Get the world spawn position.
	 * @return The world spawn position.
	 */
	public Position getSpawnPosition() {
		// TODO Actually return a valid spawn, excluding tiles we have non-walkable placements at.
		return new Position(12, 12);
	}
	
	/**
	 * Get whether the world position is walkable.
	 * Positions within non-loaded chunks are regarded as not walkable.
	 * @param position The world position.
	 * @return Whether the world position is walkable.
	 */
	public boolean isPositionWalkable(Position position) {
		// The position can only be walkable if it is an a loaded chunk.
		if (!this.cachedChunks.containsKey(Chunk.getChunkKey(position.getChunkX(), position.getChunkY()))) {
			// The chunk that this position is within is not loaded.
			return false;
		}
		// Get the chunk that this position is within.
		Chunk target = this.cachedChunks.get(Chunk.getChunkKey(position.getChunkX(), position.getChunkY()));
		// Ask the chunk whether the local position is walkable.
		return target.isPositionWalkable(position.getX() % Constants.WORLD_CHUNK_SIZE, position.getY() % Constants.WORLD_CHUNK_SIZE);
	}
	
	/**
	 * Checks whether any connected players are within the vicinity of the specified chunk.
	 * @param chunk The chunk.
	 * @return Whether any connected players are within the vicinity of the specified chunk.
	 */
	public boolean arePlayersInChunkVicinity(Chunk chunk) {
		// Check the position of each connected player.
		for (Position playerPosition : this.players.getPlayerPositions()) {
			// Is the currently connected player in the vicinity of this chunk?
			if (chunk.isPositionInVicinity(playerPosition)) {
				return true;
			}
		}
		// No connected players were in the vicinity of this chunk.
		return false;
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
	 * Checks whether the specified position is a valid chunk position.
	 * @param x The chunk x position.
	 * @param y The chunk y position.
	 * @return Whether the specified position is a valid chunk position.
	 */
	private boolean isValidChunkPosition(int x, int y) {
		// Get the number of chunks on either axis from the world origin to edge.
		int chunksToWorldEdge = Constants.WORLD_CHUNKS_PER_AXIS / 2;
		// Return whether either the x or y positions exceed the world boundaries.
		return x > -chunksToWorldEdge && x < chunksToWorldEdge && y > -chunksToWorldEdge && y < chunksToWorldEdge;
	}
}
