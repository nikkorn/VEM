package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.InventorySlotSet;
import gaia.networking.messages.MessageIdentifier;
import gaia.world.items.ItemType;

/**
 * The marshaller responsible for reading/writing InventorySlotSet messages.
 */
public class InventorySlotSetMarshaller implements IMessageMarshaller<InventorySlotSet> {

	@Override
	public InventorySlotSet read(DataInputStream dataInputStream) throws IOException {
		// Read the slot index.
		short slotIndex = (short)(dataInputStream.readByte());
		// Read the item type.
		ItemType itemType = ItemType.values()[dataInputStream.readInt()];
		// Return the constructed message.
		return new InventorySlotSet(itemType, slotIndex);
	}

	@Override
	public void write(InventorySlotSet message, DataOutputStream dataOutputStream) throws IOException {
		// Write the slot index.
		dataOutputStream.writeByte((byte) message.getSlotIndex());
		// Write the item type ordinal.
		dataOutputStream.writeInt(message.getItemHeld().ordinal());
	}
	
	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.INVENTORY_SLOT_CHANGED;
	}
}
