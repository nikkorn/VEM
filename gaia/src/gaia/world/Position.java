package gaia.world;

import org.json.JSONObject;
import gaia.Constants;
import gaia.utils.BitPacker;

/**
 * Represents a world position.
 */
public class Position implements IPositionDetails {
	/** 
	 * The number of tiles on either axis from the world origin to world edge.
	 */
	private static int WORLD_ORIGIN_TO_EDGE_TILES = Constants.WORLD_SIZE / 2;
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
	public Position(int x, int y) {
		this((short)x, (short)y);
	}
	
	/**
	 * Creates a new instance of the Position class.
	 * @param x The x position.
	 * @param y The y position.
	 */
	public Position(short x, short y) {
		// Check to make sure that the position is valid.
		if (!Position.isValid(x, y)) {
			throw new InvalidPositionException(x, y);
		}
		this.setX(x);
		this.setY(y);
	}
	
	/**
	 * Get the x position.
	 * @return The x position.
	 */
	@Override
	public short getX() {
		return x;
	}

	/**
	 * Set the x position.
	 * @param x The x position.
	 */
	public void setX(short x) {
		// Check to make sure that the position is valid.
		if (!Position.isValid(x, y)) {
			throw new InvalidPositionException(x, y);
		}
		this.x      = x;
		this.chunkX = convertWorldToChunkPosition(x);
	}
	
	/**
	 * Get the y position.
	 * @return The y position.
	 */
	@Override
	public short getY() {
		return y;
	}

	/**
	 * Set the y position.
	 * @param y The y position.
	 */
	public void setY(short y) {
		// Check to make sure that the position is valid.
		if (!Position.isValid(x, y)) {
			throw new InvalidPositionException(x, y);
		}
		this.y      = y;
		this.chunkY = convertWorldToChunkPosition(y);
	}

	/**
	 * Gets the x position of the chunk that this position is within.
	 * @return The x position of the chunk that this position is within. 
	 */
	@Override
	public short getChunkX() {
		return this.chunkX;
	}
	
	/**
	 * Gets the y position of the chunk that this position is within.
	 * @return The y position of the chunk that this position is within. 
	 */
	@Override
	public short getChunkY() {
		return this.chunkY;
	}
	
	/**
	 * Gets whether this position references the same world tile as the specified position.
	 * @param position The position to compare.
	 * @return Whether this position references the same world tile as the specified position.
	 */
	public boolean matches(Position position) {
		// There is no way the positions can match if one is null.
		if (position == null) {
			return false;
		}
		// Return whether the both positions reference the same world tile. 
		return this.getX() == position.getX() && this.getY() == position.getY();
	}
	
	/**
	 * Gets whether this position is within the specified distance of another position.
	 * @param position The positon to check.
	 * @param distance The distance.
	 * @return Whether this position is within the specified distane of another position.
	 */
	public boolean isWithinDistanceOf(Position position, int distance) {
		// Check the x axis.
		if (this.x < (position.getX() - distance) || this.x > (position.getX() + distance)) {
			return false;
		}
		// Check the y axis.
		if (this.y < (position.getY() - distance) || this.y > (position.getY() + distance)) {
			return false;
		}
		// This position is within distance of the other position.
		return true;
	}
	
	/**
	 * Get the adjacent position in the specified direction, or null if the adjacent position is not valid.
	 * @param direction The direction of the adjacent position.
	 * @return The adjacent position in the specified direction, or null if the adjacent position is not valid.
	 */
	public Position getAdjacentPosition(Direction direction) {
		short adjacentPositionX = this.getX();
		short adjacentPositionY = this.getY();
		switch(direction) {
			case UP:
				adjacentPositionY += 1;
				break;
			case DOWN:
				adjacentPositionY -= 1;
				break;
			case LEFT:
				adjacentPositionX -= 1;
				break;
			case RIGHT:
				adjacentPositionX += 1;
				break;
		}
		// Return the adjacent position if it is valid, or null if it is not.
		return Position.isValid(adjacentPositionX, adjacentPositionY) ? new Position(adjacentPositionX, adjacentPositionY) : null;
	}
	
	/**
	 * Get the position as a packed integer.
	 *   bits 0-15  - X position
	 *   bits 16-31 - Y position
	 * @return The position as a packed integer.
	 */
	public int asPackedInt() {
		return Position.asPackedInt(this.x, this.y);
	}
	
	/**
	 * Get the x/y position as a packed integer.
	 *   bits 0-15  - X position
	 *   bits 16-31 - Y position
	 * @return The x/y position as a packed integer.
	 */
	public static int asPackedInt(short x, short y) {
		// Pack the x position.
		int packed = BitPacker.pack(0, (int)x, 0, 16);
		// Pack the y position.
		packed = BitPacker.pack(packed, (int)y, 16, 16);
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
	public static short convertWorldToChunkPosition(short position) {
		return convertWorldToChunkPosition((int)position);
	}
	
	/**
	 * Converts a world postition to a chunk position.
	 * @param position The world position.
	 * @return The chunk position.
	 */
	public static short convertWorldToChunkPosition(int position) {
		return (short) Math.floor(position / (double) Constants.WORLD_CHUNK_SIZE);
	}
	
	/**
	 * Gets whether the specified x/y position is valid and within the world bounds.
	 * @param x The x position.
	 * @param y The y position.
	 * @return Whether the specified x/y position is valid and within the world bounds.
	 */
	public static boolean isValid(short x, short y) {
		return isValid((int)x, (int)y);
	}
	
	/**
	 * Gets whether the specified x/y position is valid and within the world bounds.
	 * @param x The x position.
	 * @param y The y position.
	 * @return Whether the specified x/y position is valid and within the world bounds.
	 */
	public static boolean isValid(int x, int y) {
		// Return whether either the x or y positions exceed the world boundaries.
		return x > -WORLD_ORIGIN_TO_EDGE_TILES &&
				x < WORLD_ORIGIN_TO_EDGE_TILES &&
				y > -WORLD_ORIGIN_TO_EDGE_TILES &&
				y < WORLD_ORIGIN_TO_EDGE_TILES;
	}
}
