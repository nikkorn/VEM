package gaia.server.networking;

import java.util.ArrayList;
import gaia.networking.messages.InventorySlotSet;
import gaia.networking.messages.PlacementCreated;
import gaia.networking.messages.PlacementRemoved;
import gaia.networking.messages.PlacementUpdated;
import gaia.networking.messages.PlayerMoved;
import gaia.networking.messages.PlayerSpawned;
import gaia.server.ServerConsole;
import gaia.server.engine.IWorldEventsHandler;
import gaia.server.welcomepackage.WelcomePackage;
import gaia.server.world.placements.IPlacementDetails;
import gaia.world.PlacementType;
import gaia.world.Position;
import gaia.world.items.ItemType;

/**
 * Handles world events and forwards any resulting messages to clients.
 */
public class ClientWorldEventsHandler implements IWorldEventsHandler {
	/**
	 * The client proxy manager.
	 */
	private ClientProxyManager clientProxyManager;
	
	/**
	 * Create a new instance of the ClientWorldEventsHandler class.
	 * @param clientProxyManager The client proxy manager.
	 */
	public ClientWorldEventsHandler(ClientProxyManager clientProxyManager) {
		this.clientProxyManager = clientProxyManager;
	}

	@Override
	public void onPlayerJoinSuccess(String clientId, String playerId, WelcomePackage welcomePackage) {
		// Write this out to the console.
		ServerConsole.writeInfo("Client '" + clientId + "' has joined as '" + playerId + "'!");
		// Let the client proxy manager know that the client was successful in their join attempt.
		this.clientProxyManager.acceptClient(clientId, welcomePackage);
	}

	@Override
	public void onPlayerJoinRejected(String clientId, String reason) {
		// Write this out to the console.
		ServerConsole.writeInfo("Client '" + clientId + "' failed to join! (" + reason + ")");
		// Let the client proxy manager know that the client failed in their join attempt.
		this.clientProxyManager.rejectClient(clientId, reason);
	}

	@Override
	public void onPlayerSpawn(String playerId, int x, int y) {
		// Broadcast the player spawn details.
		this.clientProxyManager.broadcastMessage(new PlayerSpawned(playerId, new Position((short)x, (short)y)));
	}

	@Override
	public void onPlayerMove(String playerId, int x, int y, boolean isCorrection) {
		// If this is a correction to be made to a player position then only that player needs to know about it.
		if (isCorrection) {
			this.clientProxyManager.sendMessage(playerId, new PlayerMoved(playerId, new Position((short)x, (short)y), isCorrection));
		} else {
			// Broadcast the player moved details to all players.
			this.clientProxyManager.broadcastMessage(new PlayerMoved(playerId, new Position((short)x, (short)y), isCorrection));
		}
	}

	@Override
	public void onPlayerInventoryChange(String playerId, ItemType item, int slotIndex) {
		// Let the client know about the update to their inventory.
		this.clientProxyManager.sendMessage(playerId, new InventorySlotSet(item, slotIndex));
	}

	@Override
	public void onChunkLoad(int x, int y, ArrayList<IPlacementDetails> placements, String instigator) {}

	@Override
	public void onPlacementCreate(String[] playerIds, int x, int y, IPlacementDetails placement) {
		// Get the created placement position.
		Position position = new Position(x, y);
		// Get the packed placement composition.
		int packedPlacement = placement.asPackedInt();
		// Let each player that cares about this placement creation know about it.
		for (String playerId : playerIds) {
			this.clientProxyManager.sendMessage(playerId, new PlacementCreated(position, packedPlacement));
		}
	}
	
	@Override
	public void onPlacementUpdate(String[] playerIds, int x, int y, IPlacementDetails placement) {
		// Get the placement position.
		Position position = new Position(x, y);
		// Get the packed placement composition.
		int packedPlacement = placement.asPackedInt();
		// Let each player that cares about this placement change know about it.
		for (String playerId : playerIds) {
			this.clientProxyManager.sendMessage(playerId, new PlacementUpdated(position, packedPlacement));
		}
	}

	@Override
	public void onPlacementRemove(String[] playerIds, int x, int y, PlacementType expectedType) {
		// Get the position of the removed placement.
		Position position = new Position(x, y);
		// Let each player that cares about this placement removal know about it.
		for (String playerId : playerIds) {
			this.clientProxyManager.sendMessage(playerId, new PlacementRemoved(position, expectedType));
		}
	}
}
