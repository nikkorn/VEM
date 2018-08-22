package gaia.world.items;

import gaia.Constants;
import gaia.world.items.container.Container;

/**
 * A player inventory.
 */
public class Inventory extends Container {

	/**
	 * Create a new instance of the Inventory class.
	 */
	public Inventory() {
		super(Constants.PLAYER_INVENTORY_SIZE);
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
