package server.world.placement;

/**
 * Represents an action to be taken by a placement on tick.
 */
public interface IPlacementAction {
	
	/**
	 * Execute the action.
	 * @param placement The placement that is taking action.
	 */
	void execute(Placement placement);
}
