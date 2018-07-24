package gaia.networking.messages;

import gaia.networking.IMessage;

/**
 * The message sent to the client on a failed join.
 */
public class JoinFailure implements IMessage {
	/**
	 * The reason for the failure.
	 */
	private String reason;
	
	/**
	 * Create a new instance of the JoinFailure class.
	 * @param reason The reason for the failure.
	 */
	public JoinFailure(String reason) {
		this.reason = reason;
	}
	
	/**
	 * Get the reason for the failure.
	 * @return The reason for the failure.
	 */
	public String getReason() {
		return this.reason;
	}

	@Override
	public int getTypeId() {
		return 2;
	}
}
