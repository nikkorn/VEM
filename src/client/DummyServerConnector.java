package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

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
			// Create the socket on which to connect to the server.
			Socket connectionsocket = new Socket("localhost", 23445);
			// Create a printwriter to write the handshake over the socket.
			new PrintWriter(connectionsocket.getOutputStream(), true).println("Hello Good Friend!");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}