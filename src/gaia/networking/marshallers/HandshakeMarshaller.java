package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.Handshake;
import gaia.networking.messages.MessageIdentifier;

/**
 * The marshaller responsible for reading/writing handshake messages.
 */
public class HandshakeMarshaller implements IMessageMarshaller<Handshake> {

	@Override
	public Handshake read(DataInputStream dataInputStream) throws IOException {
		// Create the handshake message, we are just expecting the player id.
		return new Handshake(dataInputStream.readUTF());
	}

	@Override
	public void write(Handshake message, DataOutputStream dataOutputStream) throws IOException {
		// Just write the player id. 
		dataOutputStream.writeUTF(message.getPlayerId());
	}

	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.HANDSHAKE;
	}
}
