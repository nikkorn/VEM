package gaia.server.world.players.requests;

import gaia.server.ServerConsole;
import gaia.server.welcomepackage.WelcomePackage;
import gaia.server.welcomepackage.WelcomePackageFactory;
import gaia.server.world.World;
import gaia.server.world.messaging.messages.InventorySlotSetMessage;
import gaia.server.world.messaging.messages.PlayerJoinAcceptedMessage;
import gaia.server.world.messaging.messages.PlayerJoinRejectedMessage;
import gaia.server.world.messaging.messages.PlayerSpawnedMessage;
import gaia.server.world.players.Player;
import gaia.server.world.players.PlayerJoinRequestResult;
import gaia.world.Position;
import gaia.world.items.ItemType;

/**
 * A request to join a world.
 */
public class JoinRequest extends PlayerRequest {
	/**
	 * The id of the client.
	 */
	private String clientId;

	/**
	 * Create a new instance of the JoinRequest class.
	 * @param playerId The id of the player requesting to join.
	 * @param clientId The id of the client.
	 */
	public JoinRequest(String playerId, String clientId) {
		super(playerId);
		this.clientId = clientId;
	}

	@Override
	public void satisfy(World world) {
		ServerConsole.writeInfo("Client '" + clientId + "' is attempting to join...");
		// Attempt to add the player to the world.
		PlayerJoinRequestResult result = world.getPlayers().addPlayer(this.getRequestingPlayerId(), world);
		// Handle the result of attempting to join.
		switch (result) {
			case ALREADY_JOINED:
				// Add a world message to notify of the failure the player had in joining.
				world.getWorldMessageQueue().add(new PlayerJoinRejectedMessage(this.getRequestingPlayerId(), this.clientId, "This player has already joined!"));
				break;
			case BLACKLISTED:
				// Add a world message to notify of the failure the player had in joining.
				world.getWorldMessageQueue().add(new PlayerJoinRejectedMessage(this.getRequestingPlayerId(), this.clientId, "This player is blacklisted!"));
				break;
			case SUCCESS:
				// The player was able to join!
				Player player = world.getPlayers().getPlayer(this.getRequestingPlayerId());
				// Create a welcome package for the newly connected client.
				WelcomePackage welcomePackage = WelcomePackageFactory.create(world);
				// Add a world message to notify of the success the player had in joining.
				world.getWorldMessageQueue().add(new PlayerJoinAcceptedMessage(this.getRequestingPlayerId(), this.clientId, welcomePackage));
				// Add a world message to notify every player about this player spawn.
				world.getWorldMessageQueue().add(new PlayerSpawnedMessage(this.getRequestingPlayerId(), new Position(player.getPosition().getX(), player.getPosition().getY())));
				// Add a world message for each item in the player inventorythat is not NONE.
				for (int slotIndex = 0; slotIndex < player.getInventory().size(); slotIndex++) {
					// Get the item at the current slot index.
					ItemType item = player.getInventory().get(slotIndex);
					// We only care about items that are not of the NONE type.
					if (item != ItemType.NONE) {
						world.getWorldMessageQueue().add(new InventorySlotSetMessage(this.getRequestingPlayerId(), item, slotIndex));
					}
				}
				// Let the world handle the player spawn.
				world.onPlayerSpawn(player);
				break;
			default:
				throw new RuntimeException("Unexpected PlayerJoinRequestResult value: " + result.toString());
		}
	}
}
