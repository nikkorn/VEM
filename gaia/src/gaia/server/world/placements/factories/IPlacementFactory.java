package gaia.server.world.placements.factories;

import java.util.Random;
import org.json.JSONObject;

import gaia.server.world.placements.IPlacementAction;
import gaia.server.world.placements.Priority;
import gaia.server.world.placements.state.IPlacementState;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementUnderlay;
import gaia.world.items.container.Container;

/**
 * Represents a placements factory.
 */
public interface IPlacementFactory {
	
	/**
	 * Create default state for the placements type.
	 * @param chunkRng The rng to use in creating a placements for a chunk.
	 * @return The default placements state.
	 */
	IPlacementState createState(Random chunkRng);
	
	/**
	 * Create state for the placements type based on existing save state.
	 * @param stateJSON The existing save state.
	 * @return The existing placements state.
	 */
	IPlacementState createState(JSONObject stateJSON);
	
	/**
	 * Get the initial placements priority.
	 * @return The initial placements priority.
	 */
	Priority getInitialPriority();
	
	/**
	 * Get the initial placements underlay.
	 * @return The initial placements underlay.
	 */
	PlacementUnderlay getInitialUnderlay();
	
	/**
	 * Get the initial placements overlay.
	 * @return The initial placements overlay.
	 */
	PlacementOverlay getInitialOverlay();
	
	/**
	 * Get the placements container.
	 * @param chunkRng The rng to use in creating a placements for a chunk.
	 * @return The placements container.
	 */
	Container getContainer(Random chunkRng);
	
	/**
	 * Create the placements action for this placements.
	 * @return The placements action.
	 */
	IPlacementAction getAction();
}
