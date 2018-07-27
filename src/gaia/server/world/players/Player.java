package gaia.server.world.players;

import java.util.ArrayList;

import gaia.Position;
import gaia.server.world.chunk.Chunk;

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
	 * The list of id's of chunks that this player has visited (been in the vicinity of).
	 */
	private ArrayList<String> chunksVisited = new ArrayList<String>();

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
	
	/**
	 * Add a chunk that the player has visited.
	 * Keeping track of chunk visitors helps us determine who has been here.
	 * @param chunk The visited chunk.
	 */
	public void addVisitedChunk(Chunk chunk) {
		this.chunksVisited.add(chunk.getKey());
	}
	
	/**
	 * Get whether this player has visited the specified chunk before.
	 * @param chunk The chunk.
	 * @return Whether this player has visited the specified chunk before.
	 */
	public boolean hasVisitedChunk(Chunk chunk) {
		return this.chunksVisited.contains(chunk.getKey());
	}
}
