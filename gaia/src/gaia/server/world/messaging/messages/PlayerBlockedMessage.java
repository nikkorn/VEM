package gaia.server.world.messaging.messages;

import gaia.server.engine.IWorldEventsHandler;
import gaia.world.Position;

/**
 * A message containing the details of a player's failed attempt to move.
 */
public class PlayerBlockedMessage implements IWorldMessage {
	/**
	 * The id of the moving player.
	 */
	private String playerId;
	/**
	 * The position of the player.
	 */
	private Position position;
	
	/**
	 * Create a new instance of the PlayerBlockedMessage class.
	 * @param playerId The id of the player.
	 * @param position The position of the player.
	 */
	public PlayerBlockedMessage(String playerId, Position position) {
		this.playerId = playerId;
		this.position = position;
	}
	
	/**
	 * Get the id of the moving player.
	 * @return The id of the moving player.
	 */
	public String getPlayerId() {
		return playerId;
	}

	/**
	 * Get the position of the player.
	 * @return The position of the player.
	 */
	public Position getPosition() {
		return this.position;
	}
	
	@Override
	public void process(IWorldEventsHandler handler) {
		handler.onPlayerBlocked(playerId, position);
	}
}

