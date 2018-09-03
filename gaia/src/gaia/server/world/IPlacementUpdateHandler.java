package gaia.server.world;

import gaia.server.world.placements.Placement;
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
	void onPlacementChange(Placement placement, Position position);
}
