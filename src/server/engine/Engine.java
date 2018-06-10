package server.engine;

import server.world.players.ConnectedPlayers;
import server.world.Position;
import server.world.World;
import server.world.chunk.Chunk;
import server.world.players.PlayerRequestQueue;

/**
 * The server-side game engine.
 */
public class Engine {
	/**
	 * The game world.
	 */
	private World world;
	/**
	 * The connected players.
	 * This list holds a representation of the players in the game world.
	 */
	private ConnectedPlayers connectedPlayers;
	/**
	 * The player request queue.
	 * Holds requests for all players, to be processed sequentially.
	 */
	private PlayerRequestQueue playerRequestQueue;
	
	/**
	 * Create a new instance of the Engine class.
	 * @param world The game world.
	 * @param connectedPlayers The connected players.
	 * @param playerRequestQueue The player request queue.
	 */
	public Engine(World world, ConnectedPlayers connectedPlayers, PlayerRequestQueue playerRequestQueue) {
		this.world              = world;
		this.connectedPlayers   = connectedPlayers;
		this.playerRequestQueue = playerRequestQueue;
	}
	
	/**
	 * Tick the game engine.
	 */
	public void tick() {
		// Process newly connected players.
		processNewlyConnectedPlayers();
		// Process any player requests.
		processPlayerRequests();
		// Update the world time and get whether it has changed.
		// It does not change every server tick, just ever game minute.
		boolean timeChanged = this.world.getTime().update();
		// Tick each of our cached chunks.
		for (Chunk chunk : world.getCachedChunks()) {
			// Are any players within the vicinity of this chunk?
			boolean arePlayersNearChunk = arePlayersInChunkVicinity(chunk);
			// We only want to tick chunks that are active. An active chunk either:
			// - Contains a high priority placement.
			// - Has any players in the vicinity.
			if (arePlayersNearChunk || chunk.hasHighPriorityPlacement()) {
				chunk.tick(timeChanged, this.world.getTime(), arePlayersNearChunk);
			}
		}
	}

	/**
	 * Process any newly connected players.
	 * These are players that have been added as a connected player but don't have things like a position of inventory.
	 */
	private void processNewlyConnectedPlayers() {
		// TODO Process any newly connected players.
	}

	/**
	 * Process all player requests in the player request queue.
	 */
	private void processPlayerRequests() {
		// TODO Process all player requests in the player request queue.
	}

	/**
	 * Checks whether any connected players are within the vicinity of the specified chunk.
	 * @param chunk The chunk.
	 * @return Whether any connected players are within the vicinity of the specified chunk.
	 */
	private boolean arePlayersInChunkVicinity(Chunk chunk) {
		// Check the position of each connected player.
		for (Position playerPosition : connectedPlayers.getPlayerPositions()) {
			// Is the currently connected player in the vicinity of this chunk?
			if (chunk.isPositionInVicinity(playerPosition)) {
				return true;
			}
		}
		// No connected players were in the vicinity of this chunk.
		return false;
	}
}
