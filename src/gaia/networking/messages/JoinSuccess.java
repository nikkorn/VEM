package gaia.networking.messages;

import gaia.networking.IMessage;

/**
 * The message sent to the client on a successful join.
 */
public class JoinSuccess implements IMessage {

	@Override
	public int getTypeId() {
		return MessageIdentifier.JOIN_SUCCESS;
	}
}
