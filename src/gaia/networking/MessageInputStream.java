package gaia.networking;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Reads messages from a data stream. 
 */
public class MessageInputStream extends DataInputStream {
	/**
	 * The message factory provider.
	 */
	private MessageFactoryProvider messageFactoryProvider;

	/**
	 * Create a new instance of the MessageInputStream class.
	 * @param inputStream The input stream from which to read messages.
	 * @param messageFactoryProvider The provider of message factories.
	 */
	public MessageInputStream(InputStream inputStream, MessageFactoryProvider messageFactoryProvider) {
		super(inputStream);
		this.messageFactoryProvider = messageFactoryProvider;
	}
	
	/**
	 * Read the next message from the message input stream.
	 * This method blocks until a complete message has been read.
	 * @return The next message.
	 * @throws IOException 
	 */
	public Message readMessage() throws IOException {
		// Each transmitted message will start with a message type identifier integer.
		// This call will block until we get this identifier.
		int messageTypeId = this.readInt();
		// A message type identifier was received over the stream.
		// Create and return the message using the relevant factory.
		return this.messageFactoryProvider.getFactory(messageTypeId).read(this);
	}
}