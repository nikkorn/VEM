package gaia.networking.messages;

import gaia.networking.IMessage;
import gaia.world.items.ItemType;

/**
 * A message sent to the server requesting to swap an item in the player inventory with an item in an accessible contianer.
 */
public class SwapContainerAndInventoryItem implements IMessage  {
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
	 * Create a new instance of the SwapContainerAndInventoryItem class.
	 * @param containerSlotIndex The index of the container slot containing the item the player wishes to move.
	 * @param expectedContainerItem The item that was in the container slot at the time of making the request.
	 * @param inventorySlotIndex The index of the inventory slot that the player wishes to move the item to.
	 * @param expectedInventoryItem The item that was in the inventory slot at the time of making the request.
	 */
	public SwapContainerAndInventoryItem(int containerSlotIndex, ItemType expectedContainerItem, int inventorySlotIndex, ItemType expectedInventoryItem) {
		this.containerSlotIndex    = containerSlotIndex;
		this.expectedContainerItem = expectedContainerItem;
		this.inventorySlotIndex    = inventorySlotIndex;
		this.expectedInventoryItem = expectedInventoryItem;
	}
	
	/**
	 * Get the index of the container slot that the player wishes to swap the inventory item to.
	 * @return The index of the container slot that the player wishes to swap the inventory item to.
	 */
	public int getContainerSlotIndex() {
		return this.containerSlotIndex;
	}
	
	/**
	 * Get the item that was in the container slot at the time of making the request.
	 * @return The item that was in the container slot at the time of making the request.
	 */
	public ItemType getExpectedContainerItem() {
		return this.expectedContainerItem;
	}
	
	/**
	 * Get the index of the inventory slot that the player wishes to swap the container item to.
	 * @return The index of the inventory slot that the player wishes to swap the container item to.
	 */
	public int getInventorySlotIndex() {
		return this.inventorySlotIndex;
	}
	
	/**
	 * Get the item that was in the inventory slot at the time of making the request.
	 * @return The item that was in the inventory slot at the time of making the request.
	 */
	public ItemType getExpectedInventoryItem() {
		return this.expectedInventoryItem;
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.SWAP_CONTAINER_AND_INVENTORY_ITEM;
	}
}
