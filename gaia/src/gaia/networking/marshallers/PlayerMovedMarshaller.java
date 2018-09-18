package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.MessageIdentifier;
import gaia.networking.messages.PlayerMoved;
import gaia.world.Direction;
import gaia.world.Position;

/**
 * The marshaller responsible for reading/writing PlayerMoved messages.
 */
public class PlayerMovedMarshaller implements IMessageMarshaller<PlayerMoved> {

	@Override
	public PlayerMoved read(DataInputStream dataInputStream) throws IOException {
		// Get the id of the moving player.
		String playerId = dataInputStream.readUTF();
		// Get the new packed x/y position of the moving player.
		int position = dataInputStream.readInt();
		// Get the direction that the player has moved in to reach the position.
		Direction direction = Direction.values()[dataInputStream.readShort()];
		// Return the constructed message.
		return new PlayerMoved(playerId, Position.fromPackedInt(position), direction);
	}

	@Override
	public void write(PlayerMoved message, DataOutputStream dataOutputStream) throws IOException {
		// Write the id of the moving player.
		dataOutputStream.writeUTF(message.getPlayerId());
		// Write the new packed x/y position of the player.
		dataOutputStream.writeInt(message.getNewPosition().asPackedInt());
		// Write the direction that the player has moved in to reach the position.
		dataOutputStream.writeShort(message.getDirectionOfMovement().ordinal());
	}
	
	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.PLAYER_MOVED;
	}
}
