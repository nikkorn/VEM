package gaia.world;

/**
 * Exposes the immutable details of a position.
 */
public interface IPositionDetails {
	
	/**
	 * Get the x position.
	 * @return The x position.
	 */
	short getX();
	
	/**
	 * Get the y position.
	 * @return The y position.
	 */
	short getY();
	
	/**
	 * Get the x chunk position.
	 * @return The x chunk position.
	 */
	short getChunkX();
	
	/**
	 * Get the x chunk position.
	 * @return The x chunk position.
	 */
	short getChunkY();
	
	/**
	 * Get a mutable copy of this position.
	 * The state of the copy will not be changed if the original is modified.
	 * @return A copy of this position.
	 */
	public Position copy();
}
