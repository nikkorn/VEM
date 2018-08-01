package gaia.server.world;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import org.json.JSONObject;
import gaia.Position;
import gaia.server.Constants;
import gaia.server.Helpers;
import gaia.server.world.chunk.Chunk;
import gaia.server.world.chunk.ChunkFactory;
import gaia.server.world.chunk.Chunks;
import gaia.server.world.generation.WorldGenerator;
import gaia.server.world.generation.WorldMapImageCreator;
import gaia.time.Season;
import gaia.time.Time;

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
			// Write something sensible to the console.
			System.out.println("##########################################################");
			System.out.print("loading existing world '" + name + "' ...");
			// A save already exists for this world! Create a world based on the saved state.
			World world = createExistingWorld(name);
			// Write something sensible to the console.
			System.out.println(" done!");
			System.out.println("##########################################################");
			// Return the world.
			return world;
		} else {
			// Write something sensible to the console.
			System.out.println("##########################################################");
			System.out.print("creating new world '" + name + "' ...");
			// We are creating a new world save!
			// Create a brand new world seed.
			long worldSeed = new Random().nextLong();
			// Create the initial world time.
			Time initialWorldTime = new Time(Season.SPRING, 1, 9, 0);
			// Create the world generator.
			WorldGenerator worldGenerator = new WorldGenerator(worldSeed);
			// Find a world position for players to spawn at.
			Position spawn = findWorldSpawn();
			// Load the chunks that are in the vicinity of the spawn.
			ArrayList<Chunk> spawnChunks = generateSpawnChunks(spawn, worldGenerator);
			// Create a new world!
			World world = new World(new Chunks(worldGenerator, spawnChunks), spawn, initialWorldTime, worldGenerator);
			// Create the world save directory for this new world.
			createWorldSaveDirectory(worldSaveDir, name, world);
			// Create the map overview image file.
			WorldMapImageCreator.create(worldGenerator, "map", "worlds/" + name + "/");
			// Write something sensible to the console.
			System.out.println(" done!");
			System.out.println("##########################################################");
			// Return the new world.
			return world;
		}
	}
	
	/**
	 * Find a valid world spawn position for players.
	 * @return A valid world spawn position for players.
	 */
	private static Position findWorldSpawn() {
		// TODO Check world tiles for valid position.
		return new Position(123, 234);
	}
	
	/**
	 * Generate the chunks that are in the vicinity of the spawn.
	 * @param spawn The spawn position.
	 * @param worldGenerator The world generator.
	 * @return The chunks that are in the vicinity of the spawn.
	 */
	private static ArrayList<Chunk> generateSpawnChunks(Position spawn, WorldGenerator worldGenerator) {
		// Create a list to hold the loaded spawn chunks.
		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
		// Get the chunk range that we regard as being the 'vicinity' of another chunk.
		int range = Constants.WORLD_CHUNK_VICINITY_RANGE;
		// Load every chunk position in the vicinity of the spawn.
		for (int chunkX = (spawn.getChunkX() - range); chunkX <= (spawn.getChunkX() + range); chunkX++) {
			for (int chunkY = (spawn.getChunkY() - range); chunkY <= (spawn.getChunkY() + range); chunkY++) {
				// Do not attempt to load a chunk at an invalid chunk position.
				if (!Chunks.isValidChunkPosition(chunkX, chunkY)) {
					continue;
				}
				// Generate the chunk and add it to our chunk list.
				chunks.add(ChunkFactory.createNewChunk(worldGenerator, chunkX, chunkY));
			}
		}
		
		// TODO Write the chunks to disk now.
		
		// Return the list of loaded spawn chunks.
		return chunks;
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
		// Create the world generator.
		WorldGenerator worldGenerator = new WorldGenerator(worldSeed);
		// Create the world chunks collection, containing any existing chunks read from disk.
		Chunks existingChunks = createExistingChunks(worldName, worldGenerator);
		// Get the player spawn.
		Position spawn = new Position(worldState.getJSONObject("spawn").getInt("x"), worldState.getJSONObject("spawn").getInt("y"));
		// Create the world instance.
		World world = new World(existingChunks, spawn, worldTime, worldGenerator);
		// Return the world instance.
		return world;
	}

	/**
	 * Create the world chunks from saved state.
	 * @param worldName The world name.
	 * @param worldGenerator The world generator.
	 * @return The existing chunks.
     */
	private static Chunks createExistingChunks(String worldName, WorldGenerator worldGenerator) {
		// Grab a handle to the chunks directory.
		File chunksDirectory = new File("worlds/" + worldName + "/chunks");
		// The list to hold the existing chunks.
		ArrayList<Chunk> existingChunks = new ArrayList<Chunk>();
		// Create a chunk for each chunk file in the directory.
		for (File chunkFile : chunksDirectory.listFiles()) {
			// Convert the chunk save file to JSON.
			JSONObject chunkState = Helpers.readJSONObjectFromFile(chunkFile);
			// Restore the chunk.
			Chunk chunk = ChunkFactory.restoreChunk(chunkState, worldGenerator);
			// Add the chunk to the list of existing chunks.
			existingChunks.add(chunk);
		}
		// Return the existing chunks.
		return new Chunks(worldGenerator, existingChunks);
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
		Helpers.writeStringToFile(worldSaveFile, world.serialise().toString());
	}
}
