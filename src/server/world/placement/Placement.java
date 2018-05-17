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
}
