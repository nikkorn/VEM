package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.JoinSuccess;
import gaia.networking.messages.MessageIdentifier;
import gaia.time.Season;
import gaia.time.Time;
import gaia.world.Direction;
import gaia.world.Position;
import gaia.world.players.PositionedPlayer;

/**
 * The marshaller responsible for reading/writing JoinSuccess messages.
 */
public class JoinSuccessMarshaller implements IMessageMarshaller<JoinSuccess> {

	@Override
	public JoinSuccess read(DataInputStream dataInputStream) throws IOException {
		// Read the world seed.
		long worldSeed = dataInputStream.readLong();
		// Read the world time minute.
		int minute = dataInputStream.readShort();
		// Read the world time hour.
		int hour = dataInputStream.readShort();
		// Read the world time day.
		int day = dataInputStream.readShort();
		// Read the world time season.
		Season season = Season.values()[dataInputStream.readShort()];
		// Read the list of positioned players in the world.
		ArrayList<PositionedPlayer> players = readPositionedPlayers(dataInputStream);		
		// Return the message.
		return new JoinSuccess(worldSeed, new Time(season, day, hour, minute), players);
	}
	
	/**
	 * Read the list of positioned players in the world into a list.
	 * @param dataInputStream The data input stream.
	 * @return The list of positioned players in the world into a list.
	 * @throws IOException
	 */
	private ArrayList<PositionedPlayer> readPositionedPlayers(DataInputStream dataInputStream) throws IOException {
		// Create a list to hold the player info.
		ArrayList<PositionedPlayer> players = new ArrayList<PositionedPlayer>();
		// Read the number of players that are in the world.
		int numberOfPlayers = dataInputStream.readShort();
		// Read the player details from the stream.
		for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++) {
			// Read the player's id.
			String playerId = dataInputStream.readUTF();
			// Read the position of the player.
			Position position = Position.fromPackedInt(dataInputStream.readInt());
			// Read the facing direction of the player.
			Direction facingDirection = Direction.values()[dataInputStream.readShort()];
			// Add the positioned player to the list.
			players.add(new PositionedPlayer(playerId, position, facingDirection));
		}
		// Return the list of player's info.
		return players;
	}

	@Override
	public void write(JoinSuccess message, DataOutputStream dataOutputStream) throws IOException {
		// Write the world seed.
		dataOutputStream.writeLong(message.getWorldSeed());
		// Write the world time minute.
		dataOutputStream.writeShort(message.getWorldTime().getMinute());
		// Write the world time hour.
		dataOutputStream.writeShort(message.getWorldTime().getHour());
		// Write the world time day.
		dataOutputStream.writeShort(message.getWorldTime().getDay());
		// Write the world time season.
		dataOutputStream.writeShort(message.getWorldTime().getSeason().ordinal());
		// Write the players that are in the world.
		writePositionedPlayers(message.getPositionedPlayers(), dataOutputStream);
	}
	
	/**
	 * Write the list of positioned players to an output stream.
	 * @param players The players positioned in the world.
	 * @param dataOutputStream The data output stream.
	 * @throws IOException 
	 */
	private void writePositionedPlayers(ArrayList<PositionedPlayer> players, DataOutputStream dataOutputStream) throws IOException {
		// Write the number of players that are in the world.
		dataOutputStream.writeShort(players.size());
		// Write the player details to the stream.
		for (PositionedPlayer player : players) {
			// Write the player's id.
			dataOutputStream.writeUTF(player.getPlayerId());
			// Write the packed position of the player.
			dataOutputStream.writeInt(player.getPosition().asPackedInt());
			// Write the facing direction of the player.
			dataOutputStream.writeShort(player.getFacingDirection().ordinal());
		}
	}

	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.JOIN_SUCCESS;
	}
}