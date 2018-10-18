package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.ContainerAdded;
import gaia.networking.messages.MessageIdentifier;
import gaia.world.Position;
import gaia.world.items.ItemType;
import gaia.world.items.container.ContainerCategory;
import gaia.world.items.container.ContainerType;

/**
 * The marshaller responsible for reading/writing ContainerAdded messages.
 */
public class ContainerAddedMarshaller implements IMessageMarshaller<ContainerAdded> {

	@Override
	public ContainerAdded read(DataInputStream dataInputStream) throws IOException {
		// Read the position of the container.
		Position position = Position.fromPackedInt(dataInputStream.readInt());
		// Read the type of the container
		ContainerType type = ContainerType.values()[dataInputStream.readShort()];
		// Read the category of the container
		ContainerCategory category = ContainerCategory.values()[dataInputStream.readShort()];
		// Read the size of the container.
		int size = dataInputStream.readShort();
		// Read the number of populated slots in the container.
		int numberOfPopulatedSlots = dataInputStream.readShort();
		// Create the array to hold the items held in the container.
		ItemType[] items = new ItemType[size];
		// For now, all of the items in the container are of the NONE type.
		Arrays.fill(items, ItemType.NONE);
		// Read each populated slot in the container.
		for (int populatedSlotCount = 0; populatedSlotCount < numberOfPopulatedSlots; populatedSlotCount++) {
			// Read the index of the slot.
			int slotIndex = dataInputStream.readShort();
			// Read the item held in the slot.
			ItemType itemHeld = ItemType.values()[dataInputStream.readShort()];
			// Set the item held at this slot in our items array.
			items[slotIndex] = itemHeld;
		}
		// Return the constructed message.
		return new ContainerAdded(position, type, category, items);
	}

	@Override
	public void write(ContainerAdded message, DataOutputStream dataOutputStream) throws IOException {
		// Write the position of the container.
		dataOutputStream.writeInt(message.getPosition().asPackedInt());
		// Write the type of the container
		dataOutputStream.writeShort(message.getContainerType().ordinal());
		// Write the category of the container
		dataOutputStream.writeShort(message.getContainerCategory().ordinal());
		// Write the size of the container.
		dataOutputStream.writeShort(message.getItemsHeld().length);
		// Write the number of populated slots in the container.
		dataOutputStream.writeShort(getPopulatedSlotsCount(message.getItemsHeld()));
		// Write out each item that is not NONE.
		for (int slotIndex = 0; slotIndex < message.getItemsHeld().length; slotIndex++) {
			// Get the item held at this slot.
			ItemType item = message.getItemsHeld()[slotIndex];
			// If the item at this slot is of the NONE type then there is no point send any info.
			if (item.isNothing()) {
				continue;
			}
			// There is an item at this slot!
			// Write the index of the slot.
			dataOutputStream.writeShort(slotIndex);
			// Write the item held in the slot.
			dataOutputStream.writeShort(item.ordinal());
		}
	}

	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.CONTAINER_ADDED;
	}
	
	/**
	 * Find the number of items in an item array that are not of the NONE type.
	 * @param items The items array.
	 * @return The number of items in an item array that are not of the NONE type.
	 */
	private static int getPopulatedSlotsCount(ItemType[] items) {
		// Keep track of how may items are not the NONE type.
		int filledSlots = 0;
		// Check each item.
		for (ItemType item : items) {
			if (!item.isNothing()) {
				filledSlots++;
			}
		}
		// Return the number of non-NONE item types.
		return filledSlots;
	}
}