package gaia.client.networking;

import gaia.client.gamestate.Placement;
import gaia.client.gamestate.Player;
import gaia.client.gamestate.ServerState;
import gaia.networking.IMessage;
import gaia.networking.messages.*;
import gaia.world.Position;
import gaia.world.items.ItemType;
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
		
			case MessageIdentifier.PLACEMENT_UPDATED:
				// Get the updated version of the placement.
				Placement updated = Placement.fromPackedInt(((PlacementUpdated)message).getComposition());
				// Get the position of the placement.
				Position position = ((PlacementUpdated)message).getPosition();
				// Handle the placement update.
				onPlacementUpdate(updated, position);
				break;
			
			case MessageIdentifier.INVENTORY_SLOT_CHANGED:
				setInventorySlot(((InventorySlotSet)message).getItemType(), ((InventorySlotSet)message).getSlotIndex());
				break;
			
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
					short placementY = (short) ((chunkLoadedMessage.getY() * Constants.WORLD_CHUNK_SIZE) + packedPlacement.getY());
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
	 * Set the item type for an inventory slot.
	 * @param itemType The item type.
	 * @param slotIndex The inventory slot index.
	 */
	private void setInventorySlot(ItemType itemType, int slotIndex) {
		this.serverState.getPlayers().getClientsPlayer().getInventory().set(itemType, slotIndex);
	}

	/**
	 * Add a newly spawned player.
	 * @param playerId The id of the spawning player.
	 * @param position The positon of the spawning player.
	 */
	private void addPlayer(String playerId, Position position) {
		this.serverState.getPlayers().addPlayer(new Player(playerId, position));
	}
	
	/**
	 * Update the position of a player.
	 * @param playerId The id of the player.
	 * @param position The positon of the player.
	 */
	private void updatePlayerPosition(String playerId, Position position) {
		// Get the position of the player.
		Position currentPosition = this.serverState.getPlayers().getPlayer(playerId).getPosition();
		// Update the player position.
		currentPosition.setX(position.getX());
		currentPosition.setY(position.getY());
	}
	
	/**
	 * Called in response to a placement load.
	 * @param placement The loaded placement.
	 * @param position The placement position.
	 */
	private void onPlacementLoad(Placement placement, Position position) {
		this.serverState.getPlacements().add(placement, position.getX(), position.getY());
	}
	
	/**
	 * Called in response to a placement update.
	 * @param placement The updated placement.
	 * @param position The placement position.
	 */
	private void onPlacementUpdate(Placement updated, Position position) {
		// Get the exisitng placement at tthe specified position.
		Placement target = this.serverState.getPlacements().getPlacementAt(position.getX(), position.getY());
		// If the placement does not exist or the types do not match then do nothing.
		if (target == null || target.getType() != updated.getType()) {
			return;
		}
		// Update the state of the target placement.
		target.setUnderlay(updated.getUnderlay());
		target.setOverlay(updated.getOverlay());
	}
}
