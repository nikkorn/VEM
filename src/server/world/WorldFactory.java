package server.world;

import java.io.File;
import java.util.Random;
import org.json.JSONObject;
import server.Helpers;
import server.world.chunk.Chunk;
import server.world.chunk.ChunkFactory;
import server.world.generation.WorldGenerator;
import server.world.generation.WorldMapImageCreator;
import server.world.time.Season;
import server.world.time.Time;

/**
 * A factory for world entities.
 */
public class WorldFactory {
	
	/**
	 * Create a world instance, potentially based on an existing world save file.
	 * @param name The name of the world.
	 * @return The world instance.
	 */
	public static World createWorld(String name) {
		// Check whether a world save directory of this name already exists.
		File worldSaveDir = new File("worlds/" + name);
		if (worldSaveDir.exists() && worldSaveDir.isDirectory()) {
			// A save already exists for this world! Create a world based on the saved state.
			World world = createExistingWorld(name);
			// Return the world.
			return world;
		} else {
			// We are creating a new world save!
			// Create a brand new world seed.
			long worldSeed = new Random().nextLong();
			// Create the initial world time.
			Time initialWorldTime = new Time(Season.SPRING, 1, 9, 0);
			// Create the world generator.
			WorldGenerator worldGenerator = new WorldGenerator(worldSeed);
			// Create a new world!
			World world = new World(initialWorldTime, worldGenerator);
			// Create the world save directory for this new world.
			createWorldSaveDirectory(worldSaveDir, name, world);
			// Create the map overview image file.
			WorldMapImageCreator.create(worldGenerator, "map", "worlds/" + name + "/");
			// Return the new world.
			return world;
		}
	}
	
	/**
	 * Create a world instance based on an existing world save.
	 * @param worldName The name of the existing world.
	 * @return The created world.
	 */
	private static World createExistingWorld(String worldName) {
		// Grab a handle to the world save JSON file.
		File worldSaveFile = new File("worlds/" + worldName + "/world.json");
		// Convert the world save file to JSON.
		JSONObject worldState = Helpers.readJSONObjectFromFile(worldSaveFile);
		// Get the world seed.
		long worldSeed = worldState.getLong("seed");
		// Get the world time.
		Time worldTime = Time.fromState(worldState.getJSONObject("time"));
		// Create the world instance.
		WorldGenerator worldGenerator = new WorldGenerator(worldSeed);
		World world                   = new World(worldTime, worldGenerator);
		// Create the world chunks.
		createExistingChunks(worldName, world, worldGenerator);
		// Return the world instance.
		return world;
	}

	/**
	 * Create the world chunks from saved state.
	 * @param worldName The world name.
	 * @param world The world.
	 * @param worldGenerator The world generator.
     */
	private static void createExistingChunks(String worldName, World world, WorldGenerator worldGenerator) {
		// Grab a handle to the chunks directory.
		File chunksDirectory = new File("worlds/" + worldName + "/chunks");
		// Create a chunk for each chunk file in the directory.
		for (File chunkFile : chunksDirectory.listFiles()) {
			// Convert the chunk save file to JSON.
			JSONObject chunkState = Helpers.readJSONObjectFromFile(chunkFile);
			// Restore the chunk.
			Chunk chunk = ChunkFactory.restoreChunk(chunkState, worldGenerator, world.getWorldMessageQueue());
			// Add the chunk to the world.
			world.addCachedChunk(chunk);
		}
	}

	/**
	 * Create a new world save directory.
	 * @param worldSaveDir The file instance pointing to the target world save directory.
	 * @param worldName The world name.
	 * @param world The newly created world.
	 */
	private static void createWorldSaveDirectory(File worldSaveDir, String worldName, World world) {
		// Firstly, create the world save directory.
		worldSaveDir.mkdir();
		// Create the world chunks directory.
		new File("worlds/" + worldName + "/chunks").mkdir();
		// Create the player state directory.
		new File("worlds/" + worldName + "/players").mkdir();
		// Create the world JSON file.
		File worldSaveFile = new File("worlds/" + worldName + "/world.json");
		// Get the world state as JSON (excluding chunk information) and write it to the world save file.
		Helpers.writeStringToFile(worldSaveFile, world.getState().toString());
	}
}
