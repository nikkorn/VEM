package gaia.world.items.container;

import java.util.ArrayList;
import gaia.world.items.ItemType;

/**
 * Represents a slot with a specific container index.
 */
public class IndexedSlot extends Slot {
	/*
	 * The index of the slot. 
	 */
	private int index;
	
	/**
	 * Create a new instance of the IndexedSlot class.
	 * The slot will contain the NONE item type.
	 * @param index The index of the slot. 
	 */
	public IndexedSlot(int index) {
		this.index = index;
	}
	
	/**
	 * Create a new instance of the IndexedSlot class.
	 * The slot will contain the specified item.
	 * @param index The index of the slot. 
	 * @param held The item that the slot will hold.
	 */
	public IndexedSlot(int index, ItemType held) {
		this.index = index;
		this.set(held);
	}
	
	/**
	 * Get the index of the slot.
	 * @return The index of the slot.
	 */
	public int getIndex() {
		return this.index;
	}
	
	/**
	 * Create a list of indexed slots where each slot does not hold the NONE item type based on a list of slots.
	 * @param slots The list of slots where some slost may hold the NONE item type.
	 * @return A list of indexed slots where each slot does not hold the NONE item type based on a list of slots.
	 */
	public static ArrayList<IndexedSlot> createIndexedSlotList(ArrayList<Slot> slots) {
		// Create the empty list to hold the indexed slots.
		ArrayList<IndexedSlot> indexedSlots = new ArrayList<IndexedSlot>();
		// Find each slot for item types that are not NONE and add them as an indexed slot.
		for (int slotIndex = 0; slotIndex < slots.size(); slotIndex++) {
			// Is the slot empty?
			if (slots.get(slotIndex).get() == ItemType.NONE) {
				continue;
			}
			// We have an item!
			indexedSlots.add(new IndexedSlot(slotIndex, slots.get(slotIndex).get()));
		}
		// Return the list of indexed slots.
		return indexedSlots;
	}
}
