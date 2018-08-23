package gaia.server.world;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import gaia.server.ServerConsole;
import gaia.server.world.generation.WorldGenerator;
import gaia.server.world.messaging.WorldMessageQueue;
import gaia.server.world.players.PlayerFactory;
import gaia.server.world.players.Players;
import org.json.JSONObject;
import gaia.Constants;
import gaia.server.Helpers;
import gaia.server.world.chunk.Chunk;
import gaia.server.world.chunk.ChunkFactory;
import gaia.server.world.chunk.Chunks;
import gaia.world.generation.WorldMapImageCreator;
import gaia.time.Season;
import gaia.time.Time;
import gaia.world.Position;

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
		// Create the world message queue that will be shared throughout the application.
		WorldMessageQueue worldMessageQueue = new WorldMessageQueue();
		// Does a world save already exist?
		if (worldSaveDir.exists() && worldSaveDir.isDirectory()) {
			// Write something sensible to the console.
			System.out.println("##########################################################");
			System.out.print("loading existing world '" + name + "' ... ");
			// A save already exists for this world! Create a world based on the saved state.
			World world = createExistingWorld(name, worldMessageQueue);
			// Write something sensible to the console.
			System.out.println("done!");
			System.out.println("##########################################################");
			// Return the world.
			return world;
		} else {
			// Write something sensible to the console.
			System.out.println("##########################################################");
			System.out.print("creating new world '" + name + "' ... ");
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
			ArrayList<Chunk> spawnChunks = generateSpawnChunks(spawn, worldGenerator, worldMessageQueue);
			// Create a new world!
			World world = new World(new Players(new PlayerFactory()), new Chunks(worldGenerator, worldMessageQueue, spawnChunks), spawn, initialWorldTime, worldGenerator, worldMessageQueue);
			// Create the world save directory for this new world.
			createWorldSaveDirectory(worldSaveDir, name, world);
			// Create the map overview image file.
			WorldMapImageCreator.create(worldGenerator, "map", "worlds/" + name + "/");
			// Write something sensible to the console.
			System.out.println("done!");
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
		return new Position((short)100, (short)100);
	}
	
	/**
	 * Generate the chunks that are in the vicinity of the spawn.
	 * @param spawn The spawn position.
	 * @param worldGenerator The world generator.
	 * @param worldMessageQueue The world message queue.
	 * @return The chunks that are in the vicinity of the spawn.
	 */
	private static ArrayList<Chunk> generateSpawnChunks(Position spawn, WorldGenerator worldGenerator, WorldMessageQueue worldMessageQueue) {
		// Create a list to hold the loaded spawn chunks.
		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
		// Get the chunk range that we regard as being the 'vicinity' of another chunk.
		int range = Constants.WORLD_CHUNK_VICINITY_RANGE;
		// Load every chunk position in the vicinity of the spawn.
		for (short chunkX = (short)(spawn.getChunkX() - range); chunkX <= (spawn.getChunkX() + range); chunkX++) {
			for (short chunkY = (short)(spawn.getChunkY() - range); chunkY <= (spawn.getChunkY() + range); chunkY++) {
				// Do not attempt to load a chunk at an invalid chunk position.
				if (!Chunks.isValidChunkPosition(chunkX, chunkY)) {
					continue;
				}
				// Generate the chunk and add it to our chunk list.
				chunks.add(ChunkFactory.createNewChunk(worldGenerator, chunkX, chunkY, worldMessageQueue));
			}
		}
		
		// TODO Write the chunks to disk now.
		
		// Return the list of loaded spawn chunks.
		return chunks;
	}

	/**
	 * Create a world instance based on an existing world save.
	 * @param worldName The name of the existing world.
	 * @param worldMessageQueue The world message queue.
	 * @return The created world.
	 */
	private static World createExistingWorld(String worldName, WorldMessageQueue worldMessageQueue) {
		// Grab a handle to the world save JSON file.
		File worldSaveFile = new File("worlds/" + worldName + "/world.json");
		// Convert the world save file to JSON.
		JSONObject worldState = Helpers.readJSONObjectFromFile(worldSaveFile);
		// Verify that the server version used to create the existing world state is compatible with the current version.
		verifyExistingWorldVersion(worldState.getString("version"), worldName);
		// Get the world seed.
		long worldSeed = worldState.getLong("seed");
		// Get the world time.
		Time worldTime = Time.fromState(worldState.getJSONObject("time"));
		// Create the world generator.
		WorldGenerator worldGenerator = new WorldGenerator(worldSeed);
		// Create the world chunks collection, containing any existing chunks read from disk.
		Chunks existingChunks = createExistingChunks(worldName, worldGenerator, worldMessageQueue);
		// Get the player spawn.
		Position spawn = new Position((short)worldState.getJSONObject("spawn").getInt("x"), (short)worldState.getJSONObject("spawn").getInt("y"));
		// Create the player factory.
		PlayerFactory playerFactory = new PlayerFactory("worlds/" + worldName + "/players");
		// Create the world instance.
		World world = new World(new Players(playerFactory), existingChunks, spawn, worldTime, worldGenerator, worldMessageQueue);
		// Return the world instance.
		return world;
	}

	/**
	 * Create the world chunks from saved state.
	 * @param worldName The world name.
	 * @param worldGenerator The world generator.
	 * @param worldMessageQueue The world message queue.
	 * @return The existing chunks.
     */
	private static Chunks createExistingChunks(String worldName, WorldGenerator worldGenerator, WorldMessageQueue worldMessageQueue) {
		// Grab a handle to the chunks directory.
		File chunksDirectory = new File("worlds/" + worldName + "/chunks");
		// The list to hold the existing chunks.
		ArrayList<Chunk> existingChunks = new ArrayList<Chunk>();
		// Create a chunk for each chunk file in the directory.
		for (File chunkFile : chunksDirectory.listFiles()) {
			// Convert the chunk save file to JSON.
			JSONObject chunkState = Helpers.readJSONObjectFromFile(chunkFile);
			// Restore the chunk.
			Chunk chunk = ChunkFactory.restoreChunk(chunkState, worldGenerator, worldMessageQueue);
			// Add the chunk to the list of existing chunks.
			existingChunks.add(chunk);
		}
		// Return the existing chunks.
		return new Chunks(worldGenerator, worldMessageQueue, existingChunks);
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

	/**
	 * Verify that the server version used to create the existing world state is compatible with the current version.
	 * @param worldVersion The server version used to create the existing world state.
	 * @param worldName The world name.
     */
	private static void verifyExistingWorldVersion(String worldVersion, String worldName) {
		// Split the version into major/minor/patch semantic versions.
		String[] versions = worldVersion.split("\\.");
		// Check whether the major version number differs. If so then there have
		// been breaking changes and we cannot load the existing world save state.
		if (!versions[0].equals(Constants.VERSION.split("\\.")[0])) {
			System.out.println();
			ServerConsole.writeError("The world '" + worldName + "' cannot be loaded as it uses outdated world save format '" + worldVersion + "'");
			System.exit(0);
		}
		// Check whether the minor version number differs. If so then the save is still compatible (for now).
		if (!versions[1].equals(Constants.VERSION.split("\\.")[1])) {
			System.out.println();
			ServerConsole.writeWarning("The world '" + worldName + "' save format has a minor version mismatch '" + worldVersion + "'");
		}
	}
}
