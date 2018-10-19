package gaia.server.world.placements.factories;

import java.util.Random;
import gaia.server.world.items.container.Container;
import gaia.server.world.placements.Placement;
import gaia.server.world.placements.Priority;
import gaia.server.world.placements.types.TilledEarth;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementUnderlay;

/**
 * Factory for creating a tilled earth placements.
 */
public class TilledEarthFactory implements IPlacementFactory {
	
	@Override
	public Placement create(short x, short y) {
		return new TilledEarth(x, y);
	}

	@Override
	public Priority getInitialPriority() {
		return Priority.HIGH;
	}
	
	@Override
	public PlacementUnderlay getInitialUnderlay() {
		return PlacementUnderlay.TILLED_EARTH;
	}

	@Override
	public PlacementOverlay getInitialOverlay() {
		return PlacementOverlay.NONE;
	}

	@Override
	public Container getContainer(Random chunkRng) {
		return null;
	}
}
