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
	 * Get whether the specified container matches what the player is familiar with and update differences.
	 * @param container The container we are checking matches this familiarity.
	 * @return Whether the specified container matches what the player is familiar with.
	 */
	public boolean compareAndUpdate(Container container) {
		boolean matches = true;
		// Is there a type mismatch?
		if (this.type != container.getType()) {
			this.type = container.getType();
			matches   = false;
		}
		// Is there a category mismatch?
		if (this.category != container.getCategory()) {
			this.category = container.getCategory();
			matches       = false;
		}
		
		// Is there an items mismatch?
		// TODO What do we do here? We cant just change all the items as we need to know about individual ones.
		
		// Return whether the container matched our familiarity with it.
		return matches;
	}
}
