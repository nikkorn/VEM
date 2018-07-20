package gaia.networking;

import java.io.DataInputStream;

/**
 * A factory for creating a message based on streamed network data.
 */
public interface IMessageFactory {
	
	/**
	 * Read the message from the input stream.
	 * @param dataInputStream The input stream to read the message from
	 * @return
	 */
	Message read(DataInputStream dataInputStream);
	
	// TODO Add write?
	
	/**
	 * Get the message type id.
	 * @return The message type id.
	 */
	int getMessageTypeId();
}
