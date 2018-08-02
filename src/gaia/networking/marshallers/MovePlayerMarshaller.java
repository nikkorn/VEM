package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.MessageIdentifier;
import gaia.networking.messages.MovePlayer;
import gaia.world.Direction;

/**
 * The marshaller responsible for reading/writing MovePlayer messages.
 */
public class MovePlayerMarshaller implements IMessageMarshaller<MovePlayer> {

	@Override
	public MovePlayer read(DataInputStream dataInputStream) throws IOException {
		return new MovePlayer(Direction.values()[dataInputStream.readInt()]);
	}

	@Override
	public void write(MovePlayer message, DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(message.getDirection().ordinal());
	}

	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.MOVE_PLAYER;
	}
}
