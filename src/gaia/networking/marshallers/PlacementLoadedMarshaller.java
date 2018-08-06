package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.MessageIdentifier;
import gaia.networking.messages.PlacementLoaded;

/**
 * The marshaller responsible for reading/writing PlacementLoaded messages.
 */
public class PlacementLoadedMarshaller implements IMessageMarshaller<PlacementLoaded> {

	@Override
	public PlacementLoaded read(DataInputStream dataInputStream) throws IOException {
		// Get the packed position of the placement.
		int position = dataInputStream.readInt();
		// Get the packed composition of the placement.
		int composition = dataInputStream.readInt();
		// Return the constructed message.
		return new PlacementLoaded(composition, position);
	}

	@Override
	public void write(PlacementLoaded message, DataOutputStream dataOutputStream) throws IOException {
		// Write the packed position of the placement.
		dataOutputStream.writeInt(message.getPackedPosition());
		// Write the packed composition of the placement.
		dataOutputStream.writeInt(message.getPackedComposition());
	}
	
	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.PLACEMENT_LOADED;
	}
}