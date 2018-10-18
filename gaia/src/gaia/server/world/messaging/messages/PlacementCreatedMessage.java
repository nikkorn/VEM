package gaia.server.world.messaging.messages;

import java.util.Arrays;
import java.util.List;
import gaia.server.engine.IWorldEventsHandler;
import gaia.server.world.placements.IPlacementDetails;
import gaia.world.Position;

/**
 * A message containing the details of a placement creation.
 */
public class PlacementCreatedMessage implements IWorldMessage {
	/**
	 * The ids of any players who care about the placement creation.
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
	 * Create a new instance of the PlacementCreatedMessage class.
	 * @param playerIds The ids of any players who care about the placement creation.
	 * @param placement The placement details.
	 * @param position The position of the placement.
	 */
	public PlacementCreatedMessage(List<String> playerIds, IPlacementDetails placement, Position position) {
		this.playerIds = playerIds.toArray(new String[playerIds.size()]);
		this.position  = position;
		this.placement = placement;
	}
	
	/**
	 * Create a new instance of the PlacementCreatedMessage class.
	 * @param playerId The id of the player who cares about the placement creation.
	 * @param placement The placement details.
	 * @param position The position of the placement.
	 */
	public PlacementCreatedMessage(String playerId, IPlacementDetails placement, Position position) {
		this(Arrays.asList(playerId), placement, position);
	}
	
	@Override
	public void process(IWorldEventsHandler handler) {
		// There is nothing to do if there are no players that care about the placement change.
		if (playerIds.length == 0) {
			return;
		}
		handler.onPlacementCreate(playerIds, position, placement);
	}
}
