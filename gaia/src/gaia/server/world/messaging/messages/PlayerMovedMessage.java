package gaia.server.world.messaging.messages;

import gaia.server.engine.IWorldEventsHandler;
import gaia.world.Direction;
import gaia.world.Position;

/**
 * A message containing the details of a successful player move.
 */
public class PlayerMovedMessage implements IWorldMessage {
	/**
	 * The id of the moving player.
	 */
	private String playerId;
	/**
	 * The new position of the moving player.
	 */
	private Position target;
	/**
	 * The direction of the movement.
	 */
	private Direction direction;
	
	/**
	 * Create a new instance of the PlayerPositionChangedMessage class.
	 * @param playerId The id of the player.
	 * @param target The new position of the moving player.
	 * @param direction The direction of the movement.
	 */
	public PlayerMovedMessage(String playerId, Position target, Direction direction) {
		this.playerId  = playerId;
		this.target    = target;
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
	 * Get the new position of the player.
	 * @return The new position of the player.
	 */
	public Position getTarget() {
		return this.target;
	}
	
	/**
	 * Get the direction of the movement.
	 * @return The direction of the movement.
	 */
	public Direction getDirection() {
		return this.direction;
	}
	
	@Override
	public void process(IWorldEventsHandler handler) {
		handler.onPlayerMove(playerId, target, direction);
	}
}

