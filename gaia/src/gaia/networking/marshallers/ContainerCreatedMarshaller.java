package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.ContainerCreated;
import gaia.networking.messages.MessageIdentifier;
import gaia.world.Position;
import gaia.world.items.container.ContainerCategory;
import gaia.world.items.container.ContainerType;

/**
 * The marshaller responsible for reading/writing ContainerCreated messages.
 */
public class ContainerCreatedMarshaller implements IMessageMarshaller<ContainerCreated> {

	@Override
	public ContainerCreated read(DataInputStream dataInputStream) throws IOException {
		// Read the position of the container.
		Position position = Position.fromPackedInt(dataInputStream.readInt());
		// Read the type of the container
		ContainerType type = ContainerType.values()[dataInputStream.readShort()];
		// Read the category of the container
		ContainerCategory category = ContainerCategory.values()[dataInputStream.readShort()];
		// Read the size of the container.
		int size = dataInputStream.readShort();
		
		// ...
		
		// Return the constructed message.
		return new ContainerCreated(position, type, category, size, null);
	}

	@Override
	public void write(ContainerCreated message, DataOutputStream dataOutputStream) throws IOException {
		// ...
	}

	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.CONTAINER_CREATED;
	}
}