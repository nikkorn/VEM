package server.world;

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
	 * The chunk position.
	 */
	private Position chunkPosition;
	
	/**
	 * Creates a new instance of the Position class.
	 * @param x The x position.
	 * @param y The y position.
	 */
	public Position(int x, int y) {
		this.x             = x;
		this.y             = y;
		this.chunkPosition = getChunkPosition(x, y);
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
		this.x = x;
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
		this.y = y;
	}

	/**
	 * Gets the position of the chunk that this position is within.
	 * @return The position of the chunk that this position is within. 
	 */
	public Position getChunkPosition() {
		return this.chunkPosition;
	}
	
	/**
	 * Gets the position of the chunk that this position is within. 
	 * @param x The x position.
	 * @param y The y position.
	 * @return The chunk position.
	 */
	public static Position getChunkPosition(int x, int y) {
		return null; // TODO
	}
}
