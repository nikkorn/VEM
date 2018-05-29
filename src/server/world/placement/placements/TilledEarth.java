package server.world.placement.placements;

import server.world.placement.Placement;
import server.world.placement.PlacementType;
import server.world.placement.state.TilledEarthState;

/**
 * A Tilled Earth placement.
 */
public class TilledEarth extends Placement<TilledEarthState> {

	@Override
	public PlacementType getType() {
		return PlacementType.TILLED_EARTH;
	}
}
