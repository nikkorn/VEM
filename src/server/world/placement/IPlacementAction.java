package server.world.placement;

import server.world.placement.state.IPlacementState;

/**
 * Represents an action to be taken by a placement on tick.
 */
public interface IPlacementAction<TState extends IPlacementState> {
	
	/**
	 * Execute the action.
	 * @param state The placement state.
	 * @param container The placement container.
	 */
	void execute(TState state, Container container);
}
