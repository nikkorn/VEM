package server.networking;

import server.world.players.ConnectedPlayers;
import server.world.players.PlayerRequestQueue;

/**
 * Manages connected clients.
 */
public class ConnectedClientManager {
    /**
     * The list of connected clients represented as players.
     */
    private ConnectedPlayers connectedPlayers = new ConnectedPlayers();
    /**
     * The player request queue.
     */
    private PlayerRequestQueue playerRequestQueue = new PlayerRequestQueue();

    /**
     * Get the player request queue.
     * @return The player request queue.
     */
    public PlayerRequestQueue getPlayerRequestQueue() {
        return playerRequestQueue;
    }

    /**
     * Get the list of connected clients represented as players.
     * @return The list of connected clients represented as players.
     */
    public ConnectedPlayers getConnectedPlayers() {
        return connectedPlayers;
    }
}
