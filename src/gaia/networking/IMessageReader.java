package gaia.networking;

import java.io.DataInputStream;

/**
 * Reader of a specific message type from a stream.
 */
public interface IMessageReader {
	
	/**
	 * Read the message from the input stream.
	 * @param dataInputStream The input stream to read the message from.
	 * @return The read message.
	 */
	IMessage read(DataInputStream dataInputStream);
	
	/**
	 * Get the id of the message type that this reader deals with.
	 * @return The id of the message type that this reader deals with.
	 */
	int getMessageTypeId();
}
