package gaia.server.world.messaging.messages;

import gaia.server.engine.IWorldEventsHandler;
import gaia.server.world.placements.IPlacementDetails;
import gaia.world.Position;

/**
 * A message containing the details of a placement load for a player.
 */
public class PlacementLoadedMessage implements IWorldMessage {
	/**
	 * The id of the player who cares about the placement load.
	 */
	private String playerId;
	/**
	 * The position of the placement.
	 */
	private Position position;
	/**
	 * The placement details.
	 */
	private IPlacementDetails placement;
	
	/**
	 * Create a new instance of the PlacementLoadedMessage class.
	 * @param playerId The player id.
	 * @param placement The placement details.
	 * @param position The position of the placement.
	 */
	public PlacementLoadedMessage(String playerId, IPlacementDetails placement, Position position) {
		this.playerId  = playerId;
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
		handler.onPlacementLoad(playerId, position.getX(), position.getY(), placement);
	}
}
