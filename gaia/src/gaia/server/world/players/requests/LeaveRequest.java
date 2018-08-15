package gaia.server.world.players.requests;

import gaia.server.world.World;

/**
 * A request to leave a world.
 */
public class LeaveRequest extends PlayerRequest {

	/**
	 * Create a new instance of the LeaveRequest class.
	 * @param playerId The id of the player requesting to leave.
	 */
	public LeaveRequest(String playerId) {
		super(playerId);
	}

	@Override
	public void satisfy(World world) {
		world.getPlayers().removePlayer(this.getRequestingPlayerId(), world);
	}
}
