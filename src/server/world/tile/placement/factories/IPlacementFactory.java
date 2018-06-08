package server.world.tile.placement.factories;

import java.util.Random;
import org.json.JSONObject;

import server.world.container.Container;
import server.world.tile.placement.IPlacementAction;
import server.world.tile.placement.Priority;
import server.world.tile.placement.state.IPlacementState;

/**
 * Represents a placement factory.
 */
public interface IPlacementFactory {
	
	/**
	 * Create default state for the placement type.
	 * @param chunkRng The rng to use in creating a placement for a chunk.
	 * @return The default placement state.
	 */
	IPlacementState createState(Random chunkRng);
	
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
	 * @param chunkRng The rng to use in creating a placement for a chunk.
	 * @return The initial placement container.
	 */
	Container getInitialContainer(Random chunkRng);
	
	/**
	 * Create the placement action for this placement.
	 * @return The placement action.
	 */
	IPlacementAction getAction();
}
