package gaia.server.world.items.container;

import gaia.world.items.ItemType;
import gaia.world.items.container.ContainerCategory;
import gaia.world.items.container.ContainerType;

/**
 * Exposes the immutable properties of a container.
 */
public interface IContainerDetails {
	
	/**
	 * Gets the container type.
	 * @return The container type.
	 */
	public ContainerType getType();

	/**
	 * Gets the container category.
	 * @return The container category.
	 */
	public ContainerCategory getCategory();
	
	/**
	 * Gets the array of items held in the container.
	 * @return The array of items held in the container.
	 */
	public ItemType[] getItemsHeld();
}
