package gaia.server.world.items.container.types;

import gaia.server.world.items.container.Container;
import gaia.server.world.items.container.ContainerCategory;
import gaia.server.world.items.container.ContainerType;
import gaia.world.items.ItemType;

/**
 * A general container that can be static or a pickup.
 */
public class GeneralContainer extends Container {
	/**
	 * The container category.
	 */
	private ContainerCategory category;

	/**
	 * Creates a new instance of the GeneralContainer class with the specified number of empty slots.
	 * @param numberOfSlots The number of container slots.
	 */
	public GeneralContainer(int numberOfSlots) {
		super(numberOfSlots);
		this.category = ContainerCategory.STATIC;
	}
	
	/**
	 * Creates a new instance of the GeneralContainer class containing the specified items.
	 * @param contains The items that this container will contain.
	 */
	public GeneralContainer(ItemType[] contains) {
		super(contains);
		this.category = ContainerCategory.PICKUP;
	}

	@Override
	public ContainerType getType() {
		return ContainerType.GENERAL;
	}

	@Override
	public ContainerCategory getCategory() {
		return category;
	}
}
