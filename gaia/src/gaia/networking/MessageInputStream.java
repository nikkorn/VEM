package gaia.networking;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Reads messages from a data stream. 
 */
public class MessageInputStream extends DataInputStream {
	/**
	 * The message marshaller provider.
	 */
	private MessageMarshallerProvider messageMarshallerProvider;

	/**
	 * Create a new instance of the MessageInputStream class.
	 * @param inputStream The input stream from which to read messages.
	 * @param messageMarshallerProvider The provider of message marshallers.
	 */
	public MessageInputStream(InputStream inputStream, MessageMarshallerProvider messageMarshallerProvider) {
		super(inputStream);
		this.messageMarshallerProvider = messageMarshallerProvider;
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
		// Create and return the message using the relevant marshaller.
		return this.messageMarshallerProvider.get(messageTypeId).read(this);
	}
}