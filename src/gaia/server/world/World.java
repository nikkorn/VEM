package gaia.server.world;

import org.json.JSONObject;
import gaia.Position;
import gaia.server.Constants;
import gaia.server.world.chunk.Chunk;
import gaia.server.world.chunk.Chunks;
import gaia.server.world.generation.WorldGenerator;
import gaia.server.world.messaging.WorldMessageQueue;
import gaia.server.world.players.Players;
import gaia.server.world.time.Clock;
import gaia.server.world.time.Time;

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
	 * The world player spawn position.
	 */
	private Position playerSpawn;
	/**
	 * The world clock.
	 */
	private Clock clock;
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
	 * @param spawn The player spawn.
	 * @param time The world time.
	 * @param worldGenerator The world generator.
	 */
	public World(Chunks chunks, Position spawn, Time time, WorldGenerator worldGenerator) {
		this.chunks         = chunks;
		this.playerSpawn    = spawn;
		this.clock          = new Clock(time);
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
	public Clock getClock() {
		return this.clock;
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
	 * Get the player spawn.
	 * @return The player spawn.
	 */
	public Position getPlayerSpawn() {
		return this.playerSpawn;
	}
	
	/**
	 * Get the world seed.
	 * @return the world seed.
	 */
	public long getSeed() {
		return this.worldGenerator.getSeed();
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
	 * Get the world state serialised to JSON.
	 * @return The world state serialised to JSON.
	 */
	public JSONObject serialise() {
		// Create the JSON object that will represent this world.
		JSONObject worldState = new JSONObject();
		// Set the world seed.
		worldState.put("seed", this.worldGenerator.getSeed());
		// Set the spawn position.
		worldState.put("spawn", Position.serialise(this.playerSpawn));
		// Set the world time.
		worldState.put("time", this.clock.getCurrentTime().getState());
		// Return the world state.
		return worldState;
	}
}
