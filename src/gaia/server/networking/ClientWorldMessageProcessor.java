package gaia.server.networking;

import gaia.networking.messages.PlayerMoved;
import gaia.networking.messages.PlayerSpawned;
import gaia.server.ServerConsole;
import gaia.server.world.messaging.IWorldMessageProcessor;
import gaia.server.world.messaging.messages.IWorldMessage;
import gaia.server.world.messaging.messages.PlayerJoinAcceptedMessage;
import gaia.server.world.messaging.messages.PlayerJoinRejectedMessage;
import gaia.server.world.messaging.messages.PlayerPositionChangedMessage;
import gaia.server.world.messaging.messages.PlayerSpawnedMessage;
import gaia.world.Position;

/**
 * Processes world messages.
 * These may be forwarded to connected clients.
 */
public class ClientWorldMessageProcessor implements IWorldMessageProcessor {
	/**
	 * The client proxy manager.
	 */
	private ClientProxyManager clientProxyManager;
	
	/**
	 * Create a new instance of the ClientWorldMessageProcessor class.
	 * @param clientProxyManager The client proxy manager.
	 */
	public ClientWorldMessageProcessor(ClientProxyManager clientProxyManager) {
		this.clientProxyManager = clientProxyManager;
	}

	/**
	 * Process the world message.
	 * @param message The message to process.
	 */
	@Override
	public void process(IWorldMessage message) {
		// How we handle this world message depends on its type.
		switch (message.getMessageType()) {
			case CHUNK_LOADED:
				break;
			case CHUNK_WEATHER_CHANGED:
				break;
			case CONTAINER_CLOSED:
				break;
			case CONTAINER_OPENED:
				break;
			case CONTAINER_SLOT_CHANGED:
				break;
			case PLACEMENT_ADDED:
				break;
			case PLACEMENT_OVERLAY_CHANGED:
				break;
			case PLACEMENT_REMOVED:
				break;
			case PLACEMENT_UNDERLAY_CHANGED:
				break;
			case PLAYER_JOIN_SUCCESS:
				// A player has joined successfully!
				PlayerJoinAcceptedMessage joinAcceptedMessage = (PlayerJoinAcceptedMessage)message;
				// Write this out to the console.
				ServerConsole.writeInfo("Player '" + joinAcceptedMessage.getPlayerId() + "' joined!");
				// Let the client proxy manager know that the client was successful in their join attempt.
				this.clientProxyManager.acceptClient(joinAcceptedMessage.getClientId(), joinAcceptedMessage.getWelcomePackage());
				break;
			case PLAYER_JOIN_REJECTED:
				// A player was rejected in attempting to join!
				PlayerJoinRejectedMessage joinRejectedMessage = (PlayerJoinRejectedMessage)message;
				// Write this out to the console.
				ServerConsole.writeInfo("Player '" + joinRejectedMessage.getPlayerId() + "' failed to join! (" + joinRejectedMessage.getReason() + ")");
				// Let the client proxy manager know that the client failed in their join attempt.
				this.clientProxyManager.rejectClient(joinRejectedMessage.getClientId(), joinRejectedMessage.getReason());
				break;
			case PLAYER_DESPAWN:
				break;
			case PLAYER_DIRECTION_CHANGED:
				break;
			case PLAYER_INVENTORY_SLOT_CHANGED:
				break;
			case PLAYER_POSITION_CHANGED:
				// Get the id of the moving player.
				String movingPlayerId = ((PlayerPositionChangedMessage)message).getPlayerId();
				// Get the new positino of the player.
				Position newPosition = ((PlayerPositionChangedMessage)message).getNewPosition();
				// Broadcast the player moved detials.
				this.clientProxyManager.broadcastMessage(new PlayerMoved(movingPlayerId, newPosition));
				break;
			case PLAYER_SPAWN:
				// Get the id of the spawning player.
				String spawnedPlayerId = ((PlayerSpawnedMessage)message).getPlayerId();
				// Get the position of the spawning player.
				Position spawnPosition = ((PlayerSpawnedMessage)message).getSpawnPosition();
				// Broadcast the player spawn details.
				this.clientProxyManager.broadcastMessage(new PlayerSpawned(spawnedPlayerId, spawnPosition));
				break;
			case UNKNOWN:
			default:
				break;
		}
	}
}
