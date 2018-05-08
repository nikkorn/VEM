package server.world.generation;

import server.Constants;
import server.world.TileType;

/**
 * Provides chunk information derived from RNG and Perlin noise.
 */
public class ChunkInformation {
	/**
	 * The static tiles that this chunk is composed of.
	 */
	private TileType[][] tiles = new TileType[Constants.WORLD_CHUNK_SIZE][Constants.WORLD_CHUNK_SIZE];
	/**
	 * The world seed.
	 */
	private long seed;
	/**
	 * The x/y positions of the chunk.
	 */
	private int x, y;
	
	/**
	 * Creates a new instance of the ChunkInformation class.
	 * @param x The x position of this chunk.
	 * @param y The y position of this chunk.
	 * @param seed The world seed.
	 */
	public ChunkInformation(int x, int y, long seed) {
		// Set the chunk position.
		this.x = x;
		this.y = y;
		// Populate the static tile information.
		populateTileInformation();
	}
	
	/**
	 * Get the x position of the chunk.
	 * @return The x position of the chunk.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Get the y position of the chunk.
	 * @return The y position of the chunk.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Get the type of tile at the specified position.
	 * @param x The x position of the tile.
	 * @param y The y position of the tile.
	 * @return The type of tile at the specified position.
	 */
	public TileType getTileType(int x, int y) {
		return tiles[x % Constants.WORLD_CHUNK_SIZE][y % Constants.WORLD_CHUNK_SIZE];
	}

	/**
	 * Populate the static tile information.
	 */
	private void populateTileInformation() {
		// TODO Auto-generated method stub
	}
}
