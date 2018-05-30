package server.world.placement.factories;

import org.json.JSONObject;
import server.items.ItemType;
import server.world.TileFactory;
import server.world.placement.Container;
import server.world.placement.IPlacementAction;
import server.world.placement.Priority;
import server.world.placement.state.IPlacementState;
import server.world.placement.state.TilledEarthState;

/**
 * Factory for creating a tilled earth placement.
 */
public class TilledEarthFactory implements IPlacementFactory {

	@Override
	public IPlacementState createState() {
		return new TilledEarthState();
	}

	@Override
	public IPlacementState createState(JSONObject stateJSON) {
		// TODO Create state based on JSON!
		return new TilledEarthState();
	}
	
	@Override
	public IPlacementAction getAction() {
		return new IPlacementAction() {
			@Override
			public void onServerTick(IPlacementState state, Container container) {
				System.out.println("Just growing some plants at 100% capacity!");
			}

			@Override
			public void onTimeUpdate(IPlacementState state, Container container) {
				System.out.println("Just growing some plants, oh did the time change?");
			}

			@Override
			public ItemType onInteraction(IPlacementState state, Container container, ItemType item) {
				System.out.println("Just growing some plants, did you use that item on me? I will keep it!");
				return ItemType.NONE;
			}
		};
	}

	@Override
	public Priority getInitialPriority() {
		return Priority.HIGH;
	}

	@Override
	public Container getInitialContainer() {
		return TileFactory.createContainer(3);
	}
}
