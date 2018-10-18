package gaia.server.world.messaging.messages;

import java.util.Arrays;
import java.util.List;
import gaia.server.engine.IWorldEventsHandler;
import gaia.server.world.items.container.IContainerDetails;
import gaia.world.Position;

/**
 * A message containing the details of an added container.
 */
public class ContainerAddedMessage implements IWorldMessage {
	/**
	 * The ids of any players who care about the added container.
	 */
	private String[] playerIds;
	/**
	 * The position of the container.
	 */
	private Position position;
	/**
	 * The container details.
	 */
	private IContainerDetails container;
	
	/**
	 * Create a new instance of the ContainerAddedMessage class.
	 * @param playerIds The ids of any players who care about the added container.
	 * @param container The container details.
	 * @param position The position of the container.
	 */
	public ContainerAddedMessage(List<String> playerIds, IContainerDetails container, Position position) {
		this.playerIds = playerIds.toArray(new String[playerIds.size()]);
		this.position  = position;
		this.container = container;
	}
	
	/**
	 * Create a new instance of the ContainerAddedMessage class.
	 * @param playerId The id of the player who cares about the added container.
	 * @param container The container details.
	 * @param position The position of the container.
	 */
	public ContainerAddedMessage(String playerId, IContainerDetails container, Position position) {
		this(Arrays.asList(playerId), container, position);
	}
	
	@Override
	public void process(IWorldEventsHandler handler) {
		// There is nothing to do if there are no players that care about the added container.
		if (playerIds.length == 0) {
			return;
		}
		handler.onContainerAdd(playerIds, position, container);
	}
}