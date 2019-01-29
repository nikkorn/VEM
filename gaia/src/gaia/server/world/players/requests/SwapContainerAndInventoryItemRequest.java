package gaia.server.world.players.requests;

import gaia.server.ServerConsole;
import gaia.server.world.World;
import gaia.world.items.ItemType;

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
		// TODO Check that a container exists in front of the player.
		// TODO Check that the expected item types are valid for the container type and the swap can be done.
		// TODO Set the inventory slot of the player to the container item type.
		// TODO Set the container slot of the accessible container to be the inventory item type.
		ServerConsole.writeDebug("Player (" + this.getRequestingPlayerId() + " wanted to swap their " + expectedInventoryItem +  " for a " + expectedContainerItem + "!");
	}
}
