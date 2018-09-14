package gaia.client.gamestate.players;

/**
 * An interface to expose immutable players details.
 */
public interface IPlayersDetails {
	
	/**
	 * Get the clients player details.
	 * @return The clients player details.
	 */
	public IPlayerDetails getClientsPlayerDetails();
	
	/**
	 * Get the details of the player with the player id.
	 * @param playerId The player id.
	 * @return The details of the  player with the player id.
	 */
	public IPlayerDetails getPlayerDetails(String playerId);
	
	/**
	 * Get the details of all of the players.
	 * @return The details of all of the players.
	 */
	public IPlayerDetails[] getAll();
}
