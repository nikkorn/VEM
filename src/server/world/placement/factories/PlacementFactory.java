package server.world.placement.factories;

import org.json.JSONObject;
import server.world.placement.IPlacementAction;
import server.world.placement.Placement;
import server.world.placement.state.IPlacementState;

/**
 * The base abstract placement factory.
 * @param <TState> The placement state type.
 */
public abstract class PlacementFactory<TState extends IPlacementState> {
	
	/**
	 * Create a placement in its default state.
	 * @return The placement in its default state.
	 */
	public abstract Placement create();
	
	/**
	 * Create a placement based on existing world state.
	 * @param placementJSON The JSON object representing an existing placement.
	 * @return The placement.
	 */
	public abstract Placement create(JSONObject placementJSON);
	
	/**
	 * Create default state for the placement type.
	 * @return The default placement state.
	 */
	public abstract TState createState();
	
	/**
	 * Create state for the placement type based on existing save state.
	 * @param stateJSON The existing save state.
	 * @return The existing placement state.
	 */
	public abstract TState createState(JSONObject stateJSON);
	
	/**
	 * Create the placement action for this placement.
	 * @return The placement action.
	 */
	public IPlacementAction<TState> createAction() {
		// Placements have no action by default.
		return null;
	}
}
