package gaia.networking;

import gaia.networking.marshallers.HandshakeMarshaller;
import gaia.networking.marshallers.JoinFailureMarshaller;
import gaia.networking.marshallers.JoinSuccessMarshaller;

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
		// Add the marshaller used for reading/writing join success messages.
		provider.addMarshaller(new JoinSuccessMarshaller());
		// Add the marshaller used for reading/writing join failure messages.
		provider.addMarshaller(new JoinFailureMarshaller());
		// Return the provider.
		return provider;
	}
}
