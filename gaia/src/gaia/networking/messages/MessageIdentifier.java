package gaia.networking.messages;

/**
 * The unique message identifiers.
 */
public class MessageIdentifier {
	
	// Client -> Server
	public static final int HANDSHAKE                          = 0; 
	public static final int MOVE_PLAYER                        = 1;
	public static final int USE_INVENTORY_ITEM                 = 2;
	public static final int OPEN_CONTAINER                     = 3;
	public static final int CLOSE_CONTAINER                    = 4;
	public static final int MOVE_CONTAINER_ITEM_TO_INVENTORY   = 5;
	public static final int MOVE_INVENTORY_ITEM_TO_CONTAINER   = 6;
	
	// Server -> Client
	public static final int JOIN_SUCCESS                       = 100;
	public static final int JOIN_FAIL                          = 101;
	public static final int PLAYER_SPAWNED                     = 102;
	public static final int PLAYER_DESPAWNED                   = 103;
	public static final int PLAYER_MOVED                       = 104;
	public static final int PLAYER_BLOCKED                     = 105;
	public static final int INVENTORY_SLOT_CHANGED             = 106;
	public static final int CHUNK_LOADED                       = 107;
	public static final int PLACEMENT_UPDATED                  = 108;
	public static final int PLACEMENT_CREATED                  = 109;
	public static final int PLACEMENT_REMOVED                  = 110;
	public static final int CONTAINER_SLOT_SET                 = 111;
	public static final int CONTAINER_ADDED                    = 112;
	public static final int CONTAINER_REMOVED                  = 113;
}
