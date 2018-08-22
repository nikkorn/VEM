package gaia.networking.messages;

import gaia.networking.IMessage;
import gaia.world.items.ItemType;

/**
 * A message sent to a client to notify of a player that has one of their inventory slots changed.
 */
public class InventorySlotSet implements IMessage {
	/**
	 * The item type.
	 */
	private ItemType itemType;
	/**
	 * The inventory slot index.
	 */
	private int slotIndex;
	
	/**
	 * Create a new instance of the InventorySlotSet class.
	 * @param itemType The item type.
	 * @param slotIndex The inventory slot index.
	 */
	public InventorySlotSet(ItemType itemType, int slotIndex) {
		this.itemType  = itemType;
		this.slotIndex = slotIndex;
	}

	/**
	 * Get the item type.
	 * @return The item type.
	 */
	public ItemType getItemType() {
		return this.itemType;
	}
	
	/**
	 * Get the inventory slot index.
	 * @return The inventory slot index.
	 */
	public int getSlotIndex() {
		return this.slotIndex;
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.INVENTORY_SLOT_CHANGED;
	}
}
