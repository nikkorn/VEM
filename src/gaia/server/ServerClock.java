package gaia.server;

import gaia.Constants;

/**
 * The server clock which calls the server loop at a consistent rate.
 */
public class ServerClock {
	/**
	 * The server.
	 */
	private Server server;
	
	/**
	 * Create a new instance of the ServerClock class.
	 * @param server The server.
	 */
	public ServerClock(Server server) {
		this.server = server;
	}
	
	/**
	 * Start the server clock, looping the server at the server clock rate.
	 */
	public void start() {
		// The last time that we called the server loop.
		long lastLoopCall = 0;
		// Start the server clock. 
		while (true) {
			// Get the current system time.
			long currentTimeMillis = System.currentTimeMillis();
			// We only want to loop the server if we have waited long enough to do so.
			if (currentTimeMillis >= (lastLoopCall + Constants.SERVER_CLOCK_RATE)) {
				// Call the server loop.
				server.loop();
				// Update the last loop call time.
				lastLoopCall = currentTimeMillis;
			}
		}
	}
}
