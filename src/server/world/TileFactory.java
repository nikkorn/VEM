package server.world;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import server.world.placement.Container;
import server.world.placement.Placement;
import server.world.placement.PlacementType;
import server.world.placement.factories.PlacementFactory;
import server.world.placement.factories.TilledEarthFactory;
import server.world.placement.state.IPlacementState;

/**
 * A factory for creating tile entities.
 */
public class TileFactory {
	/**
	 * The map of placement factories, keyed on placement type.
	 */
	private static HashMap<PlacementType, PlacementFactory> placementFactories = new HashMap<PlacementType, PlacementFactory>()
	{{ 
		this.put(PlacementType.TILLED_EARTH, new TilledEarthFactory());
	}};
	
	/**
	 * Create a placement of the specified type in its default state.
	 * @param type The placement type.
	 * @return The placement.
	 */
	public static Placement createPlacement(PlacementType type) {
		// Get the relevant placement factory.
		PlacementFactory<? extends IPlacementState> placementFactory = placementFactories.get(type);
		// Create the placement.
		Placement placement = placementFactory.create();
		// Create the placement state.
		placement.setState(placementFactory.createState());
		// Return the new placement.
		return placement;
	}
	
	/**
	 * Create a placement based on existing world state.
	 * @param placementJSON The JSON object representing an existing placement.
	 * @return The placement.
	 */
	public static Placement createPlacement(JSONObject placementJSON) {
		// Get the relevant placement factory.
		PlacementFactory<? extends IPlacementState> placementFactory = placementFactories.get(PlacementType.values()[placementJSON.getInt("type")]);
		// Create the placement.
		Placement placement = placementFactory.create(placementJSON);
		// Create the placement state if there is any.
		if (placementJSON.has("state")) {
			placement.setState(placementFactory.createState(placementJSON.getJSONObject("state")));
		}
		// Return the new placement.
		return placement;
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