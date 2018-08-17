package gaia.client.gamestate;

import java.util.HashMap;

/**
 * The client-side representation of the collection of connected clients.
 */
public class Players {
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
}
