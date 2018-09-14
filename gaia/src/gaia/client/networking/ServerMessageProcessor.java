package gaia.client.networking;

import gaia.client.gamestate.Placement;
import gaia.client.gamestate.ServerState;
import gaia.client.gamestate.players.Player;
import gaia.client.gamestate.players.WalkTransition;
import gaia.networking.IMessage;
import gaia.networking.messages.*;
import gaia.world.Direction;
import gaia.world.PlacementType;
import gaia.world.Position;
import gaia.world.items.ItemType;

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
		
			case MessageIdentifier.PLACEMENT_CREATED:
				// Get the newly created placement.
				Placement created = Placement.fromPackedInt(((PlacementCreated)message).getComposition());
				// Get the position of the created placement.
				Position createdPlacementPosition = ((PlacementCreated)message).getPosition();
				// Handle the placement creation.
				onPlacementCreate(created, createdPlacementPosition);
				break;
				
			case MessageIdentifier.PLACEMENT_UPDATED:
				// Get the updated version of the placement.
				Placement updated = Placement.fromPackedInt(((PlacementUpdated)message).getComposition());
				// Get the position of the placement.
				Position updatedPlacementPosition = ((PlacementUpdated)message).getPosition();
				// Handle the placement update.
				onPlacementUpdate(updated, updatedPlacementPosition);
				break;
				
			case MessageIdentifier.PLACEMENT_REMOVED:
				// Get the expected type of the removed placement.
				PlacementType removedType = ((PlacementRemoved)message).getExpectedPlacementType();
				// Get the position of the removed placement.
				Position removedPlacementPosition = ((PlacementRemoved)message).getPosition();
				// Handle the placement removal.
				onPlacementRemoved(removedType, removedPlacementPosition);
				break;
			
			case MessageIdentifier.INVENTORY_SLOT_CHANGED:
				setInventorySlot(((InventorySlotSet)message).getItemType(), ((InventorySlotSet)message).getSlotIndex());
				break;
			
			case MessageIdentifier.PLAYER_SPAWNED:
				addPlayer(((PlayerSpawned)message).getPlayerId(), ((PlayerSpawned)message).getSpawnPosition());
				break;
		
			case MessageIdentifier.PLAYER_MOVED:
				// Determine whether this message represents a corection for the client's player's position, or just a movement update.
				if (((PlayerMoved)message).isCorrection()) {
					// This message was sent by the server to correct the position of the client's player's position.
					correctPlayerPosition(((PlayerMoved)message).getTargetPosition());
				} else {
					// This is just an update to a players position, sent when any client requests to move and the server approves the move.
					updatePlayerPosition(((PlayerMoved)message).getPlayerId(), ((PlayerMoved)message).getTargetPosition());
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
		this.serverState.getPlayers().addPlayer(new Player(playerId, position, Direction.DOWN));
	}
	
	/**
	 * Update the position of a player.
	 * @param playerId The id of the player.
	 * @param position The position of the player.
	 */
	private void updatePlayerPosition(String playerId, Position position) {
		// Get the player that has moved.
		Player player = this.serverState.getPlayers().getPlayer(playerId);
		// Get the current position of the player.
		Position currentPosition = player.getPosition();
		// If the player that has changed position is not the client's player then we need to give them a walk transition.
		if (!this.serverState.getPlayers().getClientsPlayer().getPlayerId().equals(player.getPlayerId())) {
			// Determine the facing direction of the player, based on the route to the position they have moved to.
			Direction facingDirection = Direction.DOWN;
			if (position.getX() > currentPosition.getX()) {
				facingDirection = Direction.RIGHT;
			} else if (position.getX() < currentPosition.getX()) {
				facingDirection = Direction.LEFT;
			} else if (position.getY() > currentPosition.getY()) {
				facingDirection = Direction.UP;
			} else if (position.getY() < currentPosition.getY()) {
				facingDirection = Direction.DOWN;
			} 
			// Set the facing direction for this player.
			player.setFacingDirection(facingDirection);
			// Create a walk transition for this player.
			player.setWalkingTransition(WalkTransition.begin(currentPosition.copy(), position));
		}
		// Update the player position.
		player.getPosition().setX(position.getX());
		player.getPosition().setY(position.getY());
	}
	
	/**
	 * Correct the position of the client's player.
	 * @param position The correct position of the player.
	 */
	private void correctPlayerPosition(Position position) {
		// Get the client's player.
		Player clientsPlayer = this.serverState.getPlayers().getClientsPlayer();
		// Correct the player's position. This means moving them straight to the tile that the server
		// says the player is positioned at. The direction the player is facing should stay the same.
		clientsPlayer.moveTo(position.getX(), position.getY(), clientsPlayer.getFacingDirection());
	}
	
	/**
	 * Called in response to a placement creation.
	 * @param placement The newly created placement.
	 * @param position The placement position.
	 */
	private void onPlacementCreate(Placement created, Position position) {
		// Update the server state to reflect the placement creation.
		this.serverState.getPlacements().add(created, position.getX(), position.getY());
	}
	
	/**
	 * Called in response to a placement update.
	 * @param updated The updated placement.
	 * @param position The placement position.
	 */
	private void onPlacementUpdate(Placement updated, Position position) {
		// Get the existing placement at the specified position.
		Placement existing = this.serverState.getPlacements().getPlacementAt(position.getX(), position.getY());
		// We only want to update the placement at this position if it exists and it shares a placement type with the updated one.
		if (existing != null && existing.getType() == updated.getType()) {
			// Update the underlay of the existing placement.
			existing.setUnderlay(updated.getUnderlay());
			// Update the overlay of the existing placement.
			existing.setOverlay(updated.getOverlay());
		}
	}

	/**
	 * Called in response to a placement removal.
	 * @param removedType The type of the removed placement.
	 * @param position The position of the removed placement.
	 */
	private void onPlacementRemoved(PlacementType removedType, Position position) {
		// Get the existing placement at the specified position.
		Placement existing = this.serverState.getPlacements().getPlacementAt(position.getX(), position.getY());
		// If there is no placement here or the types of the placement do not match then there is nothing to do.
		if (existing == null || existing.getType() != removedType) {
			return;
		}
		// There is a placement of the expected type at the position where the placement was removed from, so remove it.
		this.serverState.getPlacements().remove(position.getX(), position.getY());
	}
}
