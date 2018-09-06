package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.MessageIdentifier;
import gaia.networking.messages.PlacementRemoved;
import gaia.world.PlacementType;
import gaia.world.Position;

/**
 * The marshaller responsible for reading/writing PlacementRemoved messages.
 */
public class PlacementRemovedMarshaller implements IMessageMarshaller<PlacementRemoved> {

	@Override
	public PlacementRemoved read(DataInputStream dataInputStream) throws IOException {
		// Read the position of the placement.
		Position position = Position.fromPackedInt(dataInputStream.readInt());
		// Read the type of the placement.
		PlacementType placementType = PlacementType.values()[dataInputStream.readShort()];
		// Return the constructed message.
		return new PlacementRemoved(position, placementType);
	}

	@Override
	public void write(PlacementRemoved message, DataOutputStream dataOutputStream) throws IOException {
		// Write the packed position of the placement.
		dataOutputStream.writeInt(message.getPosition().asPackedInt());
		// Write the type of the placement being removed.
		dataOutputStream.writeShort(message.getExpectedPlacementType().ordinal());
	}

	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.PLACEMENT_REMOVED;
	}
}
