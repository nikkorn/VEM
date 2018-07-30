package gaia.networking;

import gaia.networking.marshallers.HandshakeMarshaller;
import gaia.networking.marshallers.JoinFailureMarshaller;
import gaia.networking.marshallers.JoinSuccessMarshaller;
import gaia.networking.marshallers.MovePlayerMarshaller;
import gaia.networking.marshallers.PlayerMovedMarshaller;
import gaia.networking.marshallers.PlayerSpawnedMarshaller;

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
		// Add the marshaller used for reading/writing move player messages.
		provider.addMarshaller(new MovePlayerMarshaller());
		// Add the marshaller used for reading/writing player moved messages.
		provider.addMarshaller(new PlayerMovedMarshaller());
		// Add the marshaller used for reading/writing player spawn messages.
		provider.addMarshaller(new PlayerSpawnedMarshaller());
		// Return the provider.
		return provider;
	}
}
