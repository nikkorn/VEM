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
}
