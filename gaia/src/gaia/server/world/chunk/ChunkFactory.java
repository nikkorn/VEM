package gaia.server.world.chunk;

import gaia.server.world.generation.PlacementFactories;
import gaia.server.world.generation.WorldGenerator;
import org.json.JSONArray;
import org.json.JSONObject;
import gaia.Constants;
import gaia.server.items.ItemType;
import gaia.server.world.container.Container;
import gaia.world.TileType;
import gaia.server.world.placements.Placement;
import gaia.server.world.placements.Placements;
import gaia.server.world.placements.Priority;
import gaia.server.world.placements.factories.IPlacementFactory;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementType;
import gaia.world.PlacementUnderlay;

/**
 * A factory for chunk entities.
 */
public class ChunkFactory {

	/**
	 * Create a new chunk in its default state.
	 * This chunk does not already exist (is not defined in the world save directory)
	 * so it is the job of the world generator to spawn initial placements.
	 * @param generator The world generator.
	 * @param x The x position of the chunk.
	 * @param y The y position of the chunk.
	 * @return The created chunk.
	 */
	public static Chunk createNewChunk(WorldGenerator generator, short x, short y) {
		// Firstly, create the static world tiles for the chunk.
		TileType[][] tiles = createChunkTiles(generator, x, y);
		// This is the first time we are creating this chunk (there is no 
		// chunk state saved to disk) so it is the responsibility of the world
		// generator to create some nice initial naturally-occurring placements.
		Placements placements = generator.getChunkPlacements(tiles, x, y);
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
			// Create the actual placements.
			placements.add(createPlacement(placementJSON, placementXPosition, placementYPosition));
		}
		// Create and return the chunk.
		return new Chunk(x, y, tiles, placements);
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
	
	/**
	 * Create a placements based on existing world state.
	 * @param placementJSON The JSON object representing an existing placements.
	 * @param x The placements x position within its parent chunk.
	 * @param y The placements y position within its parent chunk.
	 * @return The placements.
	 */
	private static Placement createPlacement(JSONObject placementJSON, short x, short y) {
		// Get the placements type.
		PlacementType placementType = PlacementType.values()[placementJSON.getInt("type")];
		// Create the placements.
		Placement placement = new Placement(placementType, x, y);
		// Get the relevant placements factory.
		IPlacementFactory placementFactory = PlacementFactories.getForType(placementType);
		// Create the action for this placements.
		placement.setAction(placementFactory.getAction());
		// Create the placements state if there is any.
		if (placementJSON.has("state")) {
			placement.setState(placementFactory.createState(placementJSON.getJSONObject("state")));
		}
		// Create the placements container if it has one.
		if (placementJSON.has("container")) {
			placement.setContainer(createContainer(placementJSON.getJSONArray("container")));
		}
		// Set the placements priority.
		placement.setPriority(Priority.values()[placementJSON.getInt("priority")]);
		// Set the placements underlay.
		placement.setUnderlay(PlacementUnderlay.values()[placementJSON.getInt("underlay")]);
		// Set the placements overlay.
		placement.setOverlay(PlacementOverlay.values()[placementJSON.getInt("overlay")]);
		// Return the new placements.
		return placement;
	}
	
	/**
	 * Create an empty container with the specified number of slots.
	 * @param numberOfSlots The number of slots.
	 * @return The container.
	 */
	public static Container createContainer(int numberOfSlots) {
		return new Container(numberOfSlots);
	}
	
	/**
	 * Create a container based on existing world state.
	 * @param containerJSON The JSON array representing the container slots.
	 * @return The container.
	 */
	public static Container createContainer(JSONArray containerJSON) {
		// Get the number of slots that this container has.
		int numberOfSlots = containerJSON.length();
		// Create the container with the correct number of slots.
		Container container = new Container(numberOfSlots);
		// Populate the container slot for each entry in our JSON array.
		for (int slotIndex = 0; slotIndex < numberOfSlots; slotIndex++) {
			// Grab the item type that the slot holds at the current index.
			ItemType slotItemType = ItemType.values()[containerJSON.getInt(slotIndex)];
			// Set the item in the appropriate slot.
			container.set(slotItemType, slotIndex);
		}
		// Return the container.
		return container;
	}
}