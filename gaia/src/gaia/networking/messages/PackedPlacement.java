package gaia.networking.messages;

/**
 * A positioned and packed placement within a loaded chunk.
 */
public class PackedPlacement {
	/**
	 * The x/y position of the placement relative to the parent chunk.
	 */
	private short x, y;
	/**
	 * The packed integer representing:
	 *   - The placement type.
	 *   - The placement underlay.
	 *   - The placement overlay.
	 */
	private int packedComposition;
	
	/**
	 * Create a new instance of the PackedPlacement class.
	 * @param x The x position of the placement relative to the parent chunk.
	 * @param y The y position of the placement relative to the parent chunk.
	 * @param packedComposition The packed composition of the placement.
	 */
	public PackedPlacement(short x, short y, int packedComposition) {
		this.x                 = x; 
		this.y                 = y;
		this.packedComposition = packedComposition;
	}
	
	/**
	 * Get the x position of the placement relative to the parent chunk.
	 * @return The x position of the placement relative to the parent chunk.
	 */
	public short getX() {
		return this.x;
	}
	
	/**
	 * Get the y position of the placement relative to the parent chunk.
	 * @return The y position of the placement relative to the parent chunk.
	 */
	public short getY() {
		return this.y;
	}
	
	/**
	 * Get the packed composition of the placement, representing:
	 *   - The placement type.
	 *   - The placement underlay.
	 *   - The placement overlay.
	 * @return The packed composition of the placement.
	 */
	public int getComposition() {
		return this.packedComposition;
	}
}
