package server.world.container;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import server.items.ItemType;

/**
 * Represents a container consisting of slots.
 */
public class Container {
	/**
	 * The slots that this container is composed of.
	 */
	private ArrayList<Slot> slots = new ArrayList<Slot>(); 
	
	/**
	 * Creates a new instance of the Container class with the specified number of empty slots.
	 * @param numberOfSlots The number of container slots.
	 */
	public Container(int numberOfSlots) {
		// Populate the container with empty slots.
		for (int slotCount = 0; slotCount < numberOfSlots; slotCount++) {
			slots.add(new Slot());
		}
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
	 * Gets the item type held in the slot defined by the index.
	 * @param index The index of the slot to check.
	 * @return The item type held in the slot defined by the index.
	 */
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
	 * Add an item of the specified type to the first free container slot.
	 * @param type The type of item to add.
	 * @throws NoFreeSlotException The expection thrown when attempting to add an item to a full container.
	 */
	public void add(ItemType type) throws NoFreeSlotException {
		// We cannot add an item to a full container.
		if (this.isFull()) {
			throw new NoFreeSlotException(type);
		}
		// Find the first free slot and add our item there.
		for (Slot slot : slots) {
			if (slot.get() == ItemType.NONE) {
				// We found an empty slot!
				slot.set(type);
			}
		}
	}
	
	/**
	 * Clear this container of all items.
	 */
	public void clear() {
		for (Slot slot : slots) {
			slot.set(ItemType.NONE);
		}
	}
	
	/**
	 * Clear the slot at the specified index of its item.
	 */
	public void clear(int index) {
		set(ItemType.NONE, index);
	}
	
	/**
	 * Serialise the container to JSON to be persisted to disk.
	 * @return The serialised container.
	 */
	public JSONObject serialise() {
		// Create the JSON object that will hold the information about this container.
		JSONObject container = new JSONObject();
		// Set the container size.
		container.put("size", this.slots.size());
		// Create the JSON array to hold our slots that hold an item.
		JSONArray slotsArray = new JSONArray();
		// Serialise each slot that holds an item.
		for (Slot slot : slots) {
			if (slot.get() != ItemType.NONE) {
				// Create a JSON object for this slot.
				JSONObject slotJSON = new JSONObject();
				// Set the slot index.
				slotJSON.put("index", slots.indexOf(slot));
				// Set the item type for the slot.
				slotJSON.put("item_type", slot.get().ordinal());
				// Add the slot JSON to our JSON array of slots.
				slotsArray.put(slotJSON);
			}
		}
		// Add the slots JSON array to our container JSON.
		container.put("slots", slotsArray);
		// Return the serialised container.
		return container;
	}
}
