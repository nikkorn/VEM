package gaia.networking.messages;

import gaia.networking.IMessage;
import gaia.world.Direction;

/**
 * A message sent to the server requesting to move a player in a direction.
 */
public class MovePlayer implements IMessage {
	/*
	 * The direction of movement.
	 */
	private Direction direction;
	
	/**
	 * Create a new instance of the MovePlayer class.
	 * @param direction The direction to move in.
	 */
	public MovePlayer(Direction direction) {
		this.direction = direction;
	}
	
	/**
	 * Get the direction of movement.
	 * @return The direction of movement.
	 */
	public Direction getDirection() {
		return this.direction;
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.MOVE_PLAYER;
	}
}
