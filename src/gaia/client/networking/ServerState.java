package gaia.client.networking;

import gaia.client.players.Player;
import gaia.networking.MessageQueue;
import gaia.networking.QueuedMessageReader;
import java.util.ArrayList;

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
     * The players.
     */
    private ArrayList<Player> players = new ArrayList<Player>();
	
	/**
	 * Create a new instance of the ServerState class.
	 * @param queuedMessageReader The reader of messages sent form the server.
	 */
	public ServerState(QueuedMessageReader queuedMessageReader) {
		this.queuedMessageReader    = queuedMessageReader;
		this.serverMessageProcessor = new ServerMessageProcessor(this);
	}
	
	/**
	 * Get the players.
	 * @return The players.
	 */
	public ArrayList<Player> getPlayers() {
		return this.players;
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
}
