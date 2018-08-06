package gaia.world;

import org.json.JSONObject;
import gaia.Constants;
import gaia.utils.BitPacker;

/**
 * Represents a world position.
 */
public class Position {
	/**
	 * The x position.
	 */
	private short x;
	/**
	 * The y position.
	 */
	private short y;
	/**
	 * The chunk x position.
	 */
	private short chunkX;
	/**
	 * The chunk y position.
	 */
	private short chunkY;
	
	/**
	 * Creates a new instance of the Position class.
	 * @param x The x position.
	 * @param y The y position.
	 */
	public Position(short x, short y) {
		this.setX(x);
		this.setY(y);
	}
	
	/**
	 * Get the x position.
	 * @return The x position.
	 */
	public short getX() {
		return x;
	}

	/**
	 * Set the x position.
	 * @param x The x position.
	 */
	public void setX(short x) {
		this.x      = x;
		this.chunkX = convertWorldToChunkPosition(x);
	}
	
	/**
	 * Get the y position.
	 * @return The y position.
	 */
	public short getY() {
		return y;
	}

	/**
	 * Set the y position.
	 * @param y The y position.
	 */
	public void setY(short y) {
		this.y      = y;
		this.chunkY = convertWorldToChunkPosition(y);
	}

	/**
	 * Gets the x position of the chunk that this position is within.
	 * @return The x position of the chunk that this position is within. 
	 */
	public short getChunkX() {
		return this.chunkX;
	}
	
	/**
	 * Gets the y position of the chunk that this position is within.
	 * @return The y position of the chunk that this position is within. 
	 */
	public short getChunkY() {
		return this.chunkY;
	}
	
	/**
	 * Get the position as a packed integer.
	 *   bits 0-15  - X position
	 *   bits 16-31 - Y position
	 * @return The position as a packed integer.
	 */
	public int asPackedInt() {
		// Pack the x position.
		int packed = BitPacker.pack(0, (int)this.x, 0, 16);
		// Pack the y position.
		packed = BitPacker.pack(packed, (int)this.y, 16, 16);
		// Return the packed value.
		return packed;
	}
	
	/**
	 * Create a position based on information parsed from a packed integer.
	 * @param packed The packed integer.
	 * @return The position.
	 */
	public static Position fromPackedInt(int packed) {
		short unpackedX = (short)BitPacker.unpack(packed, 0, 16);
		short unpackedY = (short)BitPacker.unpack(packed, 16, 16);
		return new Position(unpackedX, unpackedY);
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
	private static short convertWorldToChunkPosition(short position) {
		return (short) ((position / Constants.WORLD_CHUNK_SIZE) - (position < 0 ? 1 : 0));
	}
}
