package gaia.server.world.placements.state;

import org.json.JSONObject;

/**
 * Represents the internal state of a placements.
 */
public interface IPlacementState {
	
	/**
	 * Serialise the placements state to JSON.
	 * @return The JSON object representing the placements state.
	 */
	JSONObject asJSON();
}
