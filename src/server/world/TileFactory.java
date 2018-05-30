package server.world;

import java.util.HashMap;
import org.json.JSONObject;
import server.world.placement.Container;
import server.world.placement.Placement;
import server.world.placement.PlacementType;
import server.world.placement.Priority;
import server.world.placement.factories.IPlacementFactory;
import server.world.placement.factories.TilledEarthFactory;

/**
 * A factory for creating tile entities.
 */
public class TileFactory {
	/**
	 * The map of placement factories, keyed on placement type.
	 */
	private static HashMap<PlacementType, IPlacementFactory> placementFactories = new HashMap<PlacementType, IPlacementFactory>()
	{{ 
		this.put(PlacementType.TILLED_EARTH, new TilledEarthFactory());
	}};
	
	/**
	 * Create a placement of the specified type in its default state.
	 * @param type The placement type.
	 * @return The placement.
	 */
	public static Placement createPlacement(PlacementType type) {
		// Create the new placement.
		Placement placement = new Placement(type);
		// Get the relevant placement factory.
		IPlacementFactory placementFactory = placementFactories.get(type);
		// Create the placement state.
		placement.setState(placementFactory.createState());
		// Create the action for this placement.
		placement.setAction(placementFactory.getAction());
		// Set the initial priority for this placement.
		placement.setPriority(placementFactory.getInitialPriority());
		// Set the initial container for this placement.
		placement.setContainer(placementFactory.getInitialContainer());
		// Return the new placement.
		return placement;
	}
	
	/**
	 * Create a placement based on existing world state.
	 * @param placementJSON The JSON object representing an existing placement.
	 * @return The placement.
	 */
	public static Placement createPlacement(JSONObject placementJSON) {
		// Get the placement type.
		PlacementType placementType = PlacementType.values()[placementJSON.getInt("type")];
		// Create the placement.
		Placement placement = new Placement(placementType);
		// Get the relevant placement factory.
		IPlacementFactory placementFactory = placementFactories.get(placementType);
		// Create the action for this placement.
		placement.setAction(placementFactory.getAction());
		// Create the placement state if there is any.
		if (placementJSON.has("state")) {
			placement.setState(placementFactory.createState(placementJSON.getJSONObject("state")));
		}
		// Create the placement container if it has one.
		if (placementJSON.has("container")) {
			placement.setContainer(TileFactory.createContainer(placementJSON.getJSONObject("container")));
		}
		// Set the placement priority.
		placement.setPriority(Priority.values()[placementJSON.getInt("priority")]);
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
	 * @param containerJSON The JSON object representing the container.
	 * @return The container.
	 */
	public static Container createContainer(JSONObject containerJSON) {
		return null;
	}
}