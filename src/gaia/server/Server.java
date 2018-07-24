package gaia.server;

import gaia.server.engine.Engine;
import gaia.server.engine.RequestQueue;
import gaia.server.networking.ClientWorldMessageProcessor;
import gaia.server.networking.ClientProxyManager;
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
	private ClientProxyManager connectedClientManager;
	
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
		// Create the world.
		World world = WorldFactory.createWorld(worldName);
		// Create the game engine.
		this.engine = new Engine(world, requestQueue);
		// Create the connected client manager, passing the engine request queue and the port for client connections.
		// We also pass the processor provided by the engine for making synchronized join requests.
		this.connectedClientManager = new ClientProxyManager(this.configuration.getPort(), requestQueue, this.engine.getJoinRequestProcessor());
		// In order to be able to process messages output by the engine we need to specify a world message processor.
		this.engine.setWorldMessageProcessor(new ClientWorldMessageProcessor(this.connectedClientManager));
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
		// TODO Handle player disconnects.
		
		// Tick the game engine.
		engine.tick();
		
		// TODO Broadcast changes to connected players who care about them.
		// TODO Check for whether to save world state to disk.
	}
}
