package gaia.client.gamestate;

import java.util.Arrays;
import gaia.world.items.ItemType;
import gaia.world.items.container.*;

/**
 * A client-side representation of a container.
 */
public class Container implements IContainerDetails {
	/**
	 * The container type.
	 */
	private ContainerType type;
	/**
	 * The container category.
	 */
	private ContainerCategory category;
	/**
	 * The items that are held in this container.
	 */
	private ItemType[] items;
	
	/**
	 * Create a new instance of the Container class.
	 * @param type The container type.
	 * @param category The container category.
	 * @param items The items held in the container.
	 */
	public Container(ContainerType type, ContainerCategory category, ItemType[] items) {
		this.type     = type;
		this.category = category;
		this.items    = items;
	}
	
	/**
	 * Create a new instance of the Container class.
	 * @param type The container type.
	 * @param category The container category.
	 * @param slots The number of empty slots that the container should have.
	 */
	public Container(ContainerType type, ContainerCategory category, int numberOfSlots) {
		this(type, category, new ItemType[numberOfSlots]);
		// Set each item in the container to be of the NONE type.
		Arrays.fill(this.items, ItemType.NONE);
	}
	
	/**
	 * Gets the item type held in the slot defined by the index.
	 * @param index The index of the slot to check.
	 * @return The item type held in the slot defined by the index.
	 */
	@Override
	public ItemType get(int index) {
		return this.items[index];
	}
	
	/**
	 * Sets the item type held in the slot defined by the index.
	 * @param index The index of the slot.
	 * @param item The item to set as being held at the specified slot.
	 */
	public void set(int index, ItemType item) {
		this.items[index] = item;
	}
	
	/**
	 * Gets the container type.
	 * @return The container type.
	 */
	@Override
	public ContainerType getType() {
		return type;
	}

	/**
	 * Gets the container category.
	 * @return The container category.
	 */
	@Override
	public ContainerCategory getCategory() {
		return category;
	}
	
	/**
	 * Get the size of this container, as in number of slots.
	 * @return The size of this container, as in number of slots.
	 */
	@Override
	public int getSize() {
		return this.items.length;
	}
	
	/**
	 * Gets whether the container is empty.
	 * @return Whether the container is empty.
	 */
	public boolean isEmpty() {
		// Check whether any item types other than NONE are held in this container.
		for (ItemType item : this.items) {
			if (!item.isNothing()) {
				// We found a slot holding an item.
				return false;
			}
		}
		// No slots in the container held the NONE item type.
		return true;
	}
	
	/**
	 * Gets whether the container is full.
	 * @return Whether the container is full.
	 */
	public boolean isFull() {
		// Check whether any slots in this container are not holding an item.
		for (ItemType item : this.items) {
			if (item.isNothing()) {
				// We found an empty slot.
				return false;
			}
		}
		// All slots in this container are populated.
		return true;
	}
	
	/**
	 * Gets whether the slot at the specified position is free.
	 * @param index The index of the slot to check.
	 * @return Whether the slot at the specified position is free.
	 */
	public boolean isSlotFree(int index) {
		// Is this a valid index?
		if (index < 0 || index >= getSize()) {
			return false;
		}
		// Get the item at this index.
		ItemType item = this.items[index];
		// Return whether the item is free.
		return item.isNothing();
	}
}
