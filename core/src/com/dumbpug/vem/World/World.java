package com.dumbpug.vem.World;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONObject;
import com.dumbpug.vem.C;
import com.dumbpug.vem.Entities.Drone;
import com.dumbpug.vem.Entities.Vem;
import com.dumbpug.vem.Entities.Direction;;

/**
 * 
 * @author Nikolas Howard
 *
 */
public class World {
	private Vem vem;
	private ArrayList<Drone> drones;
	
	private WorldCell[][] worldCells = new WorldCell[C.WORLD_CELL_SIZE][C.WORLD_CELL_SIZE];
	
	private WorldFamiliarity worldFamiliarity = null;
	
	public World() {
		drones = new ArrayList<Drone>();
	}
	
	/**
	 * Read all entity info from disk (VEM, Drone)
	 */
	public void populateSpecialWorldObjectsFromDisk() {
		// -------------------------------- Get Drones from Disk -----------------------------
		// If we don't have a drone save directory then just move on.
		File droneSaveDir = new File(C.ENTITY_DRONE_SAVE_DIRECTORY);
		if(droneSaveDir.exists()) {
			for(File droneSaveFile: droneSaveDir.listFiles()) {
				Scanner scanner = null;
				try {
					scanner = new Scanner(droneSaveFile);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				// Reconstruct our drone JSON object.
				JSONObject drObject = new JSONObject(scanner.nextLine());
				Drone newDrone = new Drone(drObject.getInt("posX"), drObject.getInt("posY"), drObject.getString("id"), Direction.valueOf(drObject.getString("direction"))
						, drObject.getString("script_1") , drObject.getString("script_2") , drObject.getString("script_3") , drObject.getString("script_4") , drObject.getString("script_5"));
				drones.add(newDrone);
			}
		} 
		// -----------------------------------------------------------------------------------
		
		// --------------------------------Get VEM from Disk ---------------------------------
		// Here we are initalising a new 'vem'. what we will have to do is create our vem
		// by reading his saved properties from disk (such as position)
		Scanner scanner = null;
		// Read for VEM entity save file to get info and set up this session
		try {
			scanner = new Scanner(new File(C.ENTITY_VEM_SAVE_FILE));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		JSONObject charObject = new JSONObject(scanner.nextLine());
		// Create a new instance of VEM
		vem = new Vem(charObject.getInt("posY"), charObject.getInt("posX"), Direction.valueOf(charObject.getString("Direction")), charObject.getString("MemBank"));
		// -----------------------------------------------------------------------------------
	}
	
	/**
	 * Reads the map from disk and writes into the worldCells array.
	 */
	public void populateWorldArrayFromDisk() {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("bin/map/vem.map"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
        
		for(int y = 0; y < C.WORLD_CELL_SIZE; y++) {
			for(int x = 0; x < C.WORLD_CELL_SIZE; x++) {
				int positionByte = scanner.nextInt();
				worldCells[y][x] = new WorldCell(positionByte >> 6, null);
				// Knock of the two bits representing the level.
				positionByte = positionByte & ~(1 << 6);
				positionByte = positionByte & ~(1 << 7);
				// The positionByte should now just be the ordinal for a WorldObjectType.
				// This could potentially by the NULL WorldObjectType if the MapGenerator could not get a winner.
				if(positionByte != 0) {
					worldCells[y][x].setWorldObject(new WorldObject(WorldObjectType.values()[positionByte], null));
				}
			}
		}
	}
	
	/**
	 * Read the map familiarity from disk
	 */
	public void initialiseWorldFamiliarity() {
		worldFamiliarity = new WorldFamiliarity();
	}

	/**
	 * Return true/false depending on whether the target cell is clear
	 * @param posX
	 * @param posY
	 * @param canTravelOnWater
	 * @return
	 */
	public boolean positionIsClear(int posX, int posY, boolean canTravelOnWater) {
		// If this point is outside the map then it is super NOT clear
		if(posX <= 10 ||
				posX >= C.WORLD_CELL_SIZE - 10 ||
				posY <= 10 ||
				posY >= C.WORLD_CELL_SIZE - 10) {
			return false;
		}
		// If this cell is water (level 0) then if the caller can travel on water then we don't regard water as obstructive
		if(worldCells[posY][posX].getGroundLevel() == 0) {
			if(canTravelOnWater) {
				return true;
			} else {
				return false;
			}
		}
		// Get the world object at this position (may be null)
		WorldObject wObject = worldCells[posY][posX].getWorldObject();
		//If there is no world object here then it is free
		return wObject == null;
	}
	
	/**
	 * Get VEM
	 * @return
	 */
	public Vem getVem() {
		return vem;
	}
	
	/**
	 * Get all drones
	 * @return
	 */
	public ArrayList<Drone> getDrones() {
		return drones;
	}
	
	/**
	 * Get drone with matching id
	 * @return
	 */
	public Drone getDrone(String id) {
		Drone returnDrone = null;
		for(Drone drone : drones) {
			if(drone.getId().toLowerCase().equals(id.toLowerCase())) {
				returnDrone = drone;
				break;
			}
		}
		return returnDrone;
	}
	
	/**
	 * Gets the world familiarity object
	 * @return
	 */
	public WorldFamiliarity getWorldFamiliarity() {
		return worldFamiliarity;
	}
	
	/**
	 * Remove the object at the position form the world array
	 * @param cell_x
	 * @param cell_y
	 */
	public void removeWorldObject(int cell_x, int cell_y) {
		synchronized(worldCells) {
			worldCells[cell_y][cell_x].setWorldObject(null);;
		}
	}

	/**
	 * Set the world position in the world array
	 * @param wObject
	 * @param posX
	 * @param posY
	 */
	public void setWorldObject(WorldObject wObject, int posX, int posY) {
		synchronized(worldCells) {
			worldCells[posY][posX].setWorldObject(wObject);
		}
	}
	
	/**
	 * Get world cells array
	 * @return
	 */
	public WorldCell[][] getWorldCells() {
		return worldCells;
	}
}
