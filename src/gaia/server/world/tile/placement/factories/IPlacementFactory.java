package gaia.server.world.tile.placement.factories;

import java.util.Random;
import org.json.JSONObject;
import gaia.server.world.container.Container;
import gaia.server.world.tile.placement.IPlacementAction;
import gaia.server.world.tile.placement.PlacementOverlay;
import gaia.server.world.tile.placement.PlacementUnderlay;
import gaia.server.world.tile.placement.Priority;
import gaia.server.world.tile.placement.state.IPlacementState;

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
	 * Get the initial placement underlay.
	 * @return The initial placement underlay.
	 */
	PlacementUnderlay getInitialUnderlay();
	
	/**
	 * Get the initial placement overlay.
	 * @return The initial placement overlay.
	 */
	PlacementOverlay getInitialOverlay();
	
	/**
	 * Get the placement container.
	 * @param chunkRng The rng to use in creating a placement for a chunk.
	 * @return The placement container.
	 */
	Container getContainer(Random chunkRng);
	
	/**
	 * Create the placement action for this placement.
	 * @return The placement action.
	 */
	IPlacementAction getAction();
}
