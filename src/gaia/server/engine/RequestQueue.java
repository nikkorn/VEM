package gaia.server.engine;

import java.util.ArrayList;

/**
 * A queue of requests to be processed by the engine.
 */
public class RequestQueue {
	/**
     * The list of requests to be satisfied.
     */
    private ArrayList<Request> requests = new ArrayList<Request>();

    /**
     * Get whether the request queue contains a request.
     * @return Whether the request queue contains a request.
     */
    public boolean hasNext() {
        return this.requests.size() > 0;
    }

    /**
     * Gets the next request from the queue.
     * @return The next request from the queue.
     */
    public Request next() {
        return this.requests.remove(0);
    }

    /**
     * Add a request to the end of the queue.
     * @param request The request to add.
     */
    public void add(Request request) {
        this.requests.add(request);
    }
}
