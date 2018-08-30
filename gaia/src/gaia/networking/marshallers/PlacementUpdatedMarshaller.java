package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.MessageIdentifier;
import gaia.networking.messages.PlacementUpdated;
import gaia.world.Position;

/**
 * The marshaller responsible for reading/writing PlacementUpdated messages.
 */
public class PlacementUpdatedMarshaller implements IMessageMarshaller<PlacementUpdated> {

	@Override
	public PlacementUpdated read(DataInputStream dataInputStream) throws IOException {
		// Read the position of the placement.
		Position position = Position.fromPackedInt(dataInputStream.readInt());
		// Read the packed composition of the placement.
		int packedComposition = dataInputStream.readInt();
		// Return the constructed message.
		return new PlacementUpdated(position, packedComposition);
	}

	@Override
	public void write(PlacementUpdated message, DataOutputStream dataOutputStream) throws IOException {
		// Write the packed position of the placement.
		dataOutputStream.writeInt(message.getPosition().asPackedInt());
		// Write the packed composition of the placement.
		dataOutputStream.writeInt(message.getComposition());
	}

	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.PLACEMENT_UPDATED;
	}
}
