package gaia.server.world.players.requests;

import gaia.server.world.Direction;
import gaia.server.world.World;

/**
 * A request to move in a world.
 */
public class MoveRequest extends PlayerRequest {
	/**
	 * The direction the player is requesting to move in.
	 */
	private Direction direction;

	/**
	 * Create a new instance of the MoveRequest class.
	 * @param playerId The id of the player requesting to join.
	 * @param direction The direction the player is requesting to move in.
	 */
	public MoveRequest(String playerId, Direction direction) {
		super(playerId);
		this.direction = direction;
	}

	@Override
	public void satisfy(World world) {
		// Attempt to move the player in the world.
		world.getPlayers().movePlayer(this.getRequestingPlayerId(), this.direction, world);
	}
}
