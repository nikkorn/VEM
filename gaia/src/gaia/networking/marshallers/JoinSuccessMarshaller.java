package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.JoinSuccess;
import gaia.networking.messages.MessageIdentifier;
import gaia.time.Season;
import gaia.time.Time;

/**
 * The marshaller responsible for reading/writing JoinSuccess messages.
 */
public class JoinSuccessMarshaller implements IMessageMarshaller<JoinSuccess> {

	@Override
	public JoinSuccess read(DataInputStream dataInputStream) throws IOException {
		// Read the world seed.
		long worldSeed = dataInputStream.readLong();
		// Read the world time minute.
		int minute = dataInputStream.readInt();
		// Read the world time hour.
		int hour = dataInputStream.readInt();
		// Read the world time day.
		int day = dataInputStream.readInt();
		// Read the world time season.
		Season season = Season.values()[dataInputStream.readInt()];
		// Return the message
		return new JoinSuccess(worldSeed, new Time(season, day, hour, minute));
	}

	@Override
	public void write(JoinSuccess message, DataOutputStream dataOutputStream) throws IOException {
		// Write the world seed.
		dataOutputStream.writeLong(message.getWorldSeed());
		// Write the world time minute.
		dataOutputStream.writeInt(message.getWorldTime().getMinute());
		// Write the world time hour.
		dataOutputStream.writeInt(message.getWorldTime().getHour());
		// Write the world time day.
		dataOutputStream.writeInt(message.getWorldTime().getDay());
		// Write the world time season.
		dataOutputStream.writeInt(message.getWorldTime().getSeason().ordinal());
	}

	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.JOIN_SUCCESS;
	}
}