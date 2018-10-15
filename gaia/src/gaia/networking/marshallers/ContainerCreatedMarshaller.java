package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.ContainerCreated;
import gaia.networking.messages.MessageIdentifier;
import gaia.world.Position;
import gaia.world.items.ItemType;
import gaia.world.items.container.ContainerCategory;
import gaia.world.items.container.ContainerType;
import gaia.world.items.container.IndexedSlot;

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
		// Read the number of indexed items in the container.
		int numberOfIndexedItems = dataInputStream.readShort();
		// Read each indexed slot in the container.
		ArrayList<IndexedSlot> indexedSlots = new ArrayList<IndexedSlot>();
		for (int indexedSlotCount = 0; indexedSlotCount < numberOfIndexedItems; indexedSlotCount++) {
			// Read the index of the slot.
			int slotIndex = dataInputStream.readShort();
			// Read the item held in the slot.
			ItemType held = ItemType.values()[dataInputStream.readShort()];
			// Add the indexed slot to the list.
			indexedSlots.add(new IndexedSlot(slotIndex, held));
		}
		// Return the constructed message.
		return new ContainerCreated(position, type, category, size, indexedSlots);
	}

	@Override
	public void write(ContainerCreated message, DataOutputStream dataOutputStream) throws IOException {
		// Write the position of the container.
		dataOutputStream.writeInt(message.getPosition().asPackedInt());
		// Write the type of the container
		dataOutputStream.writeShort(message.getContainerType().ordinal());
		// Write the category of the container
		dataOutputStream.writeShort(message.getContainerCategory().ordinal());
		// Write the size of the container.
		dataOutputStream.writeShort(message.getSize());
		// Write the number of indexed items in the container.
		dataOutputStream.writeShort(message.getIndexedSlots().size());
		// Write out each indexed slot.
		for (IndexedSlot slot : message.getIndexedSlots()) {
			// Write the index of the slot.
			dataOutputStream.writeShort(slot.getIndex());
			// Write the item held in the slot.
			dataOutputStream.writeShort(slot.get().ordinal());
		}
	}

	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.CONTAINER_CREATED;
	}
}