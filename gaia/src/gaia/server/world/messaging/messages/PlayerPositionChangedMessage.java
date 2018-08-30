package gaia.server.world.messaging.messages;

import gaia.server.engine.IWorldEventsHandler;
import gaia.world.Position;

/**
 * A message containing the details of a successful player move.
 */
public class PlayerPositionChangedMessage implements IWorldMessage {
	/**
	 * The id of the moving player.
	 */
	private String playerId;
	/**
	 * The new position of the moving player.
	 */
	private Position newPosition;
	
	/**
	 * Create a new instance of the PlayerPositionChangedMessage class.
	 * @param playerId The id of the player.
	 * @param position The new position of the player.
	 */
	public PlayerPositionChangedMessage(String playerId, Position position) {
		this.playerId    = playerId;
		this.newPosition = position;
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
	public void process(IWorldEventsHandler handler) {
		handler.onPlayerPositionChange(playerId, newPosition.getX(), newPosition.getY());
	}
}

