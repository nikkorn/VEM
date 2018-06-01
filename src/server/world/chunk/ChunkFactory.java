package server.world.chunk;

import org.json.JSONArray;
import org.json.JSONObject;
import server.Constants;
import server.world.generation.WorldGenerator;
import server.world.tile.TileFactory;
import server.world.tile.TileType;
import server.world.tile.placement.Placement;

/**
 * A factory for chunk entities.
 */
public class ChunkFactory {
	
	/**
	 * Create a chunk.
	 * This chunk does not already exist (is not defined in the world save directory)
	 * so it is the job of the world generator to spawn initial placements.
	 * @param generator The world generator.
	 * @param x The x position of the chunk.
	 * @param y The y position of the chunk.
	 * @return The created chunk.
	 */
	public static Chunk createChunk(WorldGenerator generator, int x, int y) {
		// Firstly, create the static world tiles for the chunk.
		TileType[][] tiles = createChunkTiles(generator, x, y);
		// This is the first time we are creating this chunk (there is no 
		// chunk state saved to disk) so it is the responsibility of the world
		// generator to create some nice initial naturally-occurring placements.
		Placement[][] placements = generateChunkPlacements(generator, x, y);
		// Create and return the chunk.
		return new Chunk(x, y, tiles, placements);
	}
	
	/**
	 * Create a chunk.
	 * This chunk already exist and is defined in the world save directory.
	 * The chunk we create will reflect this saved state state.
	 * @param chunkJSON The JSON object representing the chunk.
	 * @param generator The world generator.
	 * @return The created chunk.
	 */
	public static Chunk createChunk(JSONObject chunkJSON, WorldGenerator generator) {
		// Get the x/y chunk position.
		int x = chunkJSON.getInt("x");
		int y = chunkJSON.getInt("y");
		// Firstly, create the static world tiles for the chunk.
		TileType[][] tiles = createChunkTiles(generator, x, y);
		// We already have state for this chunk saved so read the existing placement state.
		Placement[][] placements = new Placement[Constants.WORLD_CHUNK_SIZE][Constants.WORLD_CHUNK_SIZE];
		// Get the placements array from our chunk JSON.
		JSONArray placementArray = chunkJSON.getJSONArray("placements");
		// Create an actual placement for each JSON array value.
		for (int placementIndex = 0; placementIndex < placementArray.length(); placementIndex++) {
			// Get the JSON for the current placement.
			JSONObject placementJSON = placementArray.getJSONObject(placementIndex);
			// Grab the placement position.
			int placementXPosition = placementJSON.getInt("x");
			int placementYPosition = placementJSON.getInt("y");
			// Create the actual placement.
			placements[placementXPosition][placementYPosition] = TileFactory.createPlacement(placementJSON);
		}
		// Create and return the chunk.
		return new Chunk(x, y, tiles, placements);
	}
	
	/**
	 * Create the multi-dimensional array that will hold the tile types for the chunk.
	 * @param worldGenerator The world generator.
	 * @param x The x position of the chunk.
	 * @param y The y position of the chunk.
	 * @return The multi-dimensional array holding the tile types for the chunk.
	 */
	private static TileType[][] createChunkTiles(WorldGenerator worldGenerator, int x, int y) {
		// Create the multi-dimensional array that will hold the tile types for the chunk.
		TileType[][] tiles = new TileType[Constants.WORLD_CHUNK_SIZE][Constants.WORLD_CHUNK_SIZE];
		int tileXOffset = x * Constants.WORLD_CHUNK_SIZE;
		int tileYOffset = y * Constants.WORLD_CHUNK_SIZE;
		for (int tileX = 0; tileX < Constants.WORLD_CHUNK_SIZE; tileX++) {
			for (int tileY = 0; tileY < Constants.WORLD_CHUNK_SIZE; tileY++) {
				tiles[tileX][tileY] = worldGenerator.getTileAt(tileX + tileXOffset, tileY + tileYOffset);
			}
		}
		// Return the static tile type array.
		return tiles;
	}
	
	/**
	 * Create the multi-dimensional array that will hold the placements for the chunk.
	 * These may be read from disk, or generated by the world generator.
	 * @param worldGenerator The world generator.
	 * @param x The x position of the chunk.
	 * @param y The y position of the chunk.
	 * @return The multi-dimensional array holding the placements for the chunk.
	 */
	private static Placement[][] generateChunkPlacements(WorldGenerator worldGenerator, int x, int y) {
		return new Placement[Constants.WORLD_CHUNK_SIZE][Constants.WORLD_CHUNK_SIZE];
	}
}