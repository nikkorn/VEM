package server.world;

import org.json.JSONArray;
import org.json.JSONObject;
import server.world.placement.Container;
import server.world.placement.Placement;
import server.world.placement.PlacementType;

/**
 * A factory for world entities.
 */
public class WorldFactory {
	
	/**
	 * Create a world instance based on a world save file.
	 * @param name The name of the world.
	 * @return The world instance.
	 */
	public static World createWorld(String name) {
		return null;
	}
	
	/**
	 * Create a placement of the specified type in its default state.
	 * @param type The placement type.
	 * @return The placement.
	 */
	public static Placement createPlacement(PlacementType type) {
		return null;
	}
	
	/**
	 * Create a placement based on existing world state.
	 * @param placement The JSON object representing an existing placement.
	 * @return The placement.
	 */
	public static Placement createPlacement(JSONObject placement) {
		return null;
	}
	
	/**
	 * Create an empty container with the specified number of slots.
	 * @param numberOfSlots The number of slots.
	 * @return The container.
	 */
	public static Container createContainer(int numberOfSlots) {
		return null;
	}
	
	/**
	 * Create a container based on existing world state.
	 * @param numberOfSlots The number of slots.
	 * @param slots The JSON array representing the existing slots.
	 * @return The container.
	 */
	public static Container createContainer(int numberOfSlots, JSONArray slots) {
		return null;
	}
}
