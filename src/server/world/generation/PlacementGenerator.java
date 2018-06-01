package server.world.generation;

import server.world.tile.TileType;
import server.world.tile.placement.Placement;

/**
 * Generator for initial chunk placements.
 */
public class PlacementGenerator {
	/**
	 * The world seed.
	 */
	private long seed;
	
	/**
	 * Creates a new instance of the PlacementGenerator class.
	 * @param seed The world seed.
	 */
	public PlacementGenerator(long seed) {
		this.seed = seed;
	}
	
	/**
	 * Generate initial placements for a chunk.
	 * @param tiles The chunk tiles.
	 * @param chunkXPosition The x position of the chunk.
	 * @param chunkYPosition The y position of the chunk.
	 * @return The initial placements for a chunk.
	 */
	public Placement[][] generateChunkPlacements(TileType[][] tiles, int chunkXPosition, int chunkYPosition) {
		return null;
	}
}