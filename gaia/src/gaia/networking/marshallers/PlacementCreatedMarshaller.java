package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.MessageIdentifier;
import gaia.networking.messages.PlacementCreated;
import gaia.world.Position;

/**
 * The marshaller responsible for reading/writing PlacementCreated messages.
 */
public class PlacementCreatedMarshaller implements IMessageMarshaller<PlacementCreated> {

	@Override
	public PlacementCreated read(DataInputStream dataInputStream) throws IOException {
		// Read the position of the placement.
		Position position = Position.fromPackedInt(dataInputStream.readInt());
		// Read the packed composition of the placement.
		int packedComposition = dataInputStream.readInt();
		// Return the constructed message.
		return new PlacementCreated(position, packedComposition);
	}

	@Override
	public void write(PlacementCreated message, DataOutputStream dataOutputStream) throws IOException {
		// Write the packed position of the placement.
		dataOutputStream.writeInt(message.getPosition().asPackedInt());
		// Write the packed composition of the placement.
		dataOutputStream.writeInt(message.getComposition());
	}

	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.PLACEMENT_CREATED;
	}
}
