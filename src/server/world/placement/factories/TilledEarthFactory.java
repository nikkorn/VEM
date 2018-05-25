package server.world.placement.factories;

import org.json.JSONObject;
import server.items.ItemType;
import server.world.TileFactory;
import server.world.placement.Container;
import server.world.placement.IPlacementAction;
import server.world.placement.Placement;
import server.world.placement.PlacementType;
import server.world.placement.Priority;
import server.world.placement.state.TilledEarthPlacementState;

/**
 * Factory for creating a tilled earth placement.
 */
public class TilledEarthFactory extends PlacementFactory<TilledEarthPlacementState> {

	@Override
	public Placement create(PlacementType type) {
		// Create the placement of the expected type.
		Placement placement = new Placement(PlacementType.TILLED_EARTH);
		// Set the action for this placement.
		placement.setAction(createAction());
		// Set the priority of this placement.
		placement.setPriority(Priority.HIGH);
		// Set the container for this placement.
		placement.setContainer(TileFactory.createContainer(3));
		// Return the newly created placement.
		return placement;
	}

	@Override
	public Placement create(JSONObject placementJSON) {
		// Create the placement of the expected type.
		Placement placement = new Placement(PlacementType.TILLED_EARTH);
		// Set the action for this placement.
		placement.setAction(createAction());
		// Set the priority of this placement.
		placement.setPriority(Priority.HIGH);
		// Set the container for this placement.
		placement.setContainer(TileFactory.createContainer(3, placementJSON.getJSONArray("container")));
		// Return the placement placement.
		return placement;
	}

	@Override
	public TilledEarthPlacementState createState() {
		return new TilledEarthPlacementState();
	}

	@Override
	public TilledEarthPlacementState createState(JSONObject stateJSON) {
		// TODO Create state based on JSON!
		return new TilledEarthPlacementState();
	}
	
	@Override
	protected IPlacementAction<TilledEarthPlacementState> createAction() {
		return new IPlacementAction<TilledEarthPlacementState>() {
			@Override
			public void onServerTick(TilledEarthPlacementState state, Container container) {
				System.out.println("Just growing some plants at 100% capacity!");
			}

			@Override
			public void onTimeUpdate(TilledEarthPlacementState state, Container container) {
				System.out.println("Just growing some plants, oh did the time change?");
			}

			@Override
			public void onInteraction(TilledEarthPlacementState state, Container container, ItemType item) {
				System.out.println("Just growing some plants, did you use that item on me?");
			}
		};
	}
}
