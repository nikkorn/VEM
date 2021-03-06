package gaia.server.world.placements.builders;

import java.util.Random;
import org.json.JSONObject;
import gaia.server.world.placements.IPlacementActions;
import gaia.server.world.placements.Priority;
import gaia.server.world.placements.state.IPlacementState;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementUnderlay;
import gaia.world.items.container.Container;

/**
 * Represents a placement builder.
 */
public interface IPlacementBuilder {
	
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
	 * Create the placement actions for this placement.
	 * @return The placement actions.
	 */
	IPlacementActions getActions();
}
