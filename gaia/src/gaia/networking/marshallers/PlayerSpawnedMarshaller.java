package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.MessageIdentifier;
import gaia.networking.messages.PlayerSpawned;
import gaia.world.Position;

/**
 * The marshaller responsible for reading/writing PlayerSpawned messages.
 */
public class PlayerSpawnedMarshaller implements IMessageMarshaller<PlayerSpawned> {

	@Override
	public PlayerSpawned read(DataInputStream dataInputStream) throws IOException {
		// Get the id of the spawning player.
		String playerId = dataInputStream.readUTF();
		// Get the packed x/y position of the spawning player.
		int position = dataInputStream.readInt();
		// Return the constructed message.
		return new PlayerSpawned(playerId, Position.fromPackedInt(position));
	}

	@Override
	public void write(PlayerSpawned message, DataOutputStream dataOutputStream) throws IOException {
		// Write the id of the spawning player.
		dataOutputStream.writeUTF(message.getPlayerId());
		// Write the packed x/y position of the spawn.
		dataOutputStream.writeInt(message.getSpawnPosition().asPackedInt());
	}

	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.PLAYER_SPAWNED;
	}
}