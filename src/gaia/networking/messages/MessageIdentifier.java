package gaia.networking.messages;

/**
 * The unique message identifiers.
 */
public class MessageIdentifier {
	
	// Client -> Server
	public static final int HANDSHAKE            = 0; 
	public static final int MOVE_PLAYER          = 1;
	public static final int USE_ACTIVE_SLOT      = 2;
	public static final int CHANGE_ACTIVE_SLOT   = 3;
	public static final int OPEN_CONTAINER       = 4;
	public static final int CLOSE_CONTAINER      = 5;
	
	// Server -> Client
	public static final int JOIN_SUCCESS         = 6;
	public static final int JOIN_FAIL            = 7;
	public static final int PLAYER_SPAWNED       = 8;
	public static final int PLAYER_DESPAWNED     = 9;
	public static final int PLAYER_MOVED         = 10;
	public static final int PLAYER_SLOT_CHANGED  = 11;
}
