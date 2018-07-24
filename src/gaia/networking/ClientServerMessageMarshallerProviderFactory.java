package gaia.networking;

import gaia.networking.marshallers.HandshakeMarshaller;

/**
 * Factory for creating MessageMarshallerProvider instances.
 */
public class ClientServerMessageMarshallerProviderFactory {
	
	/**
	 * Create MessageMarshallerProvider for client/server messaging.
	 * @return A MessageMarshallerProvider for client/server messaging.
	 */
	public static MessageMarshallerProvider create() {
		// Create the marshaller provider.
		MessageMarshallerProvider provider = new MessageMarshallerProvider();
		// Add the marshaller used for reading/writing handshake messages.
		provider.addMarshaller(new HandshakeMarshaller());
		// Return the provider.
		return provider;
	}
}
