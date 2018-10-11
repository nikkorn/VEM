package gaia.client.gamestate.players;

import gaia.world.items.ItemType;

/**
 * An interface to expose immutable inventory details.
 */
public interface IInventoryDetails {
	
	/**
	 * Gets the item type held in the slot defined by the index.
	 * @param index The index of the slot to check.
	 * @return The item type held in the slot defined by the index.
	 */
	public ItemType get(int index);
}
