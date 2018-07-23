package gaia.client.networking.writers;

import java.io.DataOutputStream;
import java.io.IOException;
import gaia.networking.IMessageWriter;
import gaia.networking.messages.Handshake;

/**
 * Writer of handshake messages to send to the server.
 */
public class HandshakeWriter implements IMessageWriter<Handshake> {

	@Override
	public void write(Handshake message, DataOutputStream dataOutputStream) throws IOException {
		// Just write the player id. 
		dataOutputStream.writeUTF(message.getPlayerId());
	}

	@Override
	public int getMessageTypeId() {
		return 0;
	}
}
