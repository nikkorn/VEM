package gaia.server.world.chunk;

import gaia.server.world.generation.WorldGenerator;
import gaia.server.world.messaging.WorldMessageQueue;
import org.json.JSONArray;
import org.json.JSONObject;
import gaia.Constants;
import gaia.world.TileType;
import gaia.server.world.placements.PlacementFactory;
import gaia.server.world.placements.Placements;

/**
 * Factory for creating Chunk instances.
 */
public class ChunkFactory {

	/**
	 * Create a new chunk in its default state.
	 * This chunk does not already exist (is not defined in the world save directory)
	 * so it is the job of the world generator to spawn initial placements.
	 * @param generator The world generator.
	 * @param x The x position of the chunk.
	 * @param y The y position of the chunk.
	 * @param worldMessageQueue The world message queue.
	 * @return The created chunk.
	 */
	public static Chunk create(WorldGenerator generator, short x, short y, WorldMessageQueue worldMessageQueue) {
		// Firstly, create the static world tiles for the chunk.
		TileType[][] tiles = createChunkTiles(generator, x, y);
		// This is the first time we are creating this chunk (there is no 
		// chunk state saved to disk) so it is the responsibility of the world
		// generator to create some nice initial naturally-occurring placements.
		Placements placements = generator.getChunkPlacements(tiles, x, y);
		// Create and return the chunk.
		return new Chunk(x, y, tiles, placements, worldMessageQueue);
	}
	
	/**
	 * Restore an existing chunk.
	 * This chunk already exist and is defined in the world save directory.
	 * The chunk we create will reflect this saved state state.
	 * @param chunkJSON The JSON object representing the chunk.
	 * @param generator The world generator.
	 * @param worldMessageQueue The world message queue.
	 * @return The created chunk.
	 */
	public static Chunk restore(JSONObject chunkJSON, WorldGenerator generator, WorldMessageQueue worldMessageQueue) {
		// Get the x/y chunk position.
		short x = (short)chunkJSON.getInt("x");
		short y = (short)chunkJSON.getInt("y");
		// Firstly, create the static world tiles for the chunk.
		TileType[][] tiles = createChunkTiles(generator, x, y);
		// We already have state for this chunk saved so read the existing placements state.
		Placements placements = new Placements();
		// Get the placements array from our chunk JSON.
		JSONArray placementArray = chunkJSON.getJSONArray("placements");
		// Create an actual placements for each JSON array value.
		for (int placementIndex = 0; placementIndex < placementArray.length(); placementIndex++) {
			// Get the JSON for the current placements.
			JSONObject placementJSON = placementArray.getJSONObject(placementIndex);
			// Grab the placements position.
			short placementXPosition = (short)placementJSON.getInt("x");
			short placementYPosition = (short)placementJSON.getInt("y");
			// Create the actual placement.
			placements.add(PlacementFactory.create(placementJSON, placementXPosition, placementYPosition));
		}
		// Create and return the chunk.
		return new Chunk(x, y, tiles, placements, worldMessageQueue);
	}
	
	/**
	 * Create the multi-dimensional array that will hold the tile types for the chunk.
	 * @param tileGenerator The world generator.
	 * @param x The x position of the chunk.
	 * @param y The y position of the chunk.
	 * @return The multi-dimensional array holding the tile types for the chunk.
	 */
	private static TileType[][] createChunkTiles(WorldGenerator tileGenerator, int x, int y) {
		// Create the multi-dimensional array that will hold the tile types for the chunk.
		TileType[][] tiles = new TileType[Constants.WORLD_CHUNK_SIZE][Constants.WORLD_CHUNK_SIZE];
		int tileXOffset = x * Constants.WORLD_CHUNK_SIZE;
		int tileYOffset = y * Constants.WORLD_CHUNK_SIZE;
		for (int tileX = 0; tileX < Constants.WORLD_CHUNK_SIZE; tileX++) {
			for (int tileY = 0; tileY < Constants.WORLD_CHUNK_SIZE; tileY++) {
				tiles[tileX][tileY] = tileGenerator.getTileAt(tileX + tileXOffset, tileY + tileYOffset);
			}
		}
		// Return the static tile type array.
		return tiles;
	}
}