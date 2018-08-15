package gaia.server.engine;

import gaia.server.world.World;

/**
 * Represents a queue-able request to be processed by the engine. 
 */
public abstract class Request {
	
	/**
	 * Satisfy the request.
	 * @param world The world to interact with in order to satisfy the request.
	 */
	public abstract void satisfy(World world);
}
