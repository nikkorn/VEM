package gaia.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import gaia.client.networking.writers.HandshakeWriter;
import gaia.networking.MessageOutputStream;
import gaia.networking.MessageWriterProvider;
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
			// Create the message output stream used to write messages to the server.
			MessageOutputStream messageOutputStream = new MessageOutputStream(connectionsocket.getOutputStream(), createClientMessageWriterProvider());
			// Send a handshake!
			messageOutputStream.writeMessage(new Handshake("Billy Bob"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create and populate the message writer provider that will be used to write messages to the server.
	 * @return The message writer provider that will be used to write messages to the server.
	 */
	private static MessageWriterProvider createClientMessageWriterProvider() {
		// Create the writer provider.
		MessageWriterProvider writerProvider = new MessageWriterProvider();
		// Add the writer.
		writerProvider.addWriter(new HandshakeWriter());
		// Return the writer provider.
		return writerProvider;
	}
}