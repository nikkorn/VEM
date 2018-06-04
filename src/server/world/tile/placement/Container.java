package server.world.tile.placement;

import org.json.JSONObject;
import server.items.ItemType;

/**
 * Represents a placement container.
 */
public class Container {
	
	/**
	 * Gets whether the container is empty.
	 * @return Whether the container is empty.
	 */
	public boolean isEmpty() {
		// TODO Return whether the container is empty.
		return false;
	}
	
	/**
	 * Gets whether the container is full.
	 * @return Whether the container is full.
	 */
	public boolean isFull() {
		// TODO Return whether the container is full.
		return false;
	}
	
	/**
	 * Add an item of the specified type to the first free container slot.
	 * @param type The type of item to add.
	 */
	public void add(ItemType type) {
		// TODO Add an item of the specified type to the first free container slot.
	}
	
	/**
	 * Serialise the placement to JSON to be persisted to disk.
	 * @return The serialised placement.
	 */
	public JSONObject serialise() {
		// Create the JSON object that will hold the information about this container.
		JSONObject container = new JSONObject();
		// ....
		// Return the serialised container.
		return container;
	}
}
