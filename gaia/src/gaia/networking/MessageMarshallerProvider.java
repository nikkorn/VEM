package gaia.networking;

import java.util.HashMap;

/**
 * Provider of message marshallers.
 */
public class MessageMarshallerProvider {
	/**
	 * The message marshallers.
	 */
	private HashMap<Integer, IMessageMarshaller<? extends IMessage>> marshallers = new HashMap<Integer, IMessageMarshaller<? extends IMessage>>();
	
	/**
	 * Add a message marshaller.
	 * @param marshaller The message marshaller to add.
	 */
	public <TMessage extends IMessage> void addMarshaller(IMessageMarshaller<TMessage> marshaller) {
		this.marshallers.put(marshaller.getMessageTypeId(), marshaller);
	}
	
	/**
	 * Get the message marshaller responsible for reading and writing the specified message type.
	 * @param messageTypeId The message type id.
	 * @return The message marshaller responsible for reading and writing the specified message type.
	 */
	public IMessageMarshaller<? extends IMessage> get(int messageTypeId) {
		// We expect a message marshaller to exist that can handle messages of this type.
		if (!this.marshallers.containsKey(messageTypeId)) {
			// We do not have a writer that can handle this message!
			throw new RuntimeException("No message marshaller exists for message type id: " + messageTypeId);
		}
		// We have the message marshaller we need so return it.
		return this.marshallers.get(messageTypeId);
	}
}