package server.world.placement;

import org.json.JSONObject;

import server.world.placement.state.IPlacementState;

/**
 * Represents a tile-positioned placement.
 */
public class Placement {
	/**
	 * The type of the placement.
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
	 * The placement action to be executed per game engine tick
	 * or once per interaction if the placement priority is LOW.
	 */
	private IPlacementAction<? extends IPlacementState> action;
	/**
	 * The placement state.
	 */
	private IPlacementState state;
	
	/**
	 * Create a new instance of the Placement class.
	 * @param type The type of the placement.
	 */
	public Placement(PlacementType type) {
		this.type = type;
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
		this.priority = priority;
	}

	/**
	 * Get the placement type.
	 * @return The placement type.
	 */
	public PlacementType getType() {
		return type;
	}

	/**
	 * Get the placement action.
	 * @return The placement action.
	 */
	public IPlacementAction<? extends IPlacementState> getAction() {
		return action;
	}

	/**
	 * Set the placement action.
	 * @param action The placement action.
	 */
	public void setAction(IPlacementAction<? extends IPlacementState> action) {
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
		// TODO Set container.
		// TODO Set state.
		// TODO Set x/y.
		return null;
	}
}
