package server.world.tile.placement;

import org.json.JSONObject;

/**
 * Represents a placement container.
 */
public class Container {
	
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
