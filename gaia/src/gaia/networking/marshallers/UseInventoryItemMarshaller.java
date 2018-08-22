package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.MessageIdentifier;
import gaia.networking.messages.UseInventoryItem;
import gaia.world.items.ItemType;

/**
 * The marshaller responsible for reading/writing UseInventoryItem messages.
 */
public class UseInventoryItemMarshaller implements IMessageMarshaller<UseInventoryItem> {

	@Override
	public UseInventoryItem read(DataInputStream dataInputStream) throws IOException {
		// Read the slot index.
		short slotIndex = (short)(dataInputStream.readByte());
		// Read the expected item type.
		ItemType expected = ItemType.values()[dataInputStream.readInt()];
		// Return the constructed message.
		return new UseInventoryItem(slotIndex, expected);
	}

	@Override
	public void write(UseInventoryItem message, DataOutputStream dataOutputStream) throws IOException {
		// Write the slot index.
		dataOutputStream.writeByte((byte) message.getSlotIndex());
		// Write the expected item type ordinal.
		dataOutputStream.writeInt(message.getExpectedItemType().ordinal());
	}
	
	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.USE_INVENTORY_ITEM;
	}
}