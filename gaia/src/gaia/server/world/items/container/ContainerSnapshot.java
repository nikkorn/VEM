package gaia.server.world.items.container;

import gaia.world.items.ItemType;
import gaia.world.items.container.ContainerCategory;
import gaia.world.items.container.ContainerType;

/**
 * A snapshot of the state of a container.
 */
public class ContainerSnapshot {
	/**
	 * The container type.
	 */
	private ContainerType type;
	/**
	 * The container category.
	 */
	private ContainerCategory category;
	/**
	 * The items held within the container.
	 */
	private ItemType[] items;
	
	/**
	 * Create a new instance of the ContainerSnapshot class.
	 * @param container The container that this familiarity represents.
	 */
	public ContainerSnapshot(Container container) {
		this.type     = container.getType();
		this.category = container.getCategory();
		this.items    = container.asItemTypeArray();
	}
	
	/**
	 * Gets the container type.
	 * @return The container type.
	 */
	public ContainerType getType() {
		return this.type;
	}

	/**
	 * Gets the container category.
	 * @return The container category.
	 */
	public ContainerCategory getCategory() {
		return this.category;
	}
	
	/**
	 * Gets the array of items held in the container.
	 * @return The array of items held in the container.
	 */
	public ItemType[] getItemsHeld() {
		return this.items;
	}
}
