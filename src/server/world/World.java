package server.world;

import org.json.JSONObject;
import server.Constants;
import server.world.chunk.Chunk;
import server.world.chunk.Chunks;
import server.world.generation.WorldGenerator;
import server.world.messaging.WorldMessageQueue;
import server.world.players.Players;
import server.world.time.Time;

/**
 * A game world composed of separate chunks.
 */
public class World {
	/**
	 * The chunks that the world is composed of.
	 */
	private Chunks chunks;
	/**
	 * The players within the world.
	 */
	private Players players = new Players();
	/**
	 * The world time.
	 */
	private Time time;
	/**
	 * The world generator.
	 */
	private WorldGenerator worldGenerator;
	/**
	 * The world message queue.
	 */
	private WorldMessageQueue worldMessageQueue = new WorldMessageQueue();
	
	/**
	 * Creates a new instance of the World class.
	 * @param chunks The chunks that the world is composed of.
	 * @param time The world time.
	 * @param worldGenerator The world generator.
	 */
	public World(Chunks chunks, Time time, WorldGenerator worldGenerator) {
		this.chunks         = chunks;
		this.time           = time;
		this.worldGenerator = worldGenerator;
	}
	
	/**
	 * Get the chunks that the world is composed of.
	 * @return The chunks that the world is composed of.
	 */
	public Chunks getChunks() {
		return this.chunks;
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
		// The position can only be walkable if it is a loaded chunk.
		if (!this.chunks.isChunkCached(position.getChunkX(), position.getChunkY())) {
			// The chunk that this position is within is not loaded.
			return false;
		}
		// Get the chunk that this position is within.
		Chunk target = this.chunks.getCachedChunk(position.getChunkX(), position.getChunkY());
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
}
