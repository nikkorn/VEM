package gaia.client.networking;

import java.io.IOException;
import gaia.client.gamestate.Placement;
import gaia.client.gamestate.ServerState;
import gaia.client.gamestate.players.Inventory;
import gaia.client.gamestate.players.Player;
import gaia.networking.IMessage;
import gaia.networking.MessageOutputStream;
import gaia.networking.messages.SwapContainerAndInventoryItem;
import gaia.networking.messages.MovePlayer;
import gaia.networking.messages.UseInventoryItem;
import gaia.world.Direction;
import gaia.world.Position;
import gaia.world.items.ItemTarget;
import gaia.world.items.ItemType;
import gaia.world.items.container.ContainerCategory;

/**
 * Client actions.
 */
public class ClientActions {
	/**
	 * The message output stream.
	 */
	private MessageOutputStream messageOutputStream;
	/**
	 * The server state.
	 */
	private ServerState serverState;

	/**
	 * Create a new instance of the ClientActions class.
	 * @param messageOutputStream The message output stream.
	 * @param serverState The server state.
	 */
	public ClientActions(MessageOutputStream messageOutputStream, ServerState serverState) {
		this.messageOutputStream = messageOutputStream;
		this.serverState         = serverState;
	}

	/**
	 * Attempt to move the player in the specified direction. The player will not be able to move to this position if it is blocked.
	 * @param direction The direction to move in.
	 */
	public void move(Direction direction) {
		// Refresh the server state to ensure we have the latest info.
		this.serverState.refresh();
		// Get the client's player.
		Player player = serverState.getPlayers().getClientsPlayer();
		// There is nothing to do if the player is already moving.
		if (player.isWalking()) {
			return;
		}
		// Get the position the player is trying to move to.
		Position targetPosition = player.getPosition().getAdjacentPosition(direction);
		// We cannot move to a position that is not valid.
		if (targetPosition == null) {
			return;
		}
		// Try to get the placement that is at the target position.
		Placement placementAtTarget = serverState.getPlacements().getPlacementAt(targetPosition.getX(),
				targetPosition.getY());
		// We cannot move to a position where there is a non-walkable placement in the way of the player.
		if (placementAtTarget != null && !placementAtTarget.isWalkable()) {
			return;
		}
		// Do nothing if there is already another player at this position.
		if (serverState.getPlayers().getPlayerAtPosition(targetPosition) != null) {
			return;
		}
		// Start our player moving right away.
		player.move(direction);
		// Make a request to the server to move in the same direction. Hopefully the server will respond with a
		// 'PlayerMoved' message with the details of the position we are moving to. However, the server may also 
		// return a 'PlayerMoved' with details of a position that is not the position we were targeting. This is 
		// OK though, we will just move the player there instead.
		send(new MovePlayer(direction));
	}

	/**
	 * Use the inventory item at the specified slot.
	 * @param slot The inventory slot index.
	 */
	public void useInventoryItem(int slot) {
		// Get the client's player.
		Player player = serverState.getPlayers().getClientsPlayer();
		// We should not be using inventory items while we are walking.
		if (player.isWalking()) {
			return;
		}
		// Do nothing if the slot index is invalid.
		if (!Inventory.isValidSlotIndex(slot)) {
			throw new RuntimeException("Invalid inventory slot index: " + slot);
		}
		// Refresh the server state to ensure we have the latest inventory slots.
		this.serverState.refresh();
		// Get the item at the specified index.
		ItemType itemType = player.getInventory().get(slot);
		// We do not want to do anything if there is no target of use for the item in the slot.
		if (itemType.getTarget() == ItemTarget.NONE) {
			return;
		}
		// Send a message to the server asking to use the item in the specified slot.
		send(new UseInventoryItem(slot, itemType));
	}

	/**
	 * Move an item from a slot in an accessible container to a slot in the player inventory.
	 * This will also move the inventory item to the container slot as long as the container
	 * category is STATIC or the item being moved to the container is the NONE type, otherwise
	 * the swap will not be carried out as items that are not of the NONE type caoont be placed
	 * back into containers of the PICKUP category type.
	 * 
	 * This does nothing if there is no accessible container for the player to interact with.
	 * 
	 * @param containerSlot The container slot index.
	 * @param inventorySlot The inventory slot index.
	 */
	public void swapContainerAndInventoryItem(int containerSlot, int inventorySlot) {
		// Get the client's player.
		Player player = serverState.getPlayers().getClientsPlayer();
		// We should not be moving inventory items while we are walking.
		if (player.isWalking()) {
			return;
		}
		// Do nothing if the inventory slot index is invalid.
		if (!Inventory.isValidSlotIndex(inventorySlot)) {
			throw new RuntimeException("Invalid inventory slot index: " + inventorySlot);
		}
		// Refresh the server state to ensure we have the latest info.
		this.serverState.refresh();
		// Do nothing if there is no container in front of the player.
		if (serverState.getAccessibleContainer() == null) {
			return;
		}
		// Get the inventory item at the specified index.
		ItemType inventoryItemType = player.getInventory().get(inventorySlot);
		// Get the container item at the specified index.
		ItemType containerItemType = serverState.getAccessibleContainer().get(containerSlot);
		// Do nothing if the accessible container is a PICKUP container and we are trying
		// to move an inventory item that is not the NONE type into a container slot.
		if (serverState.getAccessibleContainer().getCategory() == ContainerCategory.PICKUP && !inventoryItemType.isNothing()) {
			return;
		}
		// Send a message to the server asking to swap an invetory item with a container item.
		send(new SwapContainerAndInventoryItem(containerSlot, containerItemType, inventorySlot, inventoryItemType));
	}

	/**
	 * Send a message to the server.
	 * @param message The message to send
	 */
	private void send(IMessage message) {
		try {
			messageOutputStream.writeMessage(message);
		} catch (IOException e) {
			// TODO Should we do this?
			throw new NotConnectedException();
		}
	}
}
