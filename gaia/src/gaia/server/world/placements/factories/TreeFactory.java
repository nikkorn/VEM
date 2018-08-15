package gaia.server.world.placements.factories;

import java.util.Random;
import org.json.JSONObject;
import gaia.server.items.ItemType;
import gaia.server.world.chunk.ChunkFactory;
import gaia.server.world.container.Container;
import gaia.server.world.container.NoFreeSlotException;
import gaia.server.world.placements.IModifiablePlacement;
import gaia.server.world.placements.IPlacementAction;
import gaia.server.world.placements.Priority;
import gaia.server.world.placements.state.IPlacementState;
import gaia.server.world.placements.state.TreeState;
import gaia.time.Time;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementUnderlay;

/**
 * Factory for creating a plain tree placements.
 * Plain tree placments produce wood at random times, the rate at which is affected by the season.
 */
public class TreeFactory implements IPlacementFactory {

	@Override
	public IPlacementState createState(Random chunkRng) {
		return new TreeState();
	}

	@Override
	public IPlacementState createState(JSONObject stateJSON) {
		return new TreeState();
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
		return ChunkFactory.createContainer(2);
	}

	@Override
	public IPlacementAction getAction() {
		return new IPlacementAction() {
			@Override
			public void onServerTick(IModifiablePlacement placement) {
				// Get the placements container.
				Container container = placement.getContainer();
				// We may be producing some wood! But only if the placements container is not full.
				if (container.isFull()) {
					return;
				}
				// Our tree state holds a lotto that is used to determine whether to produce some wood.
				ItemType produced = ((TreeState)placement.getState()).woodProductionLotto.draw();
				// If we were able to produce some wood then put it in the container of this tree placements.
				if (produced != ItemType.NONE) {
					try {
						container.add(produced);
					} catch (NoFreeSlotException e) {
						// We have no free slot for our wood, this is fine.
					}
				}
			}

			@Override
			public void onTimeUpdate(IModifiablePlacement placement, Time time) {}

			@Override
			public ItemType onInteraction(IModifiablePlacement placement, ItemType item) {
				return ItemType.NONE;
			}
		};
	}
}