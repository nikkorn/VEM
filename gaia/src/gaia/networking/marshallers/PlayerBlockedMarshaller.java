package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.MessageIdentifier;
import gaia.networking.messages.PlayerBlocked;
import gaia.world.Position;

/**
 * The marshaller responsible for reading/writing PlayerBlocked messages.
 */
public class PlayerBlockedMarshaller implements IMessageMarshaller<PlayerBlocked> {

	@Override
	public PlayerBlocked read(DataInputStream dataInputStream) throws IOException {
		// Get the packed x/y position of the blocked player.
		int position = dataInputStream.readInt();
		// Return the constructed message.
		return new PlayerBlocked(Position.fromPackedInt(position));
	}

	@Override
	public void write(PlayerBlocked message, DataOutputStream dataOutputStream) throws IOException {
		// Write the packed x/y position of the blocked player.
		dataOutputStream.writeInt(message.getPosition().asPackedInt());
	}
	
	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.PLAYER_BLOCKED;
	}
}
