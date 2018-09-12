package gaia.networking.messages;

import gaia.networking.IMessage;
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
	 * The target position.
	 */
	private Position target;
	/**
	 * Whether this move is actually to correct a players position.
	 */
	private boolean isCorrection;
	
	/**
	 * Create a new instance of the PlayerMoved class.
	 * @param playerId The id of the player that has moved.
	 * @param target The target position.
	 * @param Whether this move is actually to correct a players position.
	 */
	public PlayerMoved(String playerId, Position target, boolean isCorrection) {
		this.playerId     = playerId;
		this.target       = target;
		this.isCorrection = isCorrection;
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
	public Position getTargetPosition() {
		return target;
	}
	
	/**
	 * Get whether this move is actually to correct a players position.
	 * @return Whether this move is actually to correct a players position.
	 */
	public boolean isCorrection() {
		return this.isCorrection;
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.PLAYER_MOVED;
	}
}