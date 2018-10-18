package gaia.networking.messages;

/**
 * The unique message identifiers.
 */
public class MessageIdentifier {
	
	// Client -> Server
	public static final int HANDSHAKE                = 0; 
	public static final int MOVE_PLAYER              = 1;
	public static final int USE_INVENTORY_ITEM       = 2;
	public static final int OPEN_CONTAINER           = 3;
	public static final int CLOSE_CONTAINER          = 4;
	
	// Server -> Client
	public static final int JOIN_SUCCESS             = 5;
	public static final int JOIN_FAIL                = 6;
	public static final int PLAYER_SPAWNED           = 7;
	public static final int PLAYER_DESPAWNED         = 8;
	public static final int PLAYER_MOVED             = 9;
	public static final int PLAYER_BLOCKED           = 10;
	public static final int INVENTORY_SLOT_CHANGED   = 11;
	public static final int CHUNK_LOADED             = 12;
	public static final int PLACEMENT_UPDATED        = 13;
	public static final int PLACEMENT_CREATED        = 14;
	public static final int PLACEMENT_REMOVED        = 15;
	public static final int CONTAINER_SLOT_SET       = 16;
	public static final int CONTAINER_ADDED          = 17;
	public static final int CONTAINER_REMOVED        = 18;
}
