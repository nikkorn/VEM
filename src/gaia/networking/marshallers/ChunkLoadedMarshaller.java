package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.ChunkLoaded;
import gaia.networking.messages.PackedPlacement;
import gaia.networking.messages.MessageIdentifier;

/**
 * The marshaller responsible for reading/writing ChunkLoaded messages.
 */
public class ChunkLoadedMarshaller implements IMessageMarshaller<ChunkLoaded> {

	@Override
	public ChunkLoaded read(DataInputStream dataInputStream) throws IOException {
		// Read the x/y position of the chunk.
		short chunkX = (short)(dataInputStream.readByte() & 0xFF);
		short chunkY = (short)(dataInputStream.readByte() & 0xFF);
		// Read the number of packed placements.
		short placementCount = (short)dataInputStream.readInt();
		// Create a list to hold the placements.
		ArrayList<PackedPlacement> placements = new ArrayList<PackedPlacement>();
		// Read each placement into the placements list.
		for (int placementIndex = 0; placementIndex < placementCount; placementIndex++) {
			// Read the x/y position of the placement relative to the chunk position.
			short placementX = (short)(dataInputStream.readByte() & 0xFF);
			short placementY = (short)(dataInputStream.readByte() & 0xFF);
			// Read the packed composition of the placement.
			int composition = dataInputStream.readInt();
			// Add the read placement into the placements list.
			placements.add(new PackedPlacement(placementX, placementY, composition));
		}
		// Return the constructed message.
		return new ChunkLoaded(chunkX, chunkY, placements);
	}

	@Override
	public void write(ChunkLoaded message, DataOutputStream dataOutputStream) throws IOException {
		// Write the x/y position of the chunk.
		dataOutputStream.writeByte((byte) message.getX());
		dataOutputStream.writeByte((byte) message.getY());
		// Write the number of packed placements.
		dataOutputStream.writeInt(message.getPackedPlacements().size());
		// Write out each packed placement.
		for (PackedPlacement placement : message.getPackedPlacements()) {
			// Write the x/y position of the placement relative to the chunk position.
			dataOutputStream.writeByte((byte) placement.getX());
			dataOutputStream.writeByte((byte) placement.getY());
			// Write the packed composition of the placement.
			dataOutputStream.writeInt(placement.getComposition());
		}
	}
	
	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.CHUNK_LOADED;
	}
}