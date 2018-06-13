package server.world.tile.placement;

import server.items.ItemType;
import server.world.messaging.WorldMessageQueue;
import server.world.time.Time;

/**
 * Represents an action to be taken by a placement.
 */
public interface IPlacementAction {
	
	/**
	 * Execute the action in response to a server tick.
	 * @param placement The placement.
	 * @param worldMessageQueue The world message queue.
	 */
	public abstract void onServerTick(IModifiablePlacement placement, WorldMessageQueue worldMessageQueue);
	
	/**
	 * Execute the action in response to the time being updated.
	 * @param placement The placement.
	 * @param time The current time.
	 * @param worldMessageQueue The world message queue.
	 */
	public abstract void onTimeUpdate(IModifiablePlacement placement, Time time, WorldMessageQueue worldMessageQueue);
	
	/**
	 * Execute the action in response to interaction from another entity.
	 * @param placement The placement.
	 * @param item The item used in this interaction.
	 * @param worldMessageQueue The world message queue.
	 * @returns The modified item type.
	 */
	public abstract ItemType onInteraction(IModifiablePlacement placement, ItemType item, WorldMessageQueue worldMessageQueue);
}
