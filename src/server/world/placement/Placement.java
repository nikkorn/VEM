package server.world.placement;

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
	private IPlacementAction action;
	
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
}
