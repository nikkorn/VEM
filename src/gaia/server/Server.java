package gaia.server;

import gaia.Constants;
import gaia.server.engine.Engine;
import gaia.server.engine.Request;
import gaia.server.networking.ClientWorldMessageProcessor;
import gaia.server.networking.ClientMessage;
import gaia.server.networking.ClientMessageQueue;
import gaia.server.networking.ClientProxyManager;
import gaia.server.world.World;
import gaia.server.world.WorldFactory;
import gaia.server.world.players.requests.LeaveRequest;

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
	private ClientProxyManager clientProxyManager;
	
	/**
	 * Create a new instance of the server class.
	 * @param worldName The world name.
	 */
	public Server(String worldName) {
		System.out.println("###################### Gaia Server #######################");
		System.out.println("##########################################################");
		System.out.println("version: " + Constants.VERSION);
		System.out.println("##########################################################");
		// Read the server configuration from disk.
		this.configuration = Configuration.loadFromDisk();
		// Set whether we are going to output debug info to the console.
		ServerConsole.setDebugToConsole(configuration.isDebuggingToConsole());
		// Create the world.
		World world = WorldFactory.createWorld(worldName);
		// Create the game engine.
		this.engine = new Engine(world);
		// Create the client proxy manager, passing  the port for client connections
		// and the processor provided by the engine for making synchronized join requests.
		this.clientProxyManager = new ClientProxyManager(this.configuration.getPort(), this.engine.getJoinRequestProcessor());
		// In order to be able to process messages output by the engine we need to specify a world message processor.
		this.engine.setWorldMessageProcessor(new ClientWorldMessageProcessor(this.clientProxyManager));
		// Now that everything is set up we should start listening for client connection requests.
		this.clientProxyManager.startListeningForConnections();
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
		// Handle any client disconnections.
		handleClientDisconnections();
		// Process any messages received from the connected clients.
		processReceivedClientMessages();
		// Tick the game engine.
		engine.tick();
		
		// TODO Check for whether to save world state to disk.
	}
	
	/**
	 * Handle client disconnections.
	 */
	private void handleClientDisconnections() {
		// Ask the client proxy manager to check for any disconnections, in
		// return we will get a list of the player ids of any disconnected clients.
		for (String disconnectedPlayerId : this.clientProxyManager.processDisconnections()) {
			// We will have to request that the player be removed from the game world.
			engine.getRequestQueue().add(new LeaveRequest(disconnectedPlayerId));
		}
	}

	/**
	 * Process any messages received from the connected clients.
	 */
	private void processReceivedClientMessages() {
		// Get any messages sent to the server from any connected clients.
		ClientMessageQueue receivedMessages = this.clientProxyManager.getReceivedMessageQueue();
		// Convert the client messages into engine requests and queue them up to be processed.
		while (receivedMessages.hasNext()) {
			// Convert the client message into an engine request.
			Request request = ClientMessage.toEngineRequest(receivedMessages.next());
			// Queue up the request to be processed by the engine later on.
			engine.getRequestQueue().add(request);
		}
	}
}
