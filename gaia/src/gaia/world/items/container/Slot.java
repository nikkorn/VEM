package gaia.world.items.container;

import gaia.world.items.ItemType;

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
	 * Create a new instance of the Slot class.
	 * The slot will contain the NONE item type.
	 */
	public Slot() {}
	
	/**
	 * Create a new instance of the Slot class.
	 * The slot will contain the specified item.
	 * @param held The item that the slot will hold.
	 */
	public Slot(ItemType held) {
		this.set(held);
	}
	
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
