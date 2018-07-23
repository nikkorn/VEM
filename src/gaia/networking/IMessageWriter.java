package gaia.networking;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Writer of a specific message type to a stream.
 * @param <TMessage> The message type.
 */
public interface IMessageWriter<TMessage extends IMessage> {
	
	/**
	 * Write the message as primitives to the output stream.
	 * @param message The message to write.
	 * @param dataOutputStream The output stream to write messages to.
	 * @throws IOException
	 */
	void write(TMessage message, DataOutputStream dataOutputStream) throws IOException;
	
	/**
	 * Get the id of the message type that this writer deals with.
	 * @return The id of the message type that this writer deals with.
	 */
	int getMessageTypeId();
}
