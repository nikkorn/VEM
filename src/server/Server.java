package server;

import server.engine.Engine;
import server.players.ConnectedPlayerPool;
import server.world.World;
import server.world.WorldFactory;

/**
 * The game server.
 */
public class Server {
	/**
	 * The server configuration.
	 */
	private Configuration configuration;
	/**
	 * The game engine.
	 */
	private Engine engine;
	
	/**
	 * Create a new instance of the server class.
	 * @param worldName The world name.
	 */
	public Server(String worldName) {
		// Read the server configuration from disk.
		this.configuration = Configuration.loadFromDisk();
		// Create the game engine.
		this.engine = createGameEngine(worldName);
	}
	
	/**
	 * Program entry point.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		// The first command line argument should be the world name.
		String worldName = args[0];
		// Create a server clock and pass it a new instance of Server.
		ServerClock serverClock = new ServerClock(new Server(worldName));
		// Start the server clock. 
		serverClock.start();
	}
	
	/**
	 * The server loop.
	 * This is called at a consistent rate by the server clock.
	 */
	public void loop() {
		// TODO Handle new player connections.
		// TODO Handle player disconnects.
		
		// Tick the game engine.
		engine.tick();
		
		// TODO Broadcast changes to connected players who care about them.
		// TODO Check for whether to save world state to disk.
	}
	
	/**
	 * Create the game engine.
	 * @param worldName The world name.
	 * @return The game engine.
	 */
	public Engine createGameEngine(String worldName) {
		// Create the world.
		World world = WorldFactory.createWorld(worldName);
		// Create the connected player pool.
		ConnectedPlayerPool connectedPlayerPool = new ConnectedPlayerPool();
		// Create and return the game engine.
		return new Engine(world, connectedPlayerPool);
	}
}
