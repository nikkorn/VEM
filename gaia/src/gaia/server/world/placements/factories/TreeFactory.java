package gaia.server.world.placements.factories;

import java.util.Random;
import gaia.server.world.placements.Placement;
import gaia.server.world.placements.Priority;
import gaia.server.world.placements.types.Tree;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementUnderlay;
import gaia.world.items.container.Container;
import gaia.world.items.container.ContainerFactory;

/**
 * Factory for creating a plain tree placement.
 */
public class TreeFactory implements IPlacementFactory {

	@Override
	public Placement create(short x, short y) {
		return new Tree(x, y);
	}

	@Override
	public Priority getInitialPriority() {
		return Priority.MEDIUM;
	}
	
	@Override
	public PlacementUnderlay getInitialUnderlay() {
		return PlacementUnderlay.BASIC_TREE;
	}

	@Override
	public PlacementOverlay getInitialOverlay() {
		return PlacementOverlay.NONE;
	}

	@Override
	public Container getContainer(Random chunkRng) {
		return ContainerFactory.create(2);
	}
}
