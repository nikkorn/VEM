package server.world.tile.placement;

import org.json.JSONObject;
import server.world.container.Container;
import server.world.tile.placement.state.IPlacementState;

/**
 * Represents a tile-positioned placement.
 */
public class Placement implements IModifiablePlacement{
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
	private Priority priority = Priority.NONE;
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
	 * The placement underlay.
	 */
	private PlacementUnderlay underlay = PlacementUnderlay.NONE;
	/**
	 * The placement overlay.
	 */
	private PlacementOverlay overlay = PlacementOverlay.NONE;
	
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
	 * Get the placement underlay.
	 * @return The placement underlay.
	 */
	public PlacementUnderlay getUnderlay() {
		return this.underlay;
	}
	
	/**
	 * Set the placement underlay.
	 * @param underlay The placement underlay.
	 */
	public void setUnderlay(PlacementUnderlay underlay) {
		this.underlay = underlay;
	}
	
	/**
	 * Get the placement overlay.
	 * @return The placement overlay.
	 */
	public PlacementOverlay getOverlay() {
		return this.overlay;
	}
	
	/**
	 * Set the placement overlay.
	 * @param overlay The placement overlay.
	 */
	public void setOverlay(PlacementOverlay overlay) {
		this.overlay = overlay;
	}
	
	/**
	 * Serialise the placement to JSON to be persisted to disk.
	 * @return The serialised placement.
	 */
	public JSONObject serialise() {
		// Create the JSON object that will hold the information about this placement.
		JSONObject placement = new JSONObject();
		// Set the type.
		placement.put("type", this.type.ordinal());
		// Set the underlay.
		placement.put("underlay", this.underlay.ordinal());
		// Set the overlay.
		placement.put("overlay", this.overlay.ordinal());
		// Set container array (if this placement has one).
		if (this.container != null) {
			placement.put("container", this.container.serialise());
		}
		// Set state (if this placement has any).
		if (this.state != null) {
			placement.put("state", this.state.asJSON());
		}
		// Set the priority.
		placement.put("priority", this.priority.ordinal());
		// Return the serialised placement.
		return placement;
	}
}
