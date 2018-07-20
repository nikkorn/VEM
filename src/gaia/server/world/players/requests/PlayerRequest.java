package gaia.server.world.players.requests;

import gaia.server.engine.Request;

/**
 * A request from a player.
 */
public abstract class PlayerRequest extends Request {
	/**
	 * The player id.
	 */
	private String playerId;
	
	/**
	 * Creates a new instance of the PlayerRequest class.
	 * @param playerId The id of the requesting player.
	 */
	public PlayerRequest(String playerId) {
		this.playerId = playerId;
	}
	
	/**
	 * Get the id of the requesting player.
	 * @return The id of the requesting player.
	 */
	protected String getRequestingPlayerId() {
		return this.playerId;
	}
}
