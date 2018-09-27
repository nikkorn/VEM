package gaia.world.items.container;

import org.json.JSONArray;
import gaia.world.items.ItemType;

/**
 * Factory for creating Container instances.
 */
public class ContainerFactory {
	
	/**
	 * Create an empty container with the specified number of slots.
	 * @param numberOfSlots The number of slots.
	 * @return The container.
	 */
	public static Container create(int numberOfSlots) {
		return new Container(numberOfSlots);
	}
	
	/**
	 * Create a container based on existing state.
	 * @param containerJSON The JSON array representing the container slots.
	 * @return The container.
	 */
	public static Container create(JSONArray containerJSON) {
		// Get the number of slots that this container has.
		int numberOfSlots = containerJSON.length();
		// Create the container with the correct number of slots.
		Container container = new Container(numberOfSlots);
		// Populate the container slot for each entry in our JSON array.
		for (int slotIndex = 0; slotIndex < numberOfSlots; slotIndex++) {
			// Grab the item type that the slot holds at the current index.
			ItemType slotItemType = ItemType.values()[containerJSON.getInt(slotIndex)];
			// Set the item in the appropriate slot.
			container.set(slotItemType, slotIndex);
		}
		// Return the container.
		return container;
	}
}
