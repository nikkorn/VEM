package gaia.server.world.placements;

import java.util.HashMap;
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
	/**
	 * The map of placement types generated as result of an item usage on a tile.
	 */
	private static HashMap<String, PlacementType> itemUsagePlacementResults;
	
	static {
		// Create the default RNG to use in building placements when one is not provided.
		placementGenerationRNG = new Random();
		// Populate the map of placement types generated as result of an item usage on a tile.
		itemUsagePlacementResults = new HashMap<String, PlacementType>();
		itemUsagePlacementResults.put(createItemTileLinkKey(ItemType.HOE, TileType.GRASS), PlacementType.TILLED_EARTH);
		itemUsagePlacementResults.put(createItemTileLinkKey(ItemType.HOE, TileType.PLAINS), PlacementType.TILLED_EARTH);
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
        IPlacementBuilder placementBuilder = PlacementBuilders.getForType(type);
        // Create the placement state.
        placement.setState(placementBuilder.createState(rng));
        // Create the action for this placement.
        placement.setActions(placementBuilder.getActions());
        // Set the initial priority for this placement.
        placement.setPriority(placementBuilder.getInitialPriority());
        // Set the initial underlay for this placement.
        placement.setUnderlay(placementBuilder.getInitialUnderlay());
        // Set the initial overlay for this placement.
        placement.setOverlay(placementBuilder.getInitialOverlay());
        // Set the container for this placement.
        placement.setContainer(placementBuilder.getContainer(rng));
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
		// Check whether using the item on this tile will create a placement, if so create and return it.
		String itemTileLinkKey = createItemTileLinkKey(item, tile);
		// Does using the item on the tile produce create a new placement?
		if (itemUsagePlacementResults.containsKey(itemTileLinkKey)) {
			// Create the placement and return it.
			return create(itemUsagePlacementResults.get(itemTileLinkKey), x, y);
		}
		// Using the item on the tile does not create a placement.
		return null;
	}
	
	/**
	 * Create a unique key for a item-tile combination.
	 * @param item The item type.
	 * @param tile The tile type.
	 * @return A unique key for a item-tile combination.
	 */
	private static String createItemTileLinkKey(ItemType item, TileType tile) {
		return item.toString() + "-" + tile.toString(); 
	}
}
