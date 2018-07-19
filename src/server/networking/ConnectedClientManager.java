package server.networking;

import java.util.ArrayList;
import server.engine.RequestQueue;

/**
 * Manages connected clients.
 */
public class ConnectedClientManager {
	/**
	 * The clients.
	 */
	private ArrayList<Client> clients = new ArrayList<Client>();
	/**
	 * The client event handlers.
	 */
	private IClientEventHandlers clientEventHandlers;
	/**
	 * The queue of requests to be processed by the engine.
	 */
	private RequestQueue requestQueue;
	
	/**
	 * Creates a new instance of the ConnectedClientManager class.
	 * @param clientEventHandlers The client event handlers.
	 * @param requestQueue The queue of requests to be processed by the engine.
	 */
	public ConnectedClientManager(IClientEventHandlers clientEventHandlers, RequestQueue requestQueue) {
		this.clientEventHandlers = clientEventHandlers;
		this.requestQueue        = requestQueue;
	}
	
	/**
	 * Process any requests that connected clients have made.
	 */
	public void processRequestsFromClients() {
		// TODO Process any requests that connected clients have made.
	}
	
	/**
	 * Listen for new client connections.
	 * This executes in an independent thread.
	 */
	public void listen() {
		// TODO Sit and wait for new client connections.
	}
}
