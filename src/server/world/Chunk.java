package server.world;

import server.Constants;
import server.world.generation.WorldGenerator;

/**
 * Provides chunk information derived from RNG and Perlin noise.
 */
public class Chunk {
	/**
	 * The static tiles that this chunk is composed of.
	 */
	private TileType[][] tiles = new TileType[Constants.WORLD_CHUNK_SIZE][Constants.WORLD_CHUNK_SIZE];
	/**
	 * The x/y positions of the chunk.
	 */
	private int x, y;
	/**
	 * Whether the chunk is active. Active chunks are ticked.
	 */
	private boolean isActive = false;
	
	/**
	 * Creates a new instance of the ChunkInformation class.
	 * @param x The x position of this chunk.
	 * @param y The y position of this chunk.
	 * @param worldGenerator The world generator.
	 */
	public Chunk(int x, int y, WorldGenerator worldGenerator) {
		// Set the chunk position.
		this.x = x;
		this.y = y;
		// Populate the static tile information.
		populateTileInformation(worldGenerator);
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
	 * Get whether the chunk is active.
	 * @return Whether the chunk is active.
	 */
	public boolean isActive() {
		return this.isActive;
	}
	
	/**
	 * Set whether the chunk is active.
	 * @param active Whether the chunk is active.
	 */
	public void setActive(boolean active) {
		this.isActive = active;
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
	 * Tick the chunk.
	 */
	public void tick() {
		
	}

	/**
	 * Populate the static tile information.
	 * @param worldGenerator The world generator.
	 */
	private void populateTileInformation(WorldGenerator worldGenerator) {
		int tileXOffset = this.x * Constants.WORLD_CHUNK_SIZE;
		int tileYOffset = this.y * Constants.WORLD_CHUNK_SIZE;
		for (int tileX = 0; tileX < Constants.WORLD_CHUNK_SIZE; tileX++) {
			for (int tileY = 0; tileY < Constants.WORLD_CHUNK_SIZE; tileY++) {
				this.tiles[tileX][tileY] = worldGenerator.getTileAt(tileX + tileXOffset, tileY + tileYOffset);
			}
		}
	}
}
