package gaia.networking;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Writes messages to a data stream. 
 */
public class MessageOutputStream extends DataOutputStream {
	/**
	 * The message writer provider.
	 */
	private MessageWriterProvider messageWriterProvider;

	/**
	 * Create a new instance of the MessageOutputStream class.
	 * @param outputStream The output stream to write messages to.
	 * @param messageWriterProvider The provider of message writers.
	 */
	public MessageOutputStream(OutputStream outputStream, MessageWriterProvider messageWriterProvider) {
		super(outputStream);
		this.messageWriterProvider = messageWriterProvider;
	}
	
	/**
	 * Write the message to the output stream.
	 * @throws IOException 
	 */
	public <TMessage extends IMessage> void writeMessage(TMessage message) throws IOException {
		// Get the message writer with which to write this message.
		IMessageWriter<TMessage> writer = (IMessageWriter<TMessage>) this.messageWriterProvider.getWriter(message.getTypeId());
		// We will ALWAYS write the message type id to the stream first, the message writer will handle the rest.
		this.writeInt(message.getTypeId());
		// Use the message writer to write the rest of the message.
		writer.write(message, this);
		// We should always flush the stream after writing a message.
		this.flush();
	}
}