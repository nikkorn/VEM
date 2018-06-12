package server.world.players.requests;

import server.world.World;

/**
 * A request from a player.
 */
public abstract class PlayerRequest {
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
	
	/**
	 * Satisfy the request.
	 * @param playerId The id of the requesting player.
	 * @param world The world to interact with in order to satisfy the request.
	 */
	public abstract void satisfy(World world);
}
