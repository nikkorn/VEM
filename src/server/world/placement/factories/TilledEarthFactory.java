package server.world.placement.factories;

import org.json.JSONObject;
import server.world.ChunkFactory;
import server.world.placement.IPlacementAction;
import server.world.placement.Placement;
import server.world.placement.PlacementType;
import server.world.placement.Priority;

/**
 * Factory for creating a tilled earth placement.
 */
public class TilledEarthFactory implements IPlacementFactory {

	@Override
	public Placement create(PlacementType type) {
		// Create the placement of the expected type.
		Placement placement = new Placement(PlacementType.TILLED_EARTH);
		// Set the action for this placement.
		placement.setAction(createAction());
		// Set the priority of this placement.
		placement.setPriority(Priority.HIGH);
		// Set the container for this placement.
		placement.setContainer(ChunkFactory.createContainer(3));
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
		placement.setContainer(ChunkFactory.createContainer(3, placementJSON.getJSONArray("container")));
		// Return the placement placement.
		return placement;
	}
	
	/**
	 * Create the placement action for this placement.
	 * @return The placement action.
	 */
	private IPlacementAction createAction() {
		return new IPlacementAction() {
			@Override
			public void execute(Placement placement) {
				System.out.println("This " + placement.getType().toString() + " placement is just growing some plants!");
			}
		};
	}
}
