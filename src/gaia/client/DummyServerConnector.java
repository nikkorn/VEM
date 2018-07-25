package gaia.client;

import java.io.IOException;
import java.net.UnknownHostException;
import gaia.client.networking.ServerJoinRequestRejectedException;
import gaia.client.networking.ServerProxy;

/**
 * Test for client -> Server handshake.
 */
public class DummyServerConnector {
	
	/**
	 * Program entry point.
	 * @param args The command line args.
	 */
	public static void main(String[] args) {
		try {
			// Create a connection with the server.
			ServerProxy.create("localhost", 23445, "Silly Billy");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServerJoinRequestRejectedException e) {
			e.printStackTrace();
		}
	}
}