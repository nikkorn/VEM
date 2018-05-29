package server.world.placement.factories;

import org.json.JSONObject;
import server.items.ItemType;
import server.world.TileFactory;
import server.world.placement.Container;
import server.world.placement.IPlacementAction;
import server.world.placement.Placement;
import server.world.placement.PlacementType;
import server.world.placement.Priority;
import server.world.placement.state.IPlacementState;
import server.world.placement.state.TilledEarthState;

/**
 * Factory for creating a tilled earth placement.
 */
public class TilledEarthFactory extends PlacementFactory {

	@Override
	public Placement create() {
		// Create the placement of the expected type.
		Placement placement = new Placement(PlacementType.TILLED_EARTH);
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
		// Set the priority of this placement.
		placement.setPriority(Priority.HIGH);
		// Set the container for this placement.
		placement.setContainer(TileFactory.createContainer(placementJSON.getJSONObject("container")));
		// Return the placement placement.
		return placement;
	}

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
	public IPlacementAction createAction() {
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
}
