package gaia.server.world.messaging.messages;

import java.util.ArrayList;
import gaia.server.engine.IWorldEventsHandler;
import gaia.server.world.placements.IPlacementDetails;
import gaia.world.Position;

/**
 * A message containing the details of a placement change.
 */
public class PlacementChangedMessage implements IWorldMessage {
	/**
	 * The ids of any players who care about the placement change.
	 */
	private String[] playerIds;
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
	 * @param playerIds The ids of any players who care about the placement change.
	 * @param placement The placement details.
	 * @param position The position of the placement.
	 */
	public PlacementChangedMessage(ArrayList<String> playerIds, IPlacementDetails placement, Position position) {
		this.playerIds = playerIds.toArray(new String[playerIds.size()]);
		this.position  = position;
		this.placement = placement;
	}
	
	/**
	 * Get the ids of any players who care about the placement change.
	 * @return The ids of any players who care about the placement change.
	 */
	public String[] getPlayerIds() {
		return this.playerIds;
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
		handler.onPlacementChange(playerIds, (int)position.getX(), (int)position.getY(), placement);
	}
}
