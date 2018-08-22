package gaia.networking.messages;

import gaia.networking.IMessage;
import gaia.world.items.ItemType;

/**
 * A message sent to the server requesting to use an item in their inventory.
 */
public class UseInventoryItem implements IMessage {
	/**
	 * The index of the inventory slot containing the item the player wishes to use.
	 */
	private int slotIndex;
	/**
	 * The item type that was present in the inventory slot at the time of making the request.
	 */
	private ItemType expected;
	
	/**
	 * Create a new instance of the UseInventoryItem class.
	 * @param slotIndex The index of the inventory slot containing the item the player wishes to use.
	 * @param expected The item type that was present in the inventory slot at the time of making the request.
	 */
	public UseInventoryItem(int slotIndex, ItemType expected) {
		this.slotIndex = slotIndex;
		this.expected  = expected;
	}
	
	/**
	 * Get the index of the inventory slot containing the item the player wishes to use.
	 * @return The index of the inventory slot containing the item the player wishes to use.
	 */
	public int getSlotIndex() {
		return this.slotIndex;
	}
	
	/**
	 * Get the item type that was present in the inventory slot at the time of making the request.
	 * @return The item type that was present in the inventory slot at the time of making the request.
	 */
	public ItemType getExpectedItemType() {
		return this.expected;
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.USE_INVENTORY_ITEM;
	}
}