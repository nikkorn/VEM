package server.world.tile.placement.state;

import org.json.JSONObject;

/**
 * Represents the internal state of a placement.
 */
public interface IPlacementState {
	
	/**
	 * Serialise the placement state to JSON.
	 * @return The JSON object representing the placement state.
	 */
	JSONObject serialise();
}
