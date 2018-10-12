package gaia.client.gamestate;

import gaia.world.items.ItemType;
import gaia.world.items.container.ContainerCategory;
import gaia.world.items.container.ContainerType;

/**
 * Represents immutable container details.
 */
public interface IContainerDetails {
	
	/**
	 * Get the container type.
	 * @return The container type.
	 */
	ContainerType getType();

	/**
	 * Get the container category.
	 * @return The container category.
	 */
	ContainerCategory getCategory();
	
	/**
	 * Gets the item type held in the slot defined by the index.
	 * @param index The index of the slot to check.
	 * @return The item type held in the slot defined by the index.
	 */
	ItemType get(int index);
	
	/**
	 * Get the size of this container, as in number of slots.
	 * @return The size of this container, as in number of slots.
	 */
	int getSize();
}
