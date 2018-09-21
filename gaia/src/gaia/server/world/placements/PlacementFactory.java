package gaia.server.world.placements;

import org.json.JSONObject;

import gaia.server.world.placements.builders.IPlacementBuilder;
import gaia.server.world.placements.builders.PlacementBuilders;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementType;
import gaia.world.PlacementUnderlay;
import gaia.world.items.container.ContainerFactory;

/**
 * Factory for creating Placement instances.
 */
public class PlacementFactory {
	
	/**
	 * Create a placement based on existing state.
	 * @param placementJSON The JSON object representing an existing placement.
	 * @param x The placement x local position within a chunk.
	 * @param y The placement y local position within a chunk.
	 * @return The placement.
	 */
	public static Placement create(JSONObject placementJSON, short x, short y) {
		// Get the placement type.
		PlacementType placementType = PlacementType.values()[placementJSON.getInt("type")];
		// Create the placement.
		Placement placement = new Placement(placementType, x, y);
		// Get the relevant placement builder.
		IPlacementBuilder builder = PlacementBuilders.getForType(placementType);
		// Create the action for this placement.
		placement.setActions(builder.getActions());
		// Create the placement state if there is any.
		if (placementJSON.has("state")) {
			placement.setState(builder.createState(placementJSON.getJSONObject("state")));
		}
		// Create the placement container if it has one.
		if (placementJSON.has("container")) {
			placement.setContainer(ContainerFactory.create(placementJSON.getJSONArray("container")));
		}
		// Set the placement priority.
		placement.setPriority(Priority.values()[placementJSON.getInt("priority")]);
		// Set the placement underlay.
		placement.setUnderlay(PlacementUnderlay.values()[placementJSON.getInt("underlay")]);
		// Set the placement overlay.
		placement.setOverlay(PlacementOverlay.values()[placementJSON.getInt("overlay")]);
		// Return the new placement.
		return placement;
	}
}
