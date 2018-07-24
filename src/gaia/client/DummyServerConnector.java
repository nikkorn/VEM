package gaia.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import gaia.networking.ClientServerMessageMarshallerProviderFactory;
import gaia.networking.MessageMarshallerProvider;
import gaia.networking.MessageOutputStream;
import gaia.networking.messages.Handshake;

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
			// Create the message marshaller provider for our message stream.
			MessageMarshallerProvider marshallerProvider = ClientServerMessageMarshallerProviderFactory.create();
			// Create the message output stream used to write messages to the server.
			MessageOutputStream messageOutputStream = new MessageOutputStream(connectionsocket.getOutputStream(), marshallerProvider);
			// Send a handshake!
			messageOutputStream.writeMessage(new Handshake("Silly Billy"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}