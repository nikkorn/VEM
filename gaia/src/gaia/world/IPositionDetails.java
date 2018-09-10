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
}
