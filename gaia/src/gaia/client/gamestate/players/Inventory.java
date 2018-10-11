package gaia.client.gamestate.players;

import gaia.Constants;
import gaia.client.gamestate.Container;
import gaia.world.items.container.ContainerCategory;
import gaia.world.items.container.ContainerType;

/**
 * The client-side representation of a player inventory.
 */
public class Inventory extends Container implements IInventoryDetails {
	
	/**
	 * Creates a new instance of the Inventory class.
	 */
	public Inventory() {
		super(ContainerType.INVENTORY, ContainerCategory.STATIC, Constants.PLAYER_INVENTORY_SIZE);
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
