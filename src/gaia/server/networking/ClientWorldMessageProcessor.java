package gaia.server.networking;

import gaia.Position;
import gaia.networking.messages.PlayerSpawned;
import gaia.server.world.messaging.IWorldMessageProcessor;
import gaia.server.world.messaging.messages.IWorldMessage;
import gaia.server.world.messaging.messages.PlayerSpawnedMessage;

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
			case PLAYER_DESPAWN:
				break;
			case PLAYER_DIRECTION_CHANGED:
				break;
			case PLAYER_INVENTORY_SLOT_CHANGED:
				break;
			case PLAYER_POSITION_CHANGED:
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
