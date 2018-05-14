package server.players;

/**
 * Represents a connected player.
 */
public class Player {
	/**
	 * The player id.
	 */
	private String id;
	
	/**
	 * Create an instance of the Player class.
	 * @param id The player id.
	 */
	public Player(String id) {
		this.id = id;
	}
	
	/**
	 * Get the player id.
	 * @return The player id.
	 */
	public String getPlayerId() {
		return this.id;
	}
}
