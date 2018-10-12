package gaia.client.gamestate;

import java.util.ArrayList;
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
	 * The slots that this container is composed of.
	 */
	private ArrayList<Slot> slots = new ArrayList<Slot>();
	
	/**
	 * Create a new instance of the Container class.
	 * @param type The container type.
	 * @param category The container category.
	 * @param numberOfSlots The number of slots in the container.
	 */
	public Container(ContainerType type, ContainerCategory category, int numberOfSlots) {
		this.type     = type;
		this.category = category;
		// Populate the container with empty slots.
		for (int slotCount = 0; slotCount < numberOfSlots; slotCount++) {
			slots.add(new Slot());
		}
	}
	
	/**
	 * Gets the item type held in the slot defined by the index.
	 * @param index The index of the slot to check.
	 * @return The item type held in the slot defined by the index.
	 */
	@Override
	public ItemType get(int index) {
		return this.slots.get(index).get();
	}
	
	/**
	 * Sets the item type held in the slot defined by the index.
	 * @param type The type of the item.
	 * @param index The index of the slot to check.
	 */
	public void set(ItemType type, int index) {
		this.slots.get(index).set(type);
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
		return this.slots.size();
	}
	
	/**
	 * Gets whether the container is empty.
	 * @return Whether the container is empty.
	 */
	public boolean isEmpty() {
		// Check whether any item types other than NONE are held in this container.
		for (Slot slot : slots) {
			if (slot.get() != ItemType.NONE) {
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
		for (Slot slot : slots) {
			if (slot.get() == ItemType.NONE) {
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
		if (index < 0 || index >= slots.size()) {
			return false;
		}
		// Get the slot at this index.
		Slot slot = slots.get(index);
		// Return whether the slot is free.
		return slot.get() == ItemType.NONE;
	}
}
