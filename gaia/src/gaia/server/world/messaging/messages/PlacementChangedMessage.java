package gaia.server.world.messaging.messages;

import gaia.server.engine.IWorldEventsHandler;
import gaia.server.world.placements.IPlacementDetails;
import gaia.world.Position;

/**
 * A message containing the details of a placement change.
 */
public class PlacementChangedMessage implements IWorldMessage {
	/**
	 * The position of the placement.
	 */
	private Position position;
	/**
	 * The placement details.
	 */
	private IPlacementDetails placement;
	
	/**
	 * Create a new instance of the PlacementChangedMessage class.
	 * @param placement The placement details.
	 * @param position The position of the placement.
	 */
	public PlacementChangedMessage(IPlacementDetails placement, Position position) {
		this.position  = position;
		this.placement = placement;
	}
	
	/**
	 * Get the placement details.
	 * @return The placement details.
	 */
	public IPlacementDetails getPlacement() {
		return this.placement;
	}

	/**
	 * Get the position of the placement.
	 * @return The position of the placement.
	 */
	public Position getPosition() {
		return position;
	}
	
	@Override
	public void process(IWorldEventsHandler handler) {
		handler.onPlacementChange(position.getX(), position.getY(), placement);
	}
}
