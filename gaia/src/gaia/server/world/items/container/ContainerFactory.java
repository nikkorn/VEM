package gaia.server.world.items.container;

import org.json.JSONArray;
import gaia.server.world.items.container.types.GeneralContainer;
import gaia.world.items.ItemType;
import gaia.world.items.container.ContainerType;

/**
 * Factory for creating Container instances.
 */
public class ContainerFactory {
	
	/**
	 * Create a general static container with the specified number of slots.
	 * @param numberOfSlots The number of slots.
	 * @return The container.
	 */
	public static Container create(int numberOfSlots) {
		return new GeneralContainer(numberOfSlots);
	}
	
	/**
	 * Create a general pickup container containing the specified items.
	 * @param contains The items that the container will hold.
	 * @return The container.
	 */
	public static Container create(ItemType[] contains) {
		return new GeneralContainer(contains);
	}
	
	/**
	 * Create a container based on existing state.
	 * @param containerJSON The JSON array representing the container slots.
	 * @return The container.
	 */
	public static Container create(JSONArray containerJSON) {
		
		// TODO Container should become a JSON object on which we have the container categroy and slots array.

		// Get the number of slots that this container has.
		//int numberOfSlots = containerJSON.length();
		// Create the container with the correct number of slots.
		//Container container = new Container(numberOfSlots);
		// Populate the container slot for each entry in our JSON array.
		//for (int slotIndex = 0; slotIndex < numberOfSlots; slotIndex++) {
			// Grab the item type that the slot holds at the current index.
			//ItemType slotItemType = ItemType.values()[containerJSON.getInt(slotIndex)];
			// Set the item in the appropriate slot.
			//container.set(slotItemType, slotIndex);
		//}
		// Return the container.
		//return container;
		return null;
	}
	
	/**
	 * Create a container based on a container type.
	 * @param containerType The container type.
	 * @return The container.
	 */
	public static Container create(ContainerType containerType) {
		// TODO Add switch for types.
		return null;
	}
}
