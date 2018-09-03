package gaia.server.world;

import gaia.world.generation.TileGenerator;
import gaia.world.items.ItemType;
import org.json.JSONObject;
import gaia.Constants;
import gaia.server.world.chunk.Chunk;
import gaia.server.world.chunk.Chunks;
import gaia.server.world.messaging.WorldMessageQueue;
import gaia.server.world.messaging.messages.PlacementLoadedMessage;
import gaia.server.world.placements.Placement;
import gaia.server.world.players.Player;
import gaia.server.world.players.Players;
import gaia.time.Time;
import gaia.world.Position;

/**
 * A game world composed of separate chunks.
 */
public class World implements IPlacementUpdateHandler {
	/**
	 * The chunks that the world is composed of.
	 */
	private Chunks chunks;
	/**
	 * The players within the world.
	 */
	private Players players;
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
	private TileGenerator tileGenerator;
	/**
	 * The world message queue.
	 */
	private WorldMessageQueue worldMessageQueue;
	
	/**
	 * Creates a new instance of the World class.
	 * @param players The players.
	 * @param chunks The chunks that the world is composed of.
	 * @param spawn The player spawn.
	 * @param time The world time.
	 * @param tileGenerator The world generator.
	 * @param worldMessageQueue The world message queue.
	 */
	public World(Players players, Chunks chunks, Position spawn, Time time, TileGenerator tileGenerator, WorldMessageQueue worldMessageQueue) {
		this.players           = players; 
		this.chunks            = chunks;
		this.playerSpawn       = spawn;
		this.clock             = new Clock(time);
		this.tileGenerator     = tileGenerator;
		this.worldMessageQueue = worldMessageQueue;
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
		return this.tileGenerator.getSeed();
	}
	
	/**
	 * Use an item at the specified position and return the modification made in its use.
	 * This could be on a tile, placement or even a player that is in the way.
	 * @param item The item to use.
	 * @param position The position at which to use the item.
	 * @return Any modification made to the item.
	 */
	public ItemType useItem(ItemType item, Position position) {
		// If a player is at the position then we will use the item on them.
		Player targetPlayer = this.players.getPlayerAt(position.getX(), position.getY());
		if (targetPlayer != null) {
			// We are using the item on a player!
			return targetPlayer.onItemUse(item);
		}
		// We are using the item at a world position, possibly on a placement or tile.
		// Firstly, get the chunk that the position is in. This could mean that we need
		// to create a new chunk if the position is within an uncached chunk.
		Chunk targetChunk = this.chunks.getCachedChunk(position.getChunkX(), position.getChunkY());
		// Use the item at the position in the chunk and return any modification made in its use.
		return targetChunk.useItem(item, Chunk.convertWorldToLocalPosition(position.getX()), Chunk.convertWorldToLocalPosition(position.getY()), this);
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
		Chunk targetChunk = this.chunks.getCachedChunk(position.getChunkX(), position.getChunkY());
		// Ask the chunk whether the local position is walkable.
		return targetChunk.isPositionWalkable((position.getX() + Constants.WORLD_SIZE) % Constants.WORLD_CHUNK_SIZE, (position.getY() + Constants.WORLD_SIZE) % Constants.WORLD_CHUNK_SIZE);
	}
	
	/**
	 * Called when the position of a player changes.
	 * @param player The player that has changed positions.
	 */
	public void onPlayerMove(Player player, boolean hasChangedChunks) {
		// The player may have moved into a different chunk. If this has happened then
		// we may might need to load some chunks that have not already been cached.
		if (hasChangedChunks) {
			this.getChunks().onPlayerChunkVisit(player.getPosition().getChunkX(), player.getPosition().getChunkY());
		}
		// TODO Get the direction that the player has moved in.
		// TODO For each position in the row of tiles that we have moved towards at the edge of the player view distance:
		//    - Check whether the position differs from what the player thinks is there.
		// Add a placement loaded message for the moving player for each one.
	}
	
	/**
	 * Called when a player spawns at a position.
	 * @param player The player that has spawned.
	 */
	public void onPlayerSpawn(Player player) {
		// Get the player position.
		Position playerPosition = player.getPosition();
		// The player has spawned into a chunk. We may might need to load some chunks that have not already been cached.
		this.getChunks().onPlayerChunkVisit(playerPosition.getChunkX(), playerPosition.getChunkY());
		// We will have to sent placement updates for every position in the view distance of the player.
		for (int x = playerPosition.getX() - Constants.PLAYER_VIEW_DISTANCE; x < playerPosition.getX() + Constants.PLAYER_VIEW_DISTANCE; x++ ) {
			for (int y = playerPosition.getY() - Constants.PLAYER_VIEW_DISTANCE; y < playerPosition.getY() + Constants.PLAYER_VIEW_DISTANCE; y++ ) {
				// Do nothing if this is not a valid position.
				if (!Position.isValid(x, y)) {
					continue;
				}
				// Get the placement at this position, or null if there is no placement.
				Placement placement = chunks.getPlacement(x, y);
				// If there is a placement at this position then create a placement loaded message for the spawning player.
				if (placement != null) {
					worldMessageQueue.add(new PlacementLoadedMessage(player.getPlayerId(), placement, new Position(x, y)));
				}
			}
		}
	}
	
	/**
	 * Called when a placement changes at a position.
	 * @param placement The placement that has changed.
	 * @param position The position of the changed placmement.
	 */
	public void onPlacementChange(Placement placement, Position position) {
		// TODO Get players that care!
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
		// Set the server version.
		worldState.put("version", Constants.VERSION);
		// Set the world seed.
		worldState.put("seed", this.tileGenerator.getSeed());
		// Set the spawn position.
		worldState.put("spawn", Position.serialise(this.playerSpawn));
		// Set the world time.
		worldState.put("time", this.clock.getCurrentTime().getState());
		// Return the world state.
		return worldState;
	}
}
