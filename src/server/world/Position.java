package server.world;

import org.json.JSONObject;

import server.Constants;

/**
 * Represents a world position.
 */
public class Position {
	/**
	 * The x position.
	 */
	private int x;
	/**
	 * The y position.
	 */
	private int y;
	/**
	 * The chunk x position.
	 */
	private int chunkX;
	/**
	 * The chunk y position.
	 */
	private int chunkY;
	
	/**
	 * Creates a new instance of the Position class.
	 * @param x The x position.
	 * @param y The y position.
	 */
	public Position(int x, int y) {
		this.setX(x);
		this.setY(y);
	}
	
	/**
	 * Get the x position.
	 * @return The x position.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Set the x position.
	 * @param x The x position.
	 */
	public void setX(int x) {
		this.x      = x;
		this.chunkX = convertWorldToChunkPosition(x);
	}
	
	/**
	 * Get the y position.
	 * @return The y position.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Set the y position.
	 * @param y The y position.
	 */
	public void setY(int y) {
		this.y      = y;
		this.chunkY = convertWorldToChunkPosition(y);
	}

	/**
	 * Gets the x position of the chunk that this position is within.
	 * @return The x position of the chunk that this position is within. 
	 */
	public int getChunkX() {
		return this.chunkX;
	}
	
	/**
	 * Gets the y position of the chunk that this position is within.
	 * @return The y position of the chunk that this position is within. 
	 */
	public int getChunkY() {
		return this.chunkY;
	}
	
	/**
	 * Get the position as JSON.
	 * @return The position as JSON.
	 */
	public static JSONObject serialise(Position position) {
		// Create the JSON object that will represent the position.
		JSONObject positionState = new JSONObject();
		// Set the x position.
		positionState.put("x", position.getX());
		// Set the y position.
		positionState.put("y", position.getY());
		// Return the position state.
		return positionState;
	}
	
	/**
	 * Converts a world postition to a chunk position.
	 * @param position The world position.
	 * @return The chunk position.
	 */
	private static int convertWorldToChunkPosition(int position) {
		return (position / Constants.WORLD_CHUNK_SIZE) - (position < 0 ? 1 : 0);
	}
}
