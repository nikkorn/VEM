package gaia.server.networking.readers;

import java.io.DataInputStream;
import java.io.IOException;
import gaia.networking.IMessage;
import gaia.networking.IMessageReader;
import gaia.networking.messages.Handshake;

/**
 * Reader of handshake messages sent from the client.
 */
public class HandshakeReader implements IMessageReader {

	@Override
	public IMessage read(DataInputStream dataInputStream) throws IOException {
		// Create the handshake message, we are just expecting the player id.
		return new Handshake(dataInputStream.readUTF());
	}

	@Override
	public int getMessageTypeId() {
		return 0;
	}
}
