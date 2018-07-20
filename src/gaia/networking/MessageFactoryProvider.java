package gaia.networking;

import java.util.HashMap;

/**
 * Provider of message factories.
 */
public class MessageFactoryProvider {
	/**
	 * The message factories.
	 */
	private HashMap<Integer, IMessageFactory> messageFactories = new HashMap<Integer, IMessageFactory>();
	
	/**
	 * Add a message factory.
	 * @param messageFactory The message factory to add.
	 */
	public void addMessageFactory(IMessageFactory messageFactory) {
		this.messageFactories.put(messageFactory.getMessageTypeId(), messageFactory);
	}
	
	/**
	 * Get the message factory responsible for creating the specified message type.
	 * @param messageTypeId The message type id.
	 * @return The message factory responsible for creating the specified message type.
	 */
	public IMessageFactory getFactory(int messageTypeId) {
		// We expect a message factory to exist that can handle creating messages of this type.
		if (!this.messageFactories.containsKey(messageTypeId)) {
			// We do not have a factory that can create this message!
			throw new RuntimeException("No message factory exists for message type id: " + messageTypeId);
		}
		// We have the message factory we need so return it.
		return this.messageFactories.get(messageTypeId);
	}
}
