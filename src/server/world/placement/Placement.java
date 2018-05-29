package server.world.placement;

import org.json.JSONObject;
import server.world.placement.state.IPlacementState;

/**
 * Represents a tile-positioned placement.
 */
public class Placement {
	/**
	 * The placement type.
	 */
	private PlacementType type;
	/**
	 * The container of the placement.
	 */
	private Container container;
	/**
	 * The placement priority.
	 */
	private Priority priority;
	/**
	 * The placement state.
	 */
	private IPlacementState state;
	/**
	 * The placement action to be executed per game engine tick and/or time update.
	 * This could also be done per interaction if the placement priority is LOW.
	 */
	private IPlacementAction action;
	
	/**
	 * Create a new instance of the Placement class.
	 * @param type The placement type.
	 */
	public Placement(PlacementType type) {
		this.type = type;
	}
	
	/**
	 * Get the placement type.
	 * @return The placement type.
	 */
	public PlacementType getType() {
		return this.type;
	}

	/**
	 * Get the placement priority.
	 * @return The placement priority.
	 */
	public Priority getPriority() {
		return priority;
	}

	/**
	 * Set the placement priority.
	 * @param priority The placement priority.
	 */
	public void setPriority(Priority priority) {
		// TODO Should call some callback to let the chunk know it has changed
		// priority as this could mean the chunk becomes active/inactive.
		this.priority = priority;
	}
	
	/**
	 * Get the placement action.
	 * @return The placement action.
	 */
	public IPlacementAction getAction() {
		return action;
	}

	/**
	 * Set the placement action.
	 * @param action The placement action.
	 */
	public void setAction(IPlacementAction action) {
		this.action = action;
	}

	/**
	 * Get the placement container.
	 * @return The placement container.
	 */
	public Container getContainer() {
		return container;
	}

	/**
	 * Set the placement container.
	 * @param container The placement container.
	 */
	public void setContainer(Container container) {
		this.container = container;
	}

	/**
	 * Get the state of the placement.
	 * @return The state of the placement.
	 */
	public IPlacementState getState() {
		return state;
	}

	/**
	 * Set the state of the placement.
	 * @param state The state of the placement.
	 */
	public void setState(IPlacementState state) {
		this.state = state;
	}
	
	/**
	 * Serialise the placement to JSON to be persisted to disk.
	 * @return The serialised placement.
	 */
	public JSONObject serialise() {
		// Create the JSON object that will hold the information about this placement.
		JSONObject placement = new JSONObject();
		// Set the type.
		placement.put("type", this.getType().ordinal());
		// Set container (if this placement has one).
		if (this.container != null) {
			placement.put("container", this.container.serialise());
		}
		// Set state (if this placement has any).
		if (this.state != null) {
			placement.put("state", this.state.serialise());
		}
		// Set the priority.
		placement.put("priority", this.priority.ordinal());
		// Return the serialised placement.
		return placement;
	}
}
