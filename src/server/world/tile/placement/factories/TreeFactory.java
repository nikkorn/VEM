package server.world.tile.placement.factories;

import org.json.JSONObject;
import server.items.ItemType;
import server.world.tile.TileFactory;
import server.world.tile.placement.Container;
import server.world.tile.placement.IPlacementAction;
import server.world.tile.placement.Priority;
import server.world.tile.placement.state.IPlacementState;
import server.world.tile.placement.state.TreeState;

/**
 * Factory for creating a plain tree placement.
 * Plain tree placments produce wood at random times, the rate at which is affected by the season.
 */
public class TreeFactory implements IPlacementFactory {

	@Override
	public IPlacementState createState() {
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
	public Container getInitialContainer() {
		return TileFactory.createContainer(2);
	}

	@Override
	public IPlacementAction getAction() {
		return new IPlacementAction() {
			@Override
			public void onServerTick(IPlacementState state, Container container) {
				// We may be producing some wood! But only if the placement container is not full.
				if (container.isFull()) {
					return;
				}
				// Our tree state holds a lotto that is used to determine whether to produce some wood.
				ItemType produced = ((TreeState)state).woodProductionLotto.draw();
				// If we were able to produce some wood then put it in the container of this tree placement.
				if (produced != ItemType.NONE) {
					container.add(produced);
				}
			}

			@Override
			public void onTimeUpdate(IPlacementState state, Container container) {}

			@Override
			public ItemType onInteraction(IPlacementState state, Container container, ItemType item) {
				return item;
			}
		};
	}
}
