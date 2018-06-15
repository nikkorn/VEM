package server.world.players.requests;

import server.ServerConsole;
import server.world.World;

/**
 * A request to join a world.
 */
public class JoinRequest extends PlayerRequest {

	/**
	 * Create a new instance of the JoinRequest class.
	 * @param playerId The id of the player requesting to join.
	 */
	public JoinRequest(String playerId) {
		super(playerId);
	}

	@Override
	public void satisfy(World world) {
		ServerConsole.writeInfo("Player '" + this.getRequestingPlayerId() + "' is attempting to join.");
		// Attempt to add the player to the world.
		world.getPlayers().addPlayer(this.getRequestingPlayerId(), world);
	}
}
