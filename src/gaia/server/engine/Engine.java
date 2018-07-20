package gaia.server.engine;

import gaia.server.world.World;
import gaia.server.world.chunk.Chunk;
import gaia.server.world.messaging.IWorldMessageProcessor;
import gaia.server.world.messaging.WorldMessageQueue;

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
	 * The request queue holding requests to be processed sequentially.
	 */
	private RequestQueue requestQueue;
	
	/**
	 * Create a new instance of the Engine class.
	 * @param world The game world.
	 * @param worldMessageProcessor The world message processor.
	 * @param requestQueue The request queue.
	 */
	public Engine(World world, IWorldMessageProcessor worldMessageProcessor, RequestQueue requestQueue) {
		this.world                 = world;
		this.worldMessageProcessor = worldMessageProcessor;
		this.requestQueue          = requestQueue;
	}
	
	/**
	 * Tick the game engine.
	 */
	public void tick() {
		// Process any requests.
		processRequests();
		// Update the world time and get whether it has changed.
		// It does not change every server tick, just ever game minute.
		boolean timeChanged = this.world.getTime().update();
		// Tick each of our cached chunks.
		for (Chunk chunk : world.getChunks().getCachedChunks()) {
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
	 * Process all requests in the request queue.
	 */
	private void processRequests() {
		// Process all player requests in the player request queue.
		while(this.requestQueue.hasNext()) {
			// Grab the next request.
			Request request = this.requestQueue.next();
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
