package server.world.chunk;

import java.util.HashMap;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;
import server.Constants;
import server.world.generation.WorldGenerator;
import server.world.tile.TileType;
import server.world.tile.placement.Container;
import server.world.tile.placement.Placement;
import server.world.tile.placement.PlacementType;
import server.world.tile.placement.Priority;
import server.world.tile.placement.factories.IPlacementFactory;
import server.world.tile.placement.factories.TilledEarthFactory;
import server.world.tile.placement.factories.TreeFactory;

/**
 * A factory for chunk entities.
 */
public class ChunkFactory {
	/**
	 * The map of placement factories, keyed on placement type.
	 */
	private static HashMap<PlacementType, IPlacementFactory> placementFactories = new HashMap<PlacementType, IPlacementFactory>()
	{
		private static final long serialVersionUID = 1L;
		{ 
			this.put(PlacementType.TILLED_EARTH, new TilledEarthFactory());
			this.put(PlacementType.TREE, new TreeFactory());
		}
	};
	
	/**
	 * Create a new chunk in its default state.
	 * This chunk does not already exist (is not defined in the world save directory)
	 * so it is the job of the world generator to spawn initial placements.
	 * @param generator The world generator.
	 * @param x The x position of the chunk.
	 * @param y The y position of the chunk.
	 * @return The created chunk.
	 */
	public static Chunk createNewChunk(WorldGenerator generator, int x, int y) {
		// Firstly, create the static world tiles for the chunk.
		TileType[][] tiles = createChunkTiles(generator, x, y);
		// This is the first time we are creating this chunk (there is no 
		// chunk state saved to disk) so it is the responsibility of the world
		// generator to create some nice initial naturally-occurring placements.
		Placement[][] placements = generateChunkPlacements(generator, tiles, x, y);
		// Create and return the chunk.
		return new Chunk(x, y, tiles, placements);
	}
	
	/**
	 * Restore an existing chunk.
	 * This chunk already exist and is defined in the world save directory.
	 * The chunk we create will reflect this saved state state.
	 * @param chunkJSON The JSON object representing the chunk.
	 * @param generator The world generator.
	 * @return The created chunk.
	 */
	public static Chunk restoreChunk(JSONObject chunkJSON, WorldGenerator generator) {
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
			placements[placementXPosition][placementYPosition] = createPlacement(placementJSON);
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
	 * @param tiles The static world tiles for the chunk.
	 * @param x The x position of the chunk.
	 * @param y The y position of the chunk.
	 * @return The multi-dimensional array holding the placements for the chunk.
	 */
	private static Placement[][] generateChunkPlacements(WorldGenerator worldGenerator, TileType[][] tiles, int x, int y) {
		// Create the array which will hold any generated placements.
		Placement[][] placements = new Placement[Constants.WORLD_CHUNK_SIZE][Constants.WORLD_CHUNK_SIZE];
		// Create the rng to use in creating all the placements for this chunk.
		Random chunkRng = new Random(worldGenerator.getPositionSeed(x, y));
		// For each tile in this chunk we will try to create a placement, based on the tile type.
		for (int tileX = 0; tileX < Constants.WORLD_CHUNK_SIZE; tileX++) {
			for (int tileY = 0; tileY < Constants.WORLD_CHUNK_SIZE; tileY++) {
				// Try to generate a placement for this tile positon, this could be null if we didn't generate one.
				PlacementType placementType = worldGenerator.getPlacmementLottos().getPlacementForTile(tiles[tileX][tileY], chunkRng);
				// Create the placement if we generated one.
				if (placementType != PlacementType.NONE) {
					// Passing the RNG means that we can generate consistent placement state!
					placements[tileX][tileY] = createPlacement(placementType, chunkRng);
				}
			}
		}
		// Return the generated placements.
		return placements;
	}
	
	/**
	 * Create a placement of the specified type in its default state.
	 * @param type The placement type.
	 * @param chunkRng The rng to use in creating all the placements for a chunk.
	 * @return The placement.
	 */
	private static Placement createPlacement(PlacementType type, Random chunkRng) {
		// Create the new placement.
		Placement placement = new Placement(type);
		// Get the relevant placement factory.
		IPlacementFactory placementFactory = placementFactories.get(type);
		// Create the placement state.
		placement.setState(placementFactory.createState(chunkRng));
		// Create the action for this placement.
		placement.setAction(placementFactory.getAction());
		// Set the initial priority for this placement.
		placement.setPriority(placementFactory.getInitialPriority());
		// Set the initial container for this placement.
		placement.setContainer(placementFactory.getInitialContainer(chunkRng));
		// Return the new placement.
		return placement;
	}
	
	/**
	 * Create a placement based on existing world state.
	 * @param placementJSON The JSON object representing an existing placement.
	 * @return The placement.
	 */
	private static Placement createPlacement(JSONObject placementJSON) {
		// Get the placement type.
		PlacementType placementType = PlacementType.values()[placementJSON.getInt("type")];
		// Create the placement.
		Placement placement = new Placement(placementType);
		// Get the relevant placement factory.
		IPlacementFactory placementFactory = placementFactories.get(placementType);
		// Create the action for this placement.
		placement.setAction(placementFactory.getAction());
		// Create the placement state if there is any.
		if (placementJSON.has("state")) {
			placement.setState(placementFactory.createState(placementJSON.getJSONObject("state")));
		}
		// Create the placement container if it has one.
		if (placementJSON.has("container")) {
			placement.setContainer(createContainer(placementJSON.getJSONObject("container")));
		}
		// Set the placement priority.
		placement.setPriority(Priority.values()[placementJSON.getInt("priority")]);
		// Return the new placement.
		return placement;
	}
	
	/**
	 * Create an empty container with the specified number of slots.
	 * @param numberOfSlots The number of slots.
	 * @return The container.
	 */
	public static Container createContainer(int numberOfSlots) {
		return new Container();
	}
	
	/**
	 * Create a container based on existing world state.
	 * @param containerJSON The JSON object representing the container.
	 * @return The container.
	 */
	public static Container createContainer(JSONObject containerJSON) {
		return new Container();
	}
}