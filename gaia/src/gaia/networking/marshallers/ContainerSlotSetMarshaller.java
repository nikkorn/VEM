package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.ContainerSlotSet;
import gaia.networking.messages.MessageIdentifier;
import gaia.world.Position;
import gaia.world.items.ItemType;

/**
 * The marshaller responsible for reading/writing ContainerSlotSet messages.
 */
public class ContainerSlotSetMarshaller implements IMessageMarshaller<ContainerSlotSet> {

	@Override
	public ContainerSlotSet read(DataInputStream dataInputStream) throws IOException {
		// Read the position of the container.
		Position position = Position.fromPackedInt(dataInputStream.readInt());
		// Read the container slot index.
		short slotIndex = (short)(dataInputStream.readByte());
		// Read the container item type.
		ItemType itemType = ItemType.values()[dataInputStream.readInt()];
		// Return the constructed message.
		return new ContainerSlotSet(position, slotIndex, itemType);
	}

	@Override
	public void write(ContainerSlotSet message, DataOutputStream dataOutputStream) throws IOException {
		// Write the position of the container.
		dataOutputStream.writeInt(message.getPosition().asPackedInt());
		// Write the container slot index.
		dataOutputStream.writeByte((byte) message.getSlotIndex());
		// Write the item type ordinal.
		dataOutputStream.writeInt(message.getItemHeld().ordinal());
	}
	
	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.CONTAINER_SLOT_SET;
	}
}
