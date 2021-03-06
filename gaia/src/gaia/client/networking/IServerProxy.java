package gaia.client.networking;

import gaia.client.gamestate.IServerState;

/**
 * Represents a server proxy.
 */
public interface IServerProxy {

	/**
	 * Get a snapshot of the server state.
	 * @return A snapshot of the server state.
     */
	IServerState getServerState();
	
	/**
	 * Get the actions that can be taken by the player.
	 * @return The actions that can be taken by the player.
     */
	PlayerActions getPlayerActions();
	
	/**
	 * Get whether we are still connected with the server.
	 * @return Whether we are still connected with the server.
	 */
	boolean isConnected();
}
