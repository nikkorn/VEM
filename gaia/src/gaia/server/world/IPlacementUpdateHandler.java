package gaia.server.world;

import gaia.server.world.placements.IPlacementDetails;
import gaia.world.Position;

/**
 * Handler for placement updates.
 */
public interface IPlacementUpdateHandler {
	
	/**
	 * Called when a placement is created at a position.
	 * @param placement The placement that has been created.
	 * @param position The position of the created placement.
	 */
	void onPlacementCreated(IPlacementDetails placement, Position position);
	
	/**
	 * Called when a placement changes at a position.
	 * @param placement The placement that has changed.
	 * @param position The position of the changed placmement.
	 */
	void onPlacementChanged(IPlacementDetails placement, Position position);
	
	/**
	 * Called when a placement at a position is removed.
	 * @param placement The placement that has been removed.
	 * @param position The position of the removed placement.
	 */
	void onPlacementRemoved(IPlacementDetails placement, Position position);
}
