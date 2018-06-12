package server.world.players;

import server.world.Position;

/**
 * Represents a player in the world.
 */
public class Player {
	/**
	 * The player id.
	 */
	private String id;
	/**
	 * The player position.
	 */
	private Position positon;

	/**
	 * Create an instance of the Player class.
	 * @param id The player id.
	 * @param position The initial player position.
	 */
	public Player(String id, Position position) {
		this.id      = id;
		this.positon = position;
	}
	
	/**
	 * Get the player id.
	 * @return The player id.
	 */
	public String getPlayerId() {
		return this.id;
	}

	/**
	 * Get the player position.
	 * @return The player position.
	 */
	public Position getPositon() {
		return positon;
	}
}
