package gaia.networking;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Reads messages from a data stream. 
 */
public class MessageInputStream extends DataInputStream {
	/**
	 * The message reader provider.
	 */
	private MessageReaderProvider messageReaderProvider;

	/**
	 * Create a new instance of the MessageInputStream class.
	 * @param inputStream The input stream from which to read messages.
	 * @param messageReaderProvider The provider of message readers.
	 */
	public MessageInputStream(InputStream inputStream, MessageReaderProvider messageReaderProvider) {
		super(inputStream);
		this.messageReaderProvider = messageReaderProvider;
	}
	
	/**
	 * Read the next message from the message input stream.
	 * This method blocks until a complete message has been read.
	 * @return The next message.
	 * @throws IOException 
	 */
	public IMessage readMessage() throws IOException {
		// Each transmitted message will start with a message type identifier integer.
		// This call will block until we get this identifier.
		int messageTypeId = this.readInt();
		// A message type identifier was received over the stream.
		// Create and return the message using the relevant reader.
		return this.messageReaderProvider.getReader(messageTypeId).read(this);
	}
}