package com.dumbpug.vem.Persistance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import org.json.JSONObject;

import com.dumbpug.vem.C;
import com.dumbpug.vem.Entities.Drone;
import com.dumbpug.vem.World.World;

/**
 * The sole purpose of this class is to save the game state to disk
 * @author Nikolas Howard
 *
 */
public class ResourceSaver {
	public static void save(World world) {
		// Save the state of scriptable entities (drones)
		saveScriptablesState(world);
		// Save the state of the main character
		saveVemState(world);
		// Save the world familiarity to disk
		saveWorldFamiliarity(world);
		
		// Sleep just for a little, so the 'Saving' screen doesnt just flash
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save the state of scriptable entities (drones)
	 * @param world
	 */
	private static void saveScriptablesState(World world) {
		File droneSaveDir = new File(C.ENTITY_DRONE_SAVE_DIRECTORY);
		// First of all, let us delete our old drone save files. If we have never saved before we 
		// will have to create this directory first.
		if(!droneSaveDir.exists()) {
			droneSaveDir.mkdir();
		} 
		// Delete old save files
		for(File file: droneSaveDir.listFiles()) {
			if (file.exists()) {
				// For some crazy reason delete only works when we call the garbage collector first. Must be a bug as nothing is open.
				System.gc();
				file.delete();
			} 
		}
		// Save each drone to disk.
		for(Drone drone : world.getDrones()) {
			// Generate a file with a random unique name.
			File droneSaveFile = new File(C.ENTITY_DRONE_SAVE_DIRECTORY + UUID.randomUUID().toString());
			// Physically create the new save file.
			try {
				droneSaveFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Create a JSON object using our drone info
			JSONObject droneObject = new JSONObject();
			droneObject.put("id", drone.getId());
			droneObject.put("posY", drone.getCellY());
			droneObject.put("posX", drone.getCellX());
			droneObject.put("id", drone.getId());
			droneObject.put("direction", drone.getFacingDirection().toString());
			droneObject.put("script_1", drone.getScriptCode(0));
			droneObject.put("script_2", drone.getScriptCode(1));
			droneObject.put("script_3", drone.getScriptCode(2));
			droneObject.put("script_4", drone.getScriptCode(3));
			droneObject.put("script_5", drone.getScriptCode(4));
			// Write the JSON to our file
			PrintWriter pr = null;
			try {
				pr = new PrintWriter(droneSaveFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			pr.println(droneObject); 
			pr.flush();
			if(pr != null) {
				pr.close();
			}
		}
	}
	
	/**
	 * Save the state of the main character
	 * @param world
	 */
	private static void saveVemState(World world) {
		File targetFile = new File(C.ENTITY_VEM_SAVE_FILE);
		JSONObject charObject = new JSONObject();
		charObject.put("posX", world.getVem().getCellX());
		charObject.put("posY", world.getVem().getCellY());
		charObject.put("Direction", world.getVem().getFacingDirection().toString());
		charObject.put("MemBank", world.getVem().getMemoryBankContents());
		try {
			targetFile.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PrintWriter pr = null;
		try {
			pr = new PrintWriter(targetFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		pr.println(charObject); 
		pr.flush();
		if(pr != null) {
			pr.close();
		}
	}

	/**
	 * Save the world familiarity to disk
	 * @param world
	 */
	private static void saveWorldFamiliarity(World world) {
		world.getWorldFamiliarity().save();
	}
}
