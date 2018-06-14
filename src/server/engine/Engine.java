package server.engine;

import server.world.World;
import server.world.chunk.Chunk;
import server.world.players.PlayerRequestQueue;
import server.world.players.requests.PlayerRequest;

/**
 * The server-side game engine.
 */
public class Engine {
	/**
	 * The game world.
	 */
	private World world;
	/**
	 * The player request queue.
	 * Holds requests for all players, to be processed sequentially.
	 */
	private PlayerRequestQueue playerRequestQueue = new PlayerRequestQueue();
	
	/**
	 * Create a new instance of the Engine class.
	 * @param world The game world.
	 * @param playerRequestQueue The player request queue.
	 */
	public Engine(World world) {
		this.world = world;
	}
	
	/**
	 * Add a player request to a queue to be processed.
	 * @param request The request to add.
	 */
	public void addPlayerRequest(PlayerRequest request) {
		this.playerRequestQueue.add(request);
	}
	
	/**
	 * Tick the game engine.
	 */
	public void tick() {
		// Process any player requests.
		processPlayerRequests();
		// Update the world time and get whether it has changed.
		// It does not change every server tick, just ever game minute.
		boolean timeChanged = this.world.getTime().update();
		// Tick each of our cached chunks.
		for (Chunk chunk : world.getCachedChunks()) {
			// Are any players within the vicinity of this chunk?
			boolean arePlayersNearChunk = this.world.arePlayersInChunkVicinity(chunk);
			// We only want to tick chunks that are active. An active chunk either:
			// - Contains a high priority placement.
			// - Has any players in the vicinity.
			if (arePlayersNearChunk || chunk.hasHighPriorityPlacement()) {
				chunk.tick(timeChanged, this.world.getTime(), arePlayersNearChunk, this.world.getWorldMessageQueue());
			}
		}
	}

	/**
	 * Process all player requests in the player request queue.
	 */
	private void processPlayerRequests() {
		// Process all player requests in the player request queue.
		while(this.playerRequestQueue.hasNext()) {
			// Grab the next player request.
			PlayerRequest request = this.playerRequestQueue.next();
			// Attempt to satisfy the request.
			request.satisfy(this.world);
		}
	}
}
