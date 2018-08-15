package gaia.client.gamestate;

import gaia.client.networking.ServerMessageProcessor;
import gaia.networking.MessageQueue;
import gaia.networking.QueuedMessageReader;
import gaia.world.TileType;
import gaia.world.generation.TileGenerator;
import java.util.ArrayList;
import gaia.Constants;

/**
 * Represents a snapshot of the server state.
 */
public class ServerState {
	/**
	 * The reader of messages sent form the server.
	 */
	private QueuedMessageReader queuedMessageReader;
	/**
	 * The server message processor.
	 */
	private ServerMessageProcessor serverMessageProcessor;
	/**
	 * The world seed.
	 */
	private long worldSeed;
	/**
     * The immutable world tiles.
     */
    private TileType[][] tiles;
    /**
     * The players.
     */
    private ArrayList<Player> players = new ArrayList<Player>();
	
	/**
	 * Create a new instance of the ServerState class.
	 * @param queuedMessageReader The reader of messages sent form the server.
	 * @param worldSeed The world seed.
	 */
	public ServerState(QueuedMessageReader queuedMessageReader, long worldSeed) {
		this.queuedMessageReader    = queuedMessageReader;
		this.serverMessageProcessor = new ServerMessageProcessor(this);
		this.worldSeed              = worldSeed;
		// Generate the world tiles based on the world seed.
		generateWorldTiles(new TileGenerator(worldSeed));
	}
	
	/**
	 * Get the players.
	 * @return The players.
	 */
	public ArrayList<Player> getPlayers() {
		return this.players;
	}
	
	/**
	 * Get the type of the tile at the x/z position, or null if the position is invalid.
	 * @param x The x position.
	 * @param z The z position.
	 * @return the type of the tile at the x/z position, or null if the position is invalid.
	 */
	public TileType getTileAt(int x, int z) {
		// Calculate the distance between the world centre and the edge.
		int centreToEdge = Constants.WORLD_SIZE / 2;
		try {
			// Return the tile type.
			return this.tiles[x + centreToEdge][z + centreToEdge];
		} catch(IndexOutOfBoundsException e) {
			// There is no tile at this x/z position.
			return null;
		}
	}
	
	/**
	 * Get the world seed.
	 * @return the world seed.
	 */
	public long getWorldSeed() {
		return this.worldSeed;
	}
    
    /**
     * Gets whether this server state snapshot is stale.
     * @return Whether this server state snapshot is stale.
     */
    public boolean isStale() {
    	// If we have unprocessed messages form the server then we can regard this state as stale.
    	return this.queuedMessageReader.hasQueuedMessages();
    }
    
    /**
     * Refresh this state.
     */
    public void refresh() {
    	// There is no point in doing anything if the server has not told us about any changes.
    	if (!this.isStale()) {
    		return;
    	}
    	// We have some messages from the server to process.
    	MessageQueue receivedMessages = this.queuedMessageReader.getQueuedMessages();
    	// Each message should be passed to our server message processor, which will modify the server state accordingly.
    	while (receivedMessages.hasNext()) {
    		serverMessageProcessor.process(receivedMessages.next());
    	}
    }
    
    /**
     * Populate the world tiles array based on the world seed.
     * @param tileGenerator The tile generator.
     */
    private void generateWorldTiles(TileGenerator tileGenerator) {
    	tiles = new TileType[Constants.WORLD_SIZE][Constants.WORLD_SIZE];
    	// Calculate the distance between the world centre and the edge.
		int centreToEdge = Constants.WORLD_SIZE / 2;
		// Generate each static tile and add it to our tiles array.
		for (int x = 0; x < Constants.WORLD_SIZE; x++) {
			for (int z = 0; z < Constants.WORLD_SIZE; z++) {
				tiles[x][z] = tileGenerator.getTileAt(x - centreToEdge, z - centreToEdge);
			}
		}
	}
}
