package gaia.networking;

import java.util.HashMap;

/**
 * Provider of message writers.
 */
public class MessageWriterProvider {
	/**
	 * The message readers.
	 */
	private HashMap<Integer, IMessageWriter<? extends IMessage>> writers = new HashMap<Integer, IMessageWriter<? extends IMessage>>();
	
	/**
	 * Add a message writer.
	 * @param messageWriter The message writer to add.
	 */
	public <TMessage extends IMessage> void addWriter(IMessageWriter<TMessage> writer) {
		this.writers.put(writer.getMessageTypeId(), writer);
	}
	
	/**
	 * Get the message writer responsible for writing the specified message type.
	 * @param messageTypeId The message type id.
	 * @return The message writer responsible for writing the specified message type.
	 */
	public IMessageWriter<? extends IMessage> getWriter(int messageTypeId) {
		// We expect a message writer to exist that can handle writing messages of this type.
		if (!this.writers.containsKey(messageTypeId)) {
			// We do not have a writer that can write this message!
			throw new RuntimeException("No message writer exists for message type id: " + messageTypeId);
		}
		// We have the message writer we need so return it.
		return this.writers.get(messageTypeId);
	}
}
