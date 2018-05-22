package server.world.placement.factories;

import org.json.JSONObject;
import server.world.placement.Placement;
import server.world.placement.PlacementType;

/**
 * Interface for placement factories.
 */
public interface IPlacementFactory {
	
	/**
	 * Create a placement of the specified type in its default state.
	 * @param type The placement type.
	 * @return The placement.
	 */
	public Placement create(PlacementType type);
	
	/**
	 * Create a placement based on existing world state.
	 * @param placement The JSON object representing an existing placement.
	 * @return The placement.
	 */
	public Placement create(JSONObject placement);
}
