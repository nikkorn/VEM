package gaia.server.world.placements.factories;

import java.util.Random;
import gaia.server.world.placements.Placement;
import gaia.server.world.placements.Priority;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementUnderlay;
import gaia.world.items.container.Container;

/**
 * Represents a placement builder.
 */
public interface IPlacementFactory {
	
	/**
	 * Create a placement.
	 * @param x The x position of the placement with its parent chunk.
	 * @param y The y position of the placement with its parent chunk.
	 * @return The placement.
	 */
	Placement create(short x, short y);
	
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
}
