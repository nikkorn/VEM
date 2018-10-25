package gaia.world.players;

import gaia.world.Direction;
import gaia.world.Position;

/**
 * Details of a player positioned in the world.
 */
public class PositionedPlayer {
	/**
	 * The player id.
	 */
	private String playerId;
	/**
	 * The player position.
	 */
	private Position position;
	/**
	 * The direction that the player is facing.
	 */
	private Direction direction;

	/**
	 * Create an instance of the PositionedPlayer class.
	 * @param playerId The player id.
	 * @param position The player position.
	 * @param facingDirection The facing direction of the player.
	 */
	public PositionedPlayer(String playerId, Position position, Direction facingDirection) {
		this.playerId  = playerId;
		this.position  = new Position(position.getX(), position.getY());
		this.direction = facingDirection;
	}
	
	/**
	 * Get the player id.
	 * @return The player id.
	 */
	public String getPlayerId() {
		return this.playerId;
	}

	/**
	 * Get the player position.
	 * @return The player position.
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Get the facing direction of the player.
	 * @return The facing direction of the player.
	 */
	public Direction getFacingDirection() {
		return this.direction;
	}
	
	/**
	 * Get the world position that the player is facing.
	 * @param position The player position.
	 * @param facing The facing direction of the player.
	 * @return The world position that the player is facing.
	 */
	public static Position getFacingPosition(Position position, Direction facing) {
		switch(facing) {
			case UP:
				return new Position(position.getX(), (short) (position.getY() + 1));
			case DOWN:
				return new Position(position.getX(), (short) (position.getY() - 1));
			case LEFT:
				return new Position((short) (position.getX() - 1), position.getY());
			case RIGHT:
				return new Position((short) (position.getX() + 1), position.getY());
			default:
				return new Position(position.getX(), (short) (position.getY() - 1));
		}
	}
}
