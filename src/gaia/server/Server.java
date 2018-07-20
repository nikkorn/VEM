package gaia.server;

import gaia.server.engine.Engine;
import gaia.server.engine.RequestQueue;
import gaia.server.networking.ClientWorldMessageProcessor;
import gaia.server.networking.ConnectedClientManager;
import gaia.server.world.World;
import gaia.server.world.WorldFactory;

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
	 * The connected client manager.
	 */
	private ConnectedClientManager connectedClientManager;
	
	/**
	 * Create a new instance of the server class.
	 * @param worldName The world name.
	 */
	public Server(String worldName) {
		// Read the server configuration from disk.
		this.configuration = Configuration.loadFromDisk();
		// Set whether we are going to output debug info to the console.
		ServerConsole.setDebugToConsole(configuration.isDebuggingToConsole());
		// Create the engine request queue.
		RequestQueue requestQueue = new RequestQueue();
		// Create the connected client manager, passing the engine request queue and the port for client connections.
		this.connectedClientManager = new ConnectedClientManager(this.configuration.getPort(), requestQueue);
		// Create the game engine.
		this.engine = createGameEngine(worldName, requestQueue);
		// Now that everything is set up we should start listening for client connection requests.
		this.connectedClientManager.startListeningForConnections();
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
	 * @param requestQueue The engine request queue.
	 * @return The game engine.
	 */
	private Engine createGameEngine(String worldName, RequestQueue requestQueue) {
		// Create the world.
		World world = WorldFactory.createWorld(worldName);
		// Create the game engine, passing the processor used to handle world messages for clients as well as the request queue.
		Engine engine = new Engine(world, new ClientWorldMessageProcessor(this.connectedClientManager), requestQueue);
		// Return the game engine.
		return engine;
	}
}
