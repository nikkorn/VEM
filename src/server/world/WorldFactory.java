package server.world;

import java.io.File;
import java.util.Random;
import org.json.JSONObject;
import server.world.generation.WorldGenerator;
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
			// A save already exists for this world!
			return null;
		} else {
			// We are creating a new world save.
			// Create a brand new world seed.
			long worldSeed = new Random().nextLong();
			// Create the initial world time.
			Time initalWorldTime = new Time(Season.SPRING, 1, 9, 0);
			// Create a new world!
			World world = new World(initalWorldTime, new WorldGenerator(worldSeed));
			// Create the world save directory for this new world.
			createWorldSaveDirectory(worldSaveDir, name, world);
			// Return the new world.
			return world;
		}
	}
	
	/**
	 * Create a world save directory.
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
		File worldSaveFile = new File("worlds/" + worldName + "/" + worldName + ".json");
		// Get the world state as JSON (excluding chunk information) and write it to the world save file.
		JSONObject worldState = world.getState();
		// ....
	}
}
