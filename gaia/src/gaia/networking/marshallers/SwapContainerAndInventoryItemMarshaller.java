package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.MessageIdentifier;
import gaia.networking.messages.SwapContainerAndInventoryItem;
import gaia.world.items.ItemType;

/**
 * The marshaller responsible for reading/writing SwapContainerAndInventoryItem messages.
 */
public class SwapContainerAndInventoryItemMarshaller implements IMessageMarshaller<SwapContainerAndInventoryItem>  {

	@Override
	public SwapContainerAndInventoryItem read(DataInputStream dataInputStream) throws IOException {
		// Read the container slot index.
		short containerSlotIndex = (short)(dataInputStream.readByte());
		// Read the expected container item type.
		ItemType expectedContainerItem = ItemType.values()[dataInputStream.readInt()];
		// Read the inventory slot index.
		short inventorySlotIndex = (short)(dataInputStream.readByte());
		// Read the expected inventory item type.
		ItemType expectedInventoryItem = ItemType.values()[dataInputStream.readInt()];
		// Return the constructed message.
		return new SwapContainerAndInventoryItem(containerSlotIndex, expectedContainerItem, inventorySlotIndex, expectedInventoryItem);
	}

	@Override
	public void write(SwapContainerAndInventoryItem message, DataOutputStream dataOutputStream) throws IOException {
		// Write the container slot index.
		dataOutputStream.writeByte((byte) message.getContainerSlotIndex());
		// Write the expected container item type ordinal.
		dataOutputStream.writeInt(message.getExpectedContainerItem().ordinal());
		// Write the inventory slot index.
		dataOutputStream.writeByte((byte) message.getInventorySlotIndex());
		// Write the expected inventory item type ordinal.
		dataOutputStream.writeInt(message.getExpectedInventoryItem().ordinal());
	}

	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.SWAP_CONTAINER_AND_INVENTORY_ITEM;
	}
}
