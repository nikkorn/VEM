package gaia.networking.messages;

import gaia.networking.IMessage;
import gaia.world.Direction;
import gaia.world.Position;

/**
 * A message sent to a client to notify of a player that has changed positions.
 */
public class PlayerMoved implements IMessage {
	/**
	 * The id of the player that has moved.
	 */
	private String playerId;
	/**
	 * The new position.
	 */
	private Position position;
	/**
	 * The direction that the player has moved in to reach the new position.
	 */
	private Direction direction;
	
	/**
	 * Create a new instance of the PlayerMoved class.
	 * @param playerId The id of the player that has moved.
	 * @param target The target position.
	 * @param direction The direction that the player has moved in to reach the new position.
	 * @param Whether this move is actually to correct a players position.
	 */
	public PlayerMoved(String playerId, Position position, Direction direction) {
		this.playerId  = playerId;
		this.position  = position;
		this.direction = direction;
	}
	
	/**
	 * Get the id of the moving player.
	 * @return The id of the moving player.
	 */
	public String getPlayerId() {
		return playerId;
	}

	/**
	 * Get the target position of the move.
	 * @return The target position of the move.
	 */
	public Position getNewPosition() {
		return position;
	}
	
	/**
	 * Get the direction that the player has moved in to reach the new position.
	 * @return The direction that the player has moved in to reach the new position.
	 */
	public Direction getDirectionOfMovement() {
		return direction;
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.PLAYER_MOVED;
	}
}