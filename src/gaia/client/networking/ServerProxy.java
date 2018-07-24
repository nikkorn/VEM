package gaia.client.networking;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import gaia.networking.ClientServerMessageMarshallerProviderFactory;
import gaia.networking.IMessage;
import gaia.networking.MessageInputStream;
import gaia.networking.MessageMarshallerProvider;
import gaia.networking.MessageOutputStream;
import gaia.networking.messages.Handshake;
import gaia.networking.messages.JoinFailure;

/**
 * A client-side representation of a server instance.
 */
public class ServerProxy {
	
	/**
	 * Create a new instance of the ServerProxy class.
	 * @param socket The socket.
	 * @param in The message input stream used to read messages from the server. 
	 * @param out The message output stream used to write messages to the server.
	 */
	private ServerProxy(Socket socket, MessageInputStream in, MessageOutputStream out) {}
	
	/**
	 * Connect to a remote server instance and return a server proxy that represents that server instance.
	 * @param host The host on which the server is running.
	 * @param port The port on which the server is listening for client connections.
	 * @param playerId The player id.
	 * @return A server proxy that represents that server instance.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws ServerJoinRequestRejectedException 
	 */
	public ServerProxy create(String host, int port, String playerId) throws UnknownHostException, IOException, ServerJoinRequestRejectedException  {
		// Create the socket on which to connect to the server.
		Socket connectionsocket = new Socket("localhost", 23445);
		// Create the message marshaller provider for our message stream.
		MessageMarshallerProvider marshallerProvider = ClientServerMessageMarshallerProviderFactory.create();
		// Create the message output stream used to write messages to the server.
		MessageOutputStream messageOutputStream = new MessageOutputStream(connectionsocket.getOutputStream(), marshallerProvider);
		// Send a handshake!
		messageOutputStream.writeMessage(new Handshake(playerId));
		// Wait for the response form the server. We are expecting either a join success or failure.
		// Firstly, we need to create our message input stream in order to grab the server response.
		MessageInputStream messageInputStream = new MessageInputStream(connectionsocket.getInputStream(), marshallerProvider);
		// Read the server response message. This operation blocks until we get it.
		IMessage response = messageInputStream.readMessage();
		// We got a response from the server!
		switch (response.getTypeId()) {
			case 1:
				// The server sent us a message to let us know we successfully joined!
				return new ServerProxy(connectionsocket, messageInputStream, messageOutputStream);
			case 2:
				// The server sent us a message to let us know we failed to join!
				throw new ServerJoinRequestRejectedException(((JoinFailure)response).getReason());
			default:
				throw new RuntimeException("Received unexpected response from server.");
		}
	}
}
