package server.engine;

import server.players.ConnectedPlayerPool;
import server.world.World;

/**
 * The server-side game engine.
 */
public class Engine {
	/**
	 * The game world.
	 */
	private World world;
	/**
	 * The pool of connected players.
	 */
	private ConnectedPlayerPool connectedPlayerPool;
	
	/**
	 * Create a new instance of the Engine class.
	 * @param world The game world.
	 * @param connectedPlayerPool The pool of connected players.
	 */
	public Engine(World world, ConnectedPlayerPool connectedPlayerPool) {
		this.world               = world;
		this.connectedPlayerPool = connectedPlayerPool;
	}
	
	/**
	 * Tick the game engine.
	 */
	public void tick() {
		// Update the world time and get whether it has changed.
		// It does not change every server tick, just ever game minute.
		boolean timeChanged = this.world.getTime().update();
		// ....
		if (timeChanged) {
			System.out.println(this.world.getTime().getFormattedTime());
		}
	}
}
