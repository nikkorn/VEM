package gaia.networking.messages;

import gaia.networking.IMessage;
import gaia.world.Position;

/**
 * A message sent to a client to notify that they were blocked in their attempt to change position.
 */
public class PlayerBlocked implements IMessage {
	/**
	 * The player's position.
	 */
	private Position position;
	
	/**
	 * Create a new instance of the PlayerBlocked class.
	 * @param position The player's position.
	 */
	public PlayerBlocked(Position position) {
		this.position = position;
	}

	/**
	 * Get the player's position.
	 * @return The player's position.
	 */
	public Position getPosition() {
		return position;
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.PLAYER_BLOCKED;
	}
}