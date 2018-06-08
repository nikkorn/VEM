package server.world.container;

import server.items.ItemType;

/**
 * Represents a container slot.
 * A slot can hold a single item.
 */
public class Slot {
	/*
	 * The item type held in this slot.
	 */
	private ItemType held = ItemType.NONE;
	
	/**
	 * Get the type of item held in this slot.
	 * @return The type of item held in this slot.
	 */
	public ItemType get() {
		return held;
	}
	
	/**
	 * Set the type of item held in this slot.
	 * @param held The type of item held in this slot.
	 */
	public void set(ItemType held) {
		this.held = held;
	}
}
