package gaia.client.networking;

import java.io.IOException;
import java.net.Socket;
import gaia.client.gamestate.ServerState;
import gaia.networking.ClientServerMessageMarshallerProviderFactory;
import gaia.networking.IMessage;
import gaia.networking.MessageInputStream;
import gaia.networking.MessageMarshallerProvider;
import gaia.networking.MessageOutputStream;
import gaia.networking.QueuedMessageReader;
import gaia.networking.messages.Handshake;
import gaia.networking.messages.JoinFailure;
import gaia.networking.messages.JoinSuccess;
import gaia.networking.messages.MessageIdentifier;

/**
 * A client-side representation of a server instance.
 */
public class ServerProxy {
	/**
	 * The message reader used to read messages from a message input stream into a queue.
	 */
	private QueuedMessageReader queuedMessageReader;
	/**
	 * The actions that can be taken by the player.
	 */
	private PlayerActions playerActions;
	/**
	 * The client-side representation of the server state.
	 */
	private ServerState serverState;
	
	/**
	 * Create a new instance of the ServerProxy class.
	 * @param playerId The player id.
	 * @param queuedMessageReader The message reader used to read messages from a message input stream into a queue. 
	 * @param messageOutputStream The message output stream used to write messages to the server.
	 * @param worldSeed The world seed.
	 */
	private ServerProxy(String playerId, final QueuedMessageReader queuedMessageReader, MessageOutputStream messageOutputStream, long worldSeed) {
		this.queuedMessageReader = queuedMessageReader;
		this.serverState         = new ServerState(playerId, queuedMessageReader, worldSeed);
		this.playerActions       = new PlayerActions(messageOutputStream, serverState);
	}

	/**
	 * Get a snapshot of the server state.
	 * @return A snapshot of the server state.
     */
	public ServerState getServerState() {
		return serverState;
	}

	/**
	 * Get the actions that can be taken by the player.
	 * @return The actions that can be taken by the player.
     */
	public PlayerActions getPlayerActions() {
		return this.playerActions;
	}
	
	/**
	 * Get whether we are still connected with the server.
	 * @return Whether we are still connected with the server.
	 */
	public boolean isConnected() {
		// For now, we will check whether we are still connected by checking if our reader is still connected.
		return this.queuedMessageReader.isConnected();
	}
	
	/**
	 * Connect to a remote server instance and return a server proxy that represents that server instance.
	 * @param host The host on which the server is running.
	 * @param port The port on which the server is listening for client connections.
	 * @param playerId The player id.
	 * @return A server proxy that represents that server instance.
	 * @throws IOException
	 * @throws ServerJoinRequestRejectedException 
	 */
	public static ServerProxy create(String host, int port, String playerId) throws  IOException, ServerJoinRequestRejectedException  {
		// Create the socket on which to connect to the server.
		Socket connectionSocket = new Socket(host, port);
		// Create the message marshaller provider for our message stream.
		MessageMarshallerProvider marshallerProvider = ClientServerMessageMarshallerProviderFactory.create();
		// Create the message output stream used to write messages to the server.
		MessageOutputStream messageOutputStream = new MessageOutputStream(connectionSocket.getOutputStream(), marshallerProvider);
		// Wait for the response form the server. We are expecting either a join success or failure.
		// Firstly, we need to create our message input stream in order to grab the server response.
		MessageInputStream messageInputStream = new MessageInputStream(connectionSocket.getInputStream(), marshallerProvider);
		// Send a handshake!
		messageOutputStream.writeMessage(new Handshake(playerId));
		// Read the server response message. This operation blocks until we get it.
		IMessage response = messageInputStream.readMessage();
		// We got a response from the server!
		switch (response.getTypeId()) {
			case MessageIdentifier.JOIN_SUCCESS:
				// The server sent us a message to let us know we successfully joined!
				JoinSuccess joinSuccessMessage = (JoinSuccess)response;
				// Firstly, create the queued message reader that will be used by the server proxy.
				QueuedMessageReader queuedMessageReader = new QueuedMessageReader(messageInputStream);
				// Next, create the server proxy instance.
				ServerProxy serverProxy = new ServerProxy(playerId, queuedMessageReader, messageOutputStream, joinSuccessMessage.getWorldSeed());
				// Lastly, our queued message reader needs to start reading incoming messages.
				Thread messageReaderThread = new Thread(queuedMessageReader);
				messageReaderThread.setDaemon(true);
				messageReaderThread.start();
				// We are finished, return the successfully created server proxy.
				return serverProxy;
			case MessageIdentifier.JOIN_FAIL:
				// The server sent us a message to let us know we failed to join!
				throw new ServerJoinRequestRejectedException(((JoinFailure)response).getReason());
			default:
				throw new RuntimeException("Received unexpected response from server.");
		}
	}
}
