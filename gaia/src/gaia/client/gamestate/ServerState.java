package gaia.client.gamestate;

import java.util.ArrayList;
import gaia.client.gamestate.players.IPlayersDetails;
import gaia.client.gamestate.players.Player;
import gaia.client.gamestate.players.Players;
import gaia.client.networking.ServerMessageProcessor;
import gaia.networking.MessageQueue;
import gaia.networking.QueuedMessageReader;
import gaia.world.generation.TileGenerator;
import gaia.world.players.PositionedPlayer;

/**
 * Represents a snapshot of the server state.
 */
public class ServerState implements IServerState {
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
     * The static world tiles.
     */
    private Tiles tiles;
    /**
     * The world placements.
     */
    private Placements placements;
    /**
     * The world containers.
     */
    private Containers containers;
    /**
     * The players.
     */
    private Players players;
	
	/**
	 * Create a new instance of the ServerState class.
	 * @param playerId The client's player id.
	 * @param queuedMessageReader The reader of messages sent form the server.
	 * @param worldSeed The world seed.
	 * @param players The players positioned in the world.
	 */
	public ServerState(String playerId, QueuedMessageReader queuedMessageReader, long worldSeed, ArrayList<PositionedPlayer> players) {
		this.queuedMessageReader    = queuedMessageReader;
		this.serverMessageProcessor = new ServerMessageProcessor(this);
		this.tiles                  = Tiles.generate(new TileGenerator(worldSeed));
		this.placements             = new Placements();
		this.worldSeed              = worldSeed;
		// Create the players collection.
		this.players = new Players(playerId);
		// Add each player to the players collection.
		for (PositionedPlayer player : players) {
			this.players.addPlayer(new Player(player.getPlayerId(), player.getPosition(), player.getFacingDirection()));
		}
	}
	
	/**
	 * Get the details of all the players.
	 * @return The details of all the players.
	 */
	public Players getPlayers() {
		return this.players;
	}
	
	/**
	 * Get the players.
	 * @return The players.
	 */
	@Override
	public IPlayersDetails getPlayersDetails() {
		return this.players;
	}
	
	/**
	 * Get the tiles.
	 * @return The tiles.
	 */
	@Override
	public Tiles getTiles() {
		return this.tiles;
	}
	
	/**
	 * Get the placements.
	 * @return The placements.
	 */
	@Override
	public Placements getPlacements() {
		return this.placements;
	}
	
	/**
	 * Get the containers.
	 * @return The containers.
	 */
	@Override
	public Containers getContainers() {
		return this.containers;
	}
	
	/**
	 * Get the world seed.
	 * @return the world seed.
	 */
	@Override
	public long getWorldSeed() {
		return this.worldSeed;
	}
    
    /**
     * Gets whether this server state snapshot is stale.
     * @return Whether this server state snapshot is stale.
     */
	@Override
    public boolean isStale() {
    	// If we have unprocessed messages form the server then we can regard this state as stale.
    	return this.queuedMessageReader.hasQueuedMessages();
    }
    
    /**
     * Refresh this state.
     */
    @Override
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
}
