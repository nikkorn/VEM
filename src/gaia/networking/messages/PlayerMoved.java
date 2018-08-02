package gaia.networking.messages;

import gaia.networking.IMessage;
import gaia.world.Position;

/**
 * A message sent to a client ot notify of a player that has changed positions.
 */
public class PlayerMoved implements IMessage {
	/**
	 * The id of the player that has moved.
	 */
	private String playerId;
	/**
	 * The direction of movement.
	 */
	private Position newPosition;
	
	/**
	 * Create a new instance of the PlayerMoved class.
	 * @param playerId The id of the player that has moved.
	 * @param newPosition The new position of the player.
	 */
	public PlayerMoved(String playerId, Position newPosition) {
		this.playerId    = playerId;
		this.newPosition = newPosition;
	}
	
	/**
	 * Get the id of the moving player.
	 * @return The id of the moving player.
	 */
	public String getPlayerId() {
		return playerId;
	}

	/**
	 * Get the new position of the player.
	 * @return The new position of the player.
	 */
	public Position getNewPosition() {
		return newPosition;
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.PLAYER_MOVED;
	}
}