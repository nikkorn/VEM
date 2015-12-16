package com.dumbpug.vem.Persistance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;

import com.dumbpug.vem.C;
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
		// TODO Auto-generated method stub
		
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
