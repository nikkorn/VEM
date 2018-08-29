package gaia.server.networking;

import java.util.ArrayList;
import gaia.networking.messages.ChunkLoaded;
import gaia.networking.messages.InventorySlotSet;
import gaia.networking.messages.PackedPlacement;
import gaia.networking.messages.PlacementUpdated;
import gaia.networking.messages.PlayerMoved;
import gaia.networking.messages.PlayerSpawned;
import gaia.server.ServerConsole;
import gaia.server.engine.IWorldEventsHandler;
import gaia.server.engine.WelcomePackage;
import gaia.server.world.placements.IPlacementDetails;
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
	public void onPlayerJoinSuccess(String playerId, WelcomePackage welcomePackage) {
		// Write this out to the console.
		ServerConsole.writeInfo("Player '" + playerId + "' joined!");
		// Let the client proxy manager know that the client was successful in their join attempt.
		this.clientProxyManager.acceptClient(playerId, welcomePackage);
	}

	@Override
	public void onPlayerJoinRejected(String playerId, String reason) {
		// Write this out to the console.
		ServerConsole.writeInfo("Player '" + playerId + "' failed to join! (" + reason + ")");
		// Let the client proxy manager know that the client failed in their join attempt.
		this.clientProxyManager.rejectClient(playerId, reason);
	}

	@Override
	public void onPlayerSpawn(String playerId, int x, int y) {
		// Broadcast the player spawn details.
		this.clientProxyManager.broadcastMessage(new PlayerSpawned(playerId, new Position((short)x, (short)y)));
	}

	@Override
	public void onPlayerPositionChange(String playerId, int x, int y) {
		// Broadcast the player moved details.
		this.clientProxyManager.broadcastMessage(new PlayerMoved(playerId, new Position((short)x, (short)y)));
	}

	@Override
	public void onPlayerInventoryChange(String playerId, ItemType item, int slotIndex) {
		// Let the client know about the update to their inventory.
		this.clientProxyManager.sendMessage(playerId, new InventorySlotSet(item, slotIndex));
	}

	@Override
	public void onChunkLoad(int x, int y, ArrayList<IPlacementDetails> placements, String instigator) {
		// Create a list to hold the packed placements.
		ArrayList<PackedPlacement> packedPlacements = new ArrayList<PackedPlacement>();
		// Add each packed placement to the packed placement list.
		for (IPlacementDetails placement : placements) {
			packedPlacements.add(new PackedPlacement(placement.getX(), placement.getY(), placement.asPackedInt()));
		}
		// Send the chunk load details to the client that instigated the load.
		this.clientProxyManager.sendMessage(instigator, new ChunkLoaded((short)x, (short)y, packedPlacements));
	}

	@Override
	public void onPlacementChange(int x, int y, IPlacementDetails placement) {
		// Broadcast the placement change details.
		this.clientProxyManager.broadcastMessage(new PlacementUpdated(new Position((short)x, (short)y), 12345));
	}
}
