package server.world.placement;

import server.items.ItemType;
import server.world.placement.state.IPlacementState;

/**
 * Represents an action to be taken by a placement.
 */
public interface IPlacementAction<TState extends IPlacementState> {
	
	/**
	 * Execute the action in response to a server tick.
	 * @param state The placement state.
	 * @param container The placement container.
	 */
	void onServerTick(TState state, Container container);
	
	/**
	 * Execute the action in response to the time being updated.
	 * @param state The placement state.
	 * @param container The placement container.
	 */
	void onTimeUpdate(TState state, Container container);
	
	/**
	 * Execute the action in response to interaction from another entity.
	 * @param state The placement state.
	 * @param container The placement container.
	 * @param item The item used in interacting with the placement.
	 * @returns The modified item type.
	 */
	ItemType onInteraction(TState state, Container container, ItemType item);
}
