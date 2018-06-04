package server.world.tile.placement.factories;

import org.json.JSONObject;
import server.world.tile.placement.Container;
import server.world.tile.placement.IPlacementAction;
import server.world.tile.placement.Priority;
import server.world.tile.placement.state.IPlacementState;

/**
 * Represents a placement factory.
 */
public interface IPlacementFactory {
	
	/**
	 * Create default state for the placement type.
	 * @return The default placement state.
	 */
	IPlacementState createState();
	
	/**
	 * Create state for the placement type based on existing save state.
	 * @param stateJSON The existing save state.
	 * @return The existing placement state.
	 */
	IPlacementState createState(JSONObject stateJSON);
	
	/**
	 * Get the initial placement priority.
	 * @return The initial placement priority.
	 */
	Priority getInitialPriority();
	
	/**
	 * Get the initial placement container.
	 * @return The initial placement container.
	 */
	Container getInitialContainer();
	
	/**
	 * Create the placement action for this placement.
	 * @return The placement action.
	 */
	IPlacementAction getAction();
}
