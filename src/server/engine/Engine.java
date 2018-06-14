package server.engine;

import server.world.World;
import server.world.chunk.Chunk;
import server.world.messaging.IWorldMessageProcessor;
import server.world.messaging.WorldMessageQueue;
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
	 * The world message processor.
	 */
	private IWorldMessageProcessor worldMessageProcessor;
	/**
	 * The player request queue.
	 * Holds requests for all players, to be processed sequentially.
	 */
	private PlayerRequestQueue playerRequestQueue = new PlayerRequestQueue();
	
	/**
	 * Create a new instance of the Engine class.
	 * @param world The game world.
	 * @param worldMessageProcessor The world message processor.
	 */
	public Engine(World world, IWorldMessageProcessor worldMessageProcessor) {
		this.world                 = world;
		this.worldMessageProcessor = worldMessageProcessor;
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
		// Process any messages that were added to the world message queue as part of this engine tick.
		processWorldMessages();
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
	
	/**
	 * Process the world messages in the wolrd message queue.
	 */
	private void processWorldMessages() {
		// Grab the world message queue from the world.
		WorldMessageQueue worldMessageQueue = this.world.getWorldMessageQueue();
		// Process all messages in the queue.
		while(worldMessageQueue.hasNext()) {
			// It is the responsibility of the world message processor to handle this message.
			this.worldMessageProcessor.process(worldMessageQueue.next());
		}
	}
}
