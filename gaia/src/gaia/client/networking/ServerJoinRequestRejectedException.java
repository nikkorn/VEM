package gaia.client.networking;

/**
 * The exception thrown when a request to join a server is rejected.
 */
public class ServerJoinRequestRejectedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a new instance of the ServerJoinRequestRejectedException class.
	 * Thrown when a request to join a server is rejected.
	 * @param rejectionReason The reason for the rejection.
	 */
	public ServerJoinRequestRejectedException(String rejectionReason) {
		super("Join request rejected: " + rejectionReason);
	}
}
