package gaia.server.engine;

import gaia.server.world.World;
import gaia.server.world.messaging.WorldMessageQueue;
import gaia.time.Time;

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
	private IWorldEventsHandler worldEventsHandler;
	/**
	 * The request queue holding requests to be processed sequentially.
	 */
	private RequestQueue requestQueue = new RequestQueue();
	
	/**
	 * Create a new instance of the Engine class.
	 * @param world The game world.
	 */
	public Engine(World world) {
		this.world = world;
	}
	
	/**
	 * Get the request queue.
	 * @return The request queue.
	 */
	public RequestQueue getRequestQueue() {
		return this.requestQueue;
	}
	
	/**
	 * Set the world events handler.
	 * @param worldEventsHandler The world events handler.
	 */
	public void setWorldEventsHandler(IWorldEventsHandler worldEventsHandler) {
		this.worldEventsHandler = worldEventsHandler;
	}
	
	/**
	 * Tick the game engine.
	 */
	public void tick() {
		// Process any requests.
		processRequests();
		// Update the world time and get whether it has changed.
		// It does not change every server tick, just ever game minute.
		boolean timeChanged = this.world.getClock().update();
		// Get the current time.
		Time currentTime = this.world.getClock().getCurrentTime();
		// Tick the world.
		this.world.tick(currentTime, timeChanged);
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
	 * Process the world messages in the world message queue.
	 * World messages in the queue will only be processed if a world event handler has been set.
	 */
	private void processWorldMessages() {
		// We cannot do anything if a world message processor has not been set.
		if (this.worldEventsHandler == null) {
			return;
		}
		// Grab the world message queue from the world.
		WorldMessageQueue worldMessageQueue = this.world.getWorldMessageQueue();
		// Process all messages in the queue.
		while(worldMessageQueue.hasNext()) {
			// Process the next world message, potentially invoking any relevant world event handlers.
			worldMessageQueue.next().process(this.worldEventsHandler);
		}
	}
}
