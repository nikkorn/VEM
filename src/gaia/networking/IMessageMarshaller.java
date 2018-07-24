package gaia.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Responsible for reading/writing a message of a specific type from/to a message stream.
 * @param <TMessage> The message type.
 */
public interface IMessageMarshaller <TMessage extends IMessage>  {
	
	/**
	 * Read the message from the input stream.
	 * @param dataInputStream The input stream to read the message from.
	 * @return The read message.
	 * @throws IOException
	 */
	TMessage read(DataInputStream dataInputStream) throws IOException;
	
	/**
	 * Write the message as primitives to the output stream.
	 * @param message The message to write.
	 * @param dataOutputStream The output stream to write messages to.
	 * @throws IOException
	 */
	void write(TMessage message, DataOutputStream dataOutputStream) throws IOException;
	
	/**
	 * Get the id of the message type that this reader deals with.
	 * @return The id of the message type that this reader deals with.
	 */
	int getMessageTypeId();
}