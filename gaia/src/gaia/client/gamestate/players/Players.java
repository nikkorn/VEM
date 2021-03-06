package gaia.client.gamestate.players;

import java.util.HashMap;
import gaia.world.Position;

/**
 * The client-side representation of the collection of connected clients.
 */
public class Players implements IPlayersDetails {
	/**
	 * The map of players.
	 */
	private HashMap<String, Player> players = new HashMap<String, Player>();
	/**
	 * The client's player name.
	 */
	private String clientPlayerId;
	
	/**
	 * Create a new instance of the Players class.
	 * @param clientPlayerId The player id of the client.
	 */
	public Players(String clientPlayerId) {
		this.clientPlayerId = clientPlayerId;
	}
	
	/**
	 * Get the clients player.
	 * @return The clients player.
	 */
	public Player getClientsPlayer() {
		return players.get(clientPlayerId);
	}
	
	/**
	 * Get the player with the player id.
	 * @param playerId The player id.
	 * @return The player with the player id.
	 */
	public Player getPlayer(String playerId) {
		return this.players.get(playerId);
	}
	
	/**
	 * Add a player.
	 * @param player The player to add.
	 */
	public void addPlayer(Player player) {
		this.players.put(player.getPlayerId(), player);
	}
	
	/**
	 * Get the player at the specified position, or null if there is no player at the position.
	 * @param position The position to check for a player at.
	 * @return The player at the specified position, or null if there is no player at the position.
	 */
	public Player getPlayerAtPosition(Position position) {
		for (Player player : this.players.values()) {
			if (player.getPosition().matches(position)) {
				return player;
			}
		}
		// There was no player at this position.
		return null;
	}

	/**
	 * Get the clients player details.
	 * @return The clients player details.
	 */
	@Override
	public IPlayerDetails getClientsPlayerDetails() {
		return getClientsPlayer();
	}

	/**
	 * Get the details of the player with the player id.
	 * @param playerId The player id.
	 * @return The details of the  player with the player id.
	 */
	@Override
	public IPlayerDetails getPlayerDetails(String playerId) {
		return getPlayer(playerId);
	}
	
	/**
	 * Get the details of all of the players.
	 * @return The details of all of the players.
	 */
	@Override
	public IPlayerDetails[] getAll() {
		return this.players.values().toArray(new IPlayerDetails[this.players.size()]);
	}
}
