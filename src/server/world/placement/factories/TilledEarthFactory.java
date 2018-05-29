package server.world.placement.factories;

import org.json.JSONObject;
import server.items.ItemType;
import server.world.TileFactory;
import server.world.placement.Container;
import server.world.placement.IPlacementAction;
import server.world.placement.Priority;
import server.world.placement.placements.TilledEarth;
import server.world.placement.state.TilledEarthState;

/**
 * Factory for creating a tilled earth placement.
 */
public class TilledEarthFactory extends PlacementFactory<TilledEarth, TilledEarthState> {

	@Override
	public TilledEarth create() {
		// Create the placement of the expected type.
		TilledEarth placement = new TilledEarth();
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
	public TilledEarth create(JSONObject placementJSON) {
		// Create the placement of the expected type.
		TilledEarth placement = new TilledEarth();
		// Set the action for this placement.
		placement.setAction(createAction());
		// Set the priority of this placement.
		placement.setPriority(Priority.HIGH);
		// Set the container for this placement.
		placement.setContainer(TileFactory.createContainer(placementJSON.getJSONObject("container")));
		// Return the placement placement.
		return placement;
	}

	@Override
	public TilledEarthState createState() {
		return new TilledEarthState();
	}

	@Override
	public TilledEarthState createState(JSONObject stateJSON) {
		// TODO Create state based on JSON!
		return new TilledEarthState();
	}
	
	@Override
	public IPlacementAction<TilledEarthState> createAction() {
		return new IPlacementAction<TilledEarthState>() {
			@Override
			public void onServerTick(TilledEarthState state, Container container) {
				System.out.println("Just growing some plants at 100% capacity!");
			}

			@Override
			public void onTimeUpdate(TilledEarthState state, Container container) {
				System.out.println("Just growing some plants, oh did the time change?");
			}

			@Override
			public ItemType onInteraction(TilledEarthState state, Container container, ItemType item) {
				System.out.println("Just growing some plants, did you use that item on me? I will keep it!");
				return ItemType.NONE;
			}
		};
	}
}
