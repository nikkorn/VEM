package server.networking;

/**
 * Handler for client manager events.
 */
public interface IClientEventHandlers {
	
	/**
	 * Called in response to a client connecting.
	 * @param clientId The id of the connecting client.
	 */
	void onConnect(String clientId);
	
	/**
	 * Called in response to a client disconnecting.
	 * @param clientId The id of the disconnecting client.
	 */
	void onDisconnect(String clientId);
}
