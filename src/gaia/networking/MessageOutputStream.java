package gaia.networking;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Writes messages to a data stream. 
 */
public class MessageOutputStream extends DataOutputStream {
	/**
	 * The message marshaller provider.
	 */
	private MessageMarshallerProvider messageMarshallerProvider;

	/**
	 * Create a new instance of the MessageOutputStream class.
	 * @param outputStream The output stream to write messages to.
	 * @param messageMarshallerProvider The provider of message marshallers.
	 */
	public MessageOutputStream(OutputStream outputStream, MessageMarshallerProvider messageMarshallerProvider) {
		super(outputStream);
		this.messageMarshallerProvider = messageMarshallerProvider;
	}
	
	/**
	 * Write the message to the output stream.
	 * @param message The message to write.
	 * @throws IOException
	 */
	public <TMessage extends IMessage> void writeMessage(TMessage message) throws IOException {
		// Get the message marshaller with which to write this message.
		IMessageMarshaller<TMessage> writer = (IMessageMarshaller<TMessage>) this.messageMarshallerProvider.get(message.getTypeId());
		// We will ALWAYS write the message type id to the stream first, the message writer will handle the rest.
		this.writeInt(message.getTypeId());
		// Use the message writer to write the rest of the message.
		writer.write(message, this);
		// We should always flush the stream after writing a message.
		this.flush();
	}
}