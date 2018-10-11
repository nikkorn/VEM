package gaia.client.gamestate.players;

import java.util.ArrayList;
import gaia.Constants;
import gaia.world.items.ItemType;
import gaia.world.items.container.Slot;

/**
 * The client-side representation of a player inventory.
 */
public class Inventory implements IInventoryDetails {
	/**
	 * The slots that this container is composed of.
	 */
	private ArrayList<Slot> slots = new ArrayList<Slot>();
	
	/**
	 * Creates a new instance of the Inventory class.
	 */
	public Inventory() {
		// Populate the inventory with empty slots.
		for (int slotCount = 0; slotCount < Constants.PLAYER_INVENTORY_SIZE; slotCount++) {
			slots.add(new Slot());
		}
	}
	
	/**
	 * Gets the item type held in the slot defined by the index.
	 * @param index The index of the slot to check.
	 * @return The item type held in the slot defined by the index.
	 */
	public ItemType get(int index) {
		return this.slots.get(index).get();
	}
	
	/**
	 * Sets the item type held in the slot defined by the index.
	 * @param type The type of the item.
	 * @param index The index of the slot to check.
	 */
	public void set(ItemType type, int index) {
		this.slots.get(index).set(type);
	}
	
	/**
	 * Get whether the specified inventory slot index is a valid one.
	 * @param slotIndex The inventory slot index.
	 * @return Whether the specified inventory slot index is a valid one.
	 */
	public static boolean isValidSlotIndex(int slotIndex) {
		return slotIndex >= 0 && slotIndex < Constants.PLAYER_INVENTORY_SIZE;
	}
}
