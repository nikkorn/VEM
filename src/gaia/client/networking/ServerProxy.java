package gaia.client.networking;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import gaia.networking.ClientServerMessageMarshallerProviderFactory;
import gaia.networking.IMessage;
import gaia.networking.MessageInputStream;
import gaia.networking.MessageMarshallerProvider;
import gaia.networking.MessageOutputStream;
import gaia.networking.MessageQueue;
import gaia.networking.QueuedMessageReader;
import gaia.networking.messages.Handshake;
import gaia.networking.messages.JoinFailure;

/**
 * A client-side representation of a server instance.
 */
public class ServerProxy {
	/**
	 * The message reader used to read messages from a message input stream into a queue.
	 */
	private QueuedMessageReader queuedMessageReader;
	/**
	 * The message output stream used to write messages to the server.
	 */
	private MessageOutputStream messageOutputStream;
	
	/**
	 * Create a new instance of the ServerProxy class.
	 * @param queuedMessageReader The message reader used to read messages from a message input stream into a queue. 
	 * @param messageOutputStream The message output stream used to write messages to the server.
	 */
	private ServerProxy(QueuedMessageReader queuedMessageReader, MessageOutputStream messageOutputStream) {
		this.queuedMessageReader = queuedMessageReader;
		this.messageOutputStream = messageOutputStream;
	}
	
	/**
	 * Get a queue of any messages received from the server.
	 * @return A queue of any messages received from the server.
	 */
	public MessageQueue getReceivedMessageQueue() {
		return this.queuedMessageReader.getQueuedMessages();
	}
	
	/**
	 * Send a message to the server.
	 * @param message The message to send.
	 * @throws IOException 
	 */
	public void sendMessage(IMessage message) throws IOException {
		this.messageOutputStream.writeMessage(message);
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
	 * @throws UnknownHostException 
	 * @throws ServerJoinRequestRejectedException 
	 */
	public static ServerProxy create(String host, int port, String playerId) throws UnknownHostException, IOException, ServerJoinRequestRejectedException  {
		// Create the socket on which to connect to the server.
		Socket connectionsocket = new Socket(host, port);
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
				// Firstly, create the queued message reader that will be used by the server proxy.
				QueuedMessageReader queuedMessageReader = new QueuedMessageReader(messageInputStream);
				// Next, create the server proxy instance.
				ServerProxy serverProxy = new ServerProxy(queuedMessageReader, messageOutputStream);
				// Lastly, our queued message reader needs to start reading incoming messages.
				Thread messageReaderThread = new Thread(queuedMessageReader);
				messageReaderThread.setDaemon(true);
				messageReaderThread.start();
				// We are finished, return the successfully created server proxy.
				return serverProxy;
			case 2:
				// The server sent us a message to let us know we failed to join!
				throw new ServerJoinRequestRejectedException(((JoinFailure)response).getReason());
			default:
				throw new RuntimeException("Received unexpected response from server.");
		}
	}
}
