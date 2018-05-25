package server.world.placement.state;

import org.json.JSONObject;

/**
 * Represents the internal state of a tilled earth placement.
 */
public class TilledEarthPlacementState implements IPlacementState {
	/**
	 * The number of minutes that the tilled earth is watered for.
	 */
	public int wateredTicks = 100;

	@Override
	public JSONObject serialise() {
		// Create the JSON object that will hold the information about this placement state.
		JSONObject state = new JSONObject();
		state.put("watered-ticks", this.wateredTicks);
		// Return the serialised state.
		return state;
	}
}
