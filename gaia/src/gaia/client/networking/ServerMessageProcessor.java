package gaia.client.networking;

import gaia.client.gamestate.Placement;
import gaia.client.gamestate.ServerState;
import gaia.networking.IMessage;
import gaia.networking.messages.MessageIdentifier;
import gaia.networking.messages.PackedPlacement;
import gaia.networking.messages.ChunkLoaded;
import gaia.networking.messages.PlayerMoved;
import gaia.networking.messages.PlayerSpawned;
import gaia.world.Position;
import gaia.Constants;

/**
 * Processor of messages sent from the server.
 */
public class ServerMessageProcessor {
	/**
	 * The server state to update in response to processing server messages.
	 */
	private ServerState serverState;
	
	/**
	 * Create a new instance of the ServerMessageProcessor class.
	 * @param serverState The server state to update in response to processing server messages.
	 */
	public ServerMessageProcessor(ServerState serverState) {
		this.serverState = serverState;
	}
	
	/**
	 * Process the message sent from the server.
	 * @param message The message to process.
	 */
	public void process(IMessage message) {
		// How we process this message depends on its type.
		switch (message.getTypeId()) {
		
			case MessageIdentifier.PLAYER_SPAWNED:
				addPlayer(((PlayerSpawned)message).getPlayerId(), ((PlayerSpawned)message).getSpawnPosition());
				break;
		
			case MessageIdentifier.PLAYER_MOVED:
				updatePlayerPosition(((PlayerMoved)message).getPlayerId(), ((PlayerMoved)message).getNewPosition());
				break;
				
			case MessageIdentifier.CHUNK_LOADED:
				ChunkLoaded chunkLoadedMessage = ((ChunkLoaded)message);
				// Process this chunk load as individual placement loads.
				for (PackedPlacement packedPlacement : chunkLoadedMessage.getPackedPlacements()) {
					// Determine the absolute x/y placement position.
					short placementX = (short) ((chunkLoadedMessage.getX() * Constants.WORLD_CHUNK_SIZE) + packedPlacement.getX());
					short placementY = (short) ((chunkLoadedMessage.getX() * Constants.WORLD_CHUNK_SIZE) + packedPlacement.getX());
					// Create the client-side representation of the placement.
					Placement placement = Placement.fromPackedInt(packedPlacement.getComposition());
					// Handle the placement load.
					onPlacementLoad(placement, new Position(placementX, placementY));
				}
				break;
	
			default:
				throw new RuntimeException("error: cannot process message with id '" + message.getTypeId() + "'.");
		}
	}
	
	/**
	 * Add a newly spawned player.
	 * @param playerId The id of the spawning player.
	 * @param position The positon of the spawning player.
	 */
	private void addPlayer(String playerId, Position position) {
		// TODO Handle properly.
		System.out.println("player '" + playerId + "' has spawned at X:" + position.getX() + " Y:" + position.getY());
	}
	
	/**
	 * Update the position of a player.
	 * @param playerId The id of the player.
	 * @param position The positon of the player.
	 */
	private void updatePlayerPosition(String playerId, Position position) {
		// TODO Handle properly.
		System.out.println("player '" + playerId + "' has moved to X:" + position.getX() + " Y:" + position.getY());
	}
	
	/**
	 * Called in response to a placement load.
	 */
	private void onPlacementLoad(Placement placement, Position position) {
		// TODO Handle properly.
		System.out.println("placement of type '" + placement.getType().toString() + "' loaded (x:" + position.getX() + " y:" + position.getY() + ")");
		
	}
}
