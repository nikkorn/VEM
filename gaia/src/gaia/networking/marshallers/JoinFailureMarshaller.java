package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.JoinFailure;
import gaia.networking.messages.MessageIdentifier;

/**
 * The marshaller responsible for reading/writing JoinFailure messages.
 */
public class JoinFailureMarshaller implements IMessageMarshaller<JoinFailure> {

	@Override
	public JoinFailure read(DataInputStream dataInputStream) throws IOException {
		return new JoinFailure(dataInputStream.readUTF());
	}

	@Override
	public void write(JoinFailure message, DataOutputStream dataOutputStream) throws IOException {
		// Write the failure reason.
		dataOutputStream.writeUTF(message.getReason());
	}

	@Override
	public int getMessageTypeId() {
		return MessageIdentifier.JOIN_FAIL;
	}
}