package gaia.server.world.placements;

import gaia.time.Time;
import gaia.world.items.ItemType;

/**
 * Represents any actions that can be taken by a placements.
 */
public interface IPlacementActions {
	
	/**
	 * Execute the action in response to a server tick.
	 * @param placement The placement.
	 */
	public abstract void onServerTick(IModifiablePlacement placement);
	
	/**
	 * Execute the action in response to the time being updated.
	 * @param placement The placement.
	 * @param time The current time.
	 */
	public abstract void onTimeUpdate(IModifiablePlacement placement, Time time);
	
	/**
	 * Execute the action in response to interaction from another entity.
	 * @param placement The placement.
	 * @param item The item used in this interaction.
	 * @returns The modified item type.
	 */
	public abstract ItemType onInteraction(IModifiablePlacement placement, ItemType item);
}
