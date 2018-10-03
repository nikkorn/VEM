package gaia.server.world.placements;

import gaia.world.items.ItemType;

/**
 * Executor of placement actions.
 */
public interface IPlacementActionsExecutor {
	
	/**
	 * Exectute the placement actions.
	 * @param action The placement actions.
	 * @return The modification made to an item if the action being executed is a placement interaction.
	 */
	ItemType execute(IActionablePlacement action);
}
