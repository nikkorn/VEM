package gaia.networking.messages;

/**
 * A positioned and packed placements within a loaded chunk.
 */
public class PackedPlacement {
	/**
	 * The x/y position of the placements relative to the parent chunk.
	 */
	private short x, y;
	/**
	 * The packed integer representing:
	 *   - The placements type.
	 *   - The placements underlay.
	 *   - The placements overlay.
	 */
	private int packedComposition;
	
	/**
	 * Create a new instance of the PackedPlacement class.
	 * @param x The x position of the placements relative to the parent chunk.
	 * @param y The y position of the placements relative to the parent chunk.
	 * @param packedComposition The packed composition of the placements.
	 */
	public PackedPlacement(short x, short y, int packedComposition) {
		this.x                 = x; 
		this.y                 = y;
		this.packedComposition = packedComposition;
	}
	
	/**
	 * Get the x position of the placements relative to the parent chunk.
	 * @return The x position of the placements relative to the parent chunk.
	 */
	public short getX() {
		return this.x;
	}
	
	/**
	 * Get the y position of the placements relative to the parent chunk.
	 * @return The y position of the placements relative to the parent chunk.
	 */
	public short getY() {
		return this.y;
	}
	
	/**
	 * Get the packed composition of the placements, representing:
	 *   - The placements type.
	 *   - The placements underlay.
	 *   - The placements overlay.
	 * @return The packed composition of the placements.
	 */
	public int getComposition() {
		return this.packedComposition;
	}
}
