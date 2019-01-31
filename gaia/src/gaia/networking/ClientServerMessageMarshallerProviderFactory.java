package gaia.networking;

import gaia.networking.marshallers.*;

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
		// Add the marshaller used for reading/writing player blocked messages.
		provider.addMarshaller(new PlayerBlockedMarshaller());
		// Add the marshaller used for reading/writing player spawn messages.
		provider.addMarshaller(new PlayerSpawnedMarshaller());
		// Add the marshaller used for reading/writing placement updated messages.
		provider.addMarshaller(new PlacementUpdatedMarshaller());
		// Add the marshaller used for reading/writing placement created messages.
		provider.addMarshaller(new PlacementCreatedMarshaller());
		// Add the marshaller used for reading/writing placement removed messages.
		provider.addMarshaller(new PlacementRemovedMarshaller());
		// Add the marshaller used for reading/writing container created messages.
		provider.addMarshaller(new ContainerAddedMarshaller());
		// Add the marshaller used for reading/writing container slot set messages.
		provider.addMarshaller(new ContainerSlotSetMarshaller());
		// Add the marshaller used for reading/writing inventory slot set messages.
		provider.addMarshaller(new InventorySlotSetMarshaller());
		// Add the marshaller used for reading/writing use inventory item messages.
		provider.addMarshaller(new UseInventoryItemMarshaller());
		// Add the marshaller used for reading/writing swap inventory and container item messages.
		provider.addMarshaller(new SwapContainerAndInventoryItemMarshaller());
		// Return the provider.
		return provider;
	}
}
