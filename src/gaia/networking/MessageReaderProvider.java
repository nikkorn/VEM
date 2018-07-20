package gaia.networking;

import java.util.HashMap;

/**
 * Provider of message readers.
 */
public class MessageReaderProvider {
	/**
	 * The message readers.
	 */
	private HashMap<Integer, IMessageReader> readers = new HashMap<Integer, IMessageReader>();
	
	/**
	 * Add a message reader.
	 * @param messageReader The message reader to add.
	 */
	public void addReader(IMessageReader reader) {
		this.readers.put(reader.getMessageTypeId(), reader);
	}
	
	/**
	 * Get the message reader responsible for reading the specified message type.
	 * @param messageTypeId The message type id.
	 * @return The message reader responsible for reading the specified message type.
	 */
	public IMessageReader getReader(int messageTypeId) {
		// We expect a message reader to exist that can handle reading messages of this type.
		if (!this.readers.containsKey(messageTypeId)) {
			// We do not have a reader that can read this message!
			throw new RuntimeException("No message reader exists for message type id: " + messageTypeId);
		}
		// We have the message reader we need so return it.
		return this.readers.get(messageTypeId);
	}
}
