package gaia.server.world;

import gaia.server.world.placements.IPlacementDetails;
import gaia.world.Position;

/**
 * Handler for placement updates.
 */
public interface IPlacementUpdateHandler {
	
	/**
	 * Called when a placement changes at a position.
	 * @param placement The placement that has changed.
	 * @param position The position of the changed placmement.
	 */
	void onPlacementChange(IPlacementDetails placement, Position position);
	
	/**
	 * Called when a placement at a position is deleted.
	 * @param placement The placement that has been deleted.
	 * @param position The position of the deleted placement.
	 */
	void onPlacementDelete(IPlacementDetails placement, Position position);
}
