package gaia.server.world.players.worldfamiliarity;

import gaia.server.world.items.container.Container;
import gaia.world.items.ItemType;
import gaia.world.items.container.ContainerCategory;
import gaia.world.items.container.ContainerType;

/**
 * Represents a players familiarity with a container.
 */
public class ContainerFamiliarity {
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
	 * Create a new instance of the ContainerFamiliarity class.
	 * @param container The container that this familiarity represents.
	 */
	public ContainerFamiliarity(Container container) {
		this.type     = container.getType();
		this.category = container.getCategory();
		this.items    = container.asItemTypeArray();
	}
	
	/**
	 * Get whether the specified container matches what the player is familiar with.
	 * @param container The container we are checking matches this familiarity.
	 * @return Whether the specified container matches what the player is familiar with.
	 */
	public boolean compare(Container container) {
		// Is there a type mismatch?
		if (this.type != container.getType()) {
			return false;
		}
		// Is there a category mismatch?
		if (this.category != container.getCategory()) {
			return false;
		}
		// Does the number of items differ?
		if (container.size() != items.length) {
			return false;
		}
		// Do any of the items differ?
		for (int itemIndex = 0; itemIndex < this.items.length; itemIndex++) {
			if (this.items[itemIndex] != container.get(itemIndex)) {
				return false;
			}
		}
		// The containers match.
		return true;
	}
	
	/**
	 * Update the familiarity to match the specified container.
	 * @param container The container to match.
	 */
	public void update(Container container) {
		this.type     = container.getType();
		this.category = container.getCategory();
		this.items    = container.asItemTypeArray();
	}
}
