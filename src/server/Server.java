package server;

/**
 * The game server.
 */
public class Server 
{
	/**
	 * Program entry point.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		// Create a server clock and pass it a new instance of Server.
		ServerClock serverClock = new ServerClock(new Server());
		// Start the server clock. 
		serverClock.start();
	}
	
	/**
	 * The server loop.
	 * This is called at a consistent rate by the server clock.
	 */
	public void loop() {
		System.out.println("LOOP");
	}
}
