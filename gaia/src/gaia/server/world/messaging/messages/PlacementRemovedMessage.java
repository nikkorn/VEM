package gaia.server.world.messaging.messages;

import java.util.Arrays;
import java.util.List;
import gaia.server.engine.IWorldEventsHandler;
import gaia.world.PlacementType;
import gaia.world.Position;

/**
 * A message containing the details of a placement delete.
 */
public class PlacementRemovedMessage implements IWorldMessage {
	/**
	 * The ids of any players who care about the placement change.
	 */
	private String[] playerIds;
	/**
	 * The position of the placement.
	 */
	private Position position;
	/**
	 * The expected placement type.
	 */
	private PlacementType expectedPlacementType;
	
	/**
	 * Create a new instance of the PlacementRemoveMessage class.
	 * @param playerIds The ids of any players who care about the placement deletion.
	 * @param expectedPlacementType The expected placement type.
	 * @param position The position of the placement.
	 */
	public PlacementRemovedMessage(List<String> playerIds, PlacementType expectedPlacementType, Position position) {
		this.playerIds             = playerIds.toArray(new String[playerIds.size()]);
		this.position              = position;
		this.expectedPlacementType = expectedPlacementType;
	}
	
	/**
	 * Create a new instance of the PlacementDeletedMessage class.
	 * @param playerId The id of the player who cares about the placement deletion.
	 * @param expectedPlacementType The expected placement type.
	 * @param position The position of the placement.
	 */
	public PlacementRemovedMessage(String playerId, PlacementType expectedPlacementType, Position position) {
		this(Arrays.asList(playerId), expectedPlacementType, position);
	}
	
	@Override
	public void process(IWorldEventsHandler handler) {
		// There is nothing to do if there are no players that care about the placement being removed.
		if (playerIds.length > 0) {
			handler.onPlacementRemove(playerIds, (int)position.getX(), (int)position.getY(), expectedPlacementType);
		}
	}
}