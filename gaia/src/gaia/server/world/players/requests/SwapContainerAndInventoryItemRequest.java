package gaia.server.world.players.requests;

import gaia.server.world.World;
import gaia.server.world.items.container.Container;
import gaia.server.world.messaging.messages.ContainerSlotChangedMessage;
import gaia.server.world.messaging.messages.InventorySlotSetMessage;
import gaia.server.world.players.Player;
import gaia.world.Position;
import gaia.world.items.ItemType;
import gaia.world.items.container.ContainerCategory;
import gaia.world.players.PositionedPlayer;

/**
 * A request to swap an inventory item with an item in the container accessible to the player.
 */
public class SwapContainerAndInventoryItemRequest extends PlayerRequest {
	/**
	 * The index of the container slot that the player wishes to swap the inventory item to.
	 */
	private int containerSlotIndex;
	/**
	 * The item that was in the container slot at the time of making the request.
	 */
	private ItemType expectedContainerItem;
	/**
	 * The index of the inventory slot that the player wishes to swap the container item to.
	 */
	private int inventorySlotIndex;
	/**
	 * The item that was in the inventory slot at the time of making the request.
	 */
	private ItemType expectedInventoryItem;

	/**
	 * Create a new instance of the SwapContainerAndInventoryItemRequest class.
	 * @param playerId The id of the player requesting to do the swap.
	 * @param containerSlotIndex The index of the container slot containing the item the player wishes to move.
	 * @param expectedContainerItem The item that was in the container slot at the time of making the request.
	 * @param inventorySlotIndex The index of the inventory slot that the player wishes to move the item to.
	 * @param expectedInventoryItem The item that was in the inventory slot at the time of making the request.
	 */
	public SwapContainerAndInventoryItemRequest(String playerId, int containerSlotIndex, ItemType expectedContainerItem, int inventorySlotIndex, ItemType expectedInventoryItem) {
		super(playerId);
		this.containerSlotIndex    = containerSlotIndex;
		this.expectedContainerItem = expectedContainerItem;
		this.inventorySlotIndex    = inventorySlotIndex;
		this.expectedInventoryItem = expectedInventoryItem;
	}

	@Override
	public void satisfy(World world) {
		// Get the player.
		Player player = world.getPlayers().getPlayer(this.getRequestingPlayerId());
		
		// Get the position that the player is facing.
		Position facingPosition = PositionedPlayer.getFacingPosition(player.getPosition(), player.getFacingDirection());
		
		// Try to get the container that is in front of the player.
		Container container = world.getContainer(facingPosition);
		
		// There is nothing to do if there is no container in front of the player or there is a
		// container but the item that is in the target container slot is not the expected one.
		if (container == null || container.get(containerSlotIndex) != expectedContainerItem) {
			return;
		}

		// Get the inventory item.
		ItemType inventoryItem = player.getInventory().get(inventorySlotIndex);
		
		// There is nothing to do if the inventory item that is in the target inventory slot is
		// not the expected one, or the item is the expected one but it is not the NONE type and
		// the container category is PICKUP, as items cannot be placed into PICKUP containers.
		if (inventoryItem != expectedInventoryItem || (container.getCategory() == ContainerCategory.PICKUP && !inventoryItem.isNothing())) {
			return;
		}
		
		// Set the inventory slot of the player to the container item type.
		player.getInventory().set(expectedContainerItem, inventorySlotIndex);

		// Add a world message to notify of this change.
		world.getWorldMessageQueue().add(new InventorySlotSetMessage(this.getRequestingPlayerId(), expectedContainerItem, inventorySlotIndex));
		
		// Set the container slot to the inventory slot item type.
		container.set(expectedInventoryItem, containerSlotIndex);

		// Add a world message to notify of this change.
		world.getWorldMessageQueue().add(new ContainerSlotChangedMessage(containerSlotIndex, expectedInventoryItem, facingPosition));
	}
}
