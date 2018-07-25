package gaia.networking.marshallers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import gaia.networking.IMessageMarshaller;
import gaia.networking.messages.JoinSuccess;

/**
 * The marshaller responsible for reading/writing JoinSuccess messages.
 */
public class JoinSuccessMarshaller implements IMessageMarshaller<JoinSuccess> {

	@Override
	public JoinSuccess read(DataInputStream dataInputStream) throws IOException {
		return new JoinSuccess();
	}

	@Override
	public void write(JoinSuccess message, DataOutputStream dataOutputStream) throws IOException {}

	@Override
	public int getMessageTypeId() {
		return 1;
	}
}