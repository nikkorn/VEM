package gaia.networking.messages;

import gaia.networking.IMessage;
import gaia.world.Position;
import gaia.world.items.container.ContainerType;

/**
 * A message sent to a client notifying them of a container being removed from the world.
 */
public class ContainerRemoved implements IMessage {
	/**
	 * The position of the container.
	 */
	private Position position;
	/**
	 * The expected type of the container being removed.
	 */
	private ContainerType expectedContainerType;

	/**
	 * Create a new instance of the ContainerRemoved class.
	 * @param position The position of the container.
	 * @param expectedContainerType The expected type of the container being removed.
	 */
	public ContainerRemoved(Position position, ContainerType expectedContainerType) {
		this.position              = position; 
		this.expectedContainerType = expectedContainerType;
	}
	
	/**
	 * Get the position of the container.
	 * @return The position of the container.
	 */
	public Position getPosition() {
		return this.position;
	}
	
	/**
	 * Get the expected type of the container being removed.
	 * @return The expected type of the container being removed.
	 */
	public ContainerType getExpectedContainerType() {
		return this.expectedContainerType;
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.CONTAINER_REMOVED;
	}
}
