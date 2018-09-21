package gaia.server.world.placements;

import java.util.Random;
import org.json.JSONObject;
import gaia.server.world.placements.builders.IPlacementBuilder;
import gaia.server.world.placements.builders.PlacementBuilders;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementType;
import gaia.world.PlacementUnderlay;
import gaia.world.TileType;
import gaia.world.items.ItemType;
import gaia.world.items.container.ContainerFactory;

/**
 * Factory for creating Placement instances.
 */
public class PlacementFactory {
	/**
	 * The RNG to use in generating new placements in their default state.
	 */
	private static Random placementGenerationRNG;
	
	static {
		placementGenerationRNG = new Random();
	}
	
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
	
	/**
     * Create a placement of the specified type in its default state using the specified RNG.
     * @param type The placements type.
     * @param x The placement x local position within a chunk.
	 * @param y The placement y local position within a chunk.
     * @param rng The RNG to use in creating the placement.
     * @return The placement.
     */
    public static Placement create(PlacementType type, short x, short y, Random rng) {
        // Create the new placement.
        Placement placement = new Placement(type, x, y);
        // Get the relevant placement builder.
        IPlacementBuilder placementFactory = PlacementBuilders.getForType(type);
        // Create the placement state.
        placement.setState(placementFactory.createState(rng));
        // Create the action for this placement.
        placement.setActions(placementFactory.getActions());
        // Set the initial priority for this placement.
        placement.setPriority(placementFactory.getInitialPriority());
        // Set the initial underlay for this placement.
        placement.setUnderlay(placementFactory.getInitialUnderlay());
        // Set the initial overlay for this placement.
        placement.setOverlay(placementFactory.getInitialOverlay());
        // Set the container for this placement.
        placement.setContainer(placementFactory.getContainer(rng));
        // Return the new placement.
        return placement;
    }
    
    /**
     * Create a placement of the specified type in its default state.
     * @param type The placement type.
     * @param x The placement x local position within a chunk.
	 * @param y The placement y local position within a chunk.
     * @return The placement.
     */
    public static Placement create(PlacementType type, short x, short y) {
        // Return the new placement, passing the RNG to use in generating new placements in their default state.
        return create(type, x, y, PlacementFactory.placementGenerationRNG);
    }
	
	/**
	 * Attempt to create a placement as the result of interacting with an empty tile using the specified item.
	 * @param placementJSON The JSON object representing an existing placement.
	 * @param x The placement x local position within a chunk.
	 * @param y The placement y local position within a chunk.
	 * @return The placement created as a result of using the item on the tile, or null if no placement was created.
	 */
	public static Placement create(TileType tile, ItemType item, short x, short y) {
		// TODO Check whether using the item on this tile will create a placement, if so create and return it.
		return null;
	}
	
}
