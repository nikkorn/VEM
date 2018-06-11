package server.world.players;

import java.util.ArrayList;

/**
 * A queue of requests populated by players.
 */
public class PlayerRequestQueue {
    /**
     * The list of requests.
     */
    private ArrayList<PlayerRequest> requests = new ArrayList<PlayerRequest>();

    /**
     * Get whether the request queue contains a request.
     * @return Whether the request queue contains a request.
     */
    public boolean hasNext() {
        return this.requests.size() > 0;
    }

    /**
     * Gets the next player request from the queue.
     * @return The next player request from the queue.
     */
    public PlayerRequest next() {
        return this.requests.remove(0);
    }

    /**
     * Add a player request to the end of the queue.
     * @param request The request to add.
     */
    public void add(PlayerRequest request) {
        this.requests.add(request);
    }
}