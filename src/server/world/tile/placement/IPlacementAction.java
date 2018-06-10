package server.world.tile.placement;

import server.items.ItemType;
import server.world.container.Container;
import server.world.messaging.WorldMessageQueue;
import server.world.tile.placement.state.IPlacementState;
import server.world.time.Time;

/**
 * Represents an action to be taken by a placement.
 */
public interface IPlacementAction {
	
	/**
	 * Execute the action in response to a server tick.
	 * @param state The placement state.
	 * @param container The placement container.
	 * @param worldMessageQueue The world message queue.
	 */
	public abstract void onServerTick(IPlacementState state, Container container, WorldMessageQueue worldMessageQueue);
	
	/**
	 * Execute the action in response to the time being updated.
	 * @param time The current time.
	 * @param state The placement state.
	 * @param container The placement container.
	 * @param worldMessageQueue The world message queue.
	 */
	public abstract void onTimeUpdate(Time time, IPlacementState state, Container container, WorldMessageQueue worldMessageQueue);
	
	/**
	 * Execute the action in response to interaction from another entity.
	 * @param state The placement state.
	 * @param container The placement container.
	 * @param item The item used in interacting with the placement.
	 * @param worldMessageQueue The world message queue.
	 * @returns The modified item type.
	 */
	public abstract ItemType onInteraction(IPlacementState state, Container container, ItemType item, WorldMessageQueue worldMessageQueue);
}
