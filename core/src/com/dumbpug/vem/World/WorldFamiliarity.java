package com.dumbpug.vem.World;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import com.dumbpug.vem.C;
import com.dumbpug.vem.Persistance.Saveable;

/**
 * 
 * @author Nikolas Howard
 *
 */
public class WorldFamiliarity implements Saveable {
	// The worldFamiliarity array represents the players familiarity with the map. This must be atomic as 
	// entity script threads can get/set this as well as the GamePanel class (when drawing map)
	private AtomicBoolean[][] worldFamiliarity = null;
	
	public WorldFamiliarity() {
		worldFamiliarity = new AtomicBoolean[C.CONSOLE_MAP_FAMILIARITY_GRID_SIZE][C.CONSOLE_MAP_FAMILIARITY_GRID_SIZE];
		// Initialise all elements in the worldFamiliarity array
		for(int y = 0; y < C.CONSOLE_MAP_FAMILIARITY_GRID_SIZE; y++){
			for(int x = 0; x < C.CONSOLE_MAP_FAMILIARITY_GRID_SIZE; x++){
				worldFamiliarity[y][x] = new AtomicBoolean(false);
			}
		}
		// The first step is to populate the worldFamiliarity array
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(C.WORLD_FAMILIARITY_SAVE_FILE));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
        String rawMarkerInfo = "";
        while(scanner.hasNextLine()) {
        	rawMarkerInfo = scanner.nextLine().trim();
        	String[] valueArray = rawMarkerInfo.split("_");
        	worldFamiliarity[Integer.parseInt(valueArray[0])][Integer.parseInt(valueArray[1])].set(true);;
        }
	}
	
	public boolean isCellFamiliar(int positionY, int positionX) {
		return worldFamiliarity[positionY][positionX].get();	
	}
	
	public void markCellAsFamiliar(int positionY, int positionX) {
		worldFamiliarity[positionY][positionX].set(true);	
	}
	
	@Override
	public boolean save() {
		File targetFile = new File(C.WORLD_FAMILIARITY_SAVE_FILE);
		PrintWriter pr = null;
		try {
			pr = new PrintWriter(targetFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			for (int y = 0; y < worldFamiliarity[0].length; y++) {
				for (int x = 0; x < worldFamiliarity[0].length; x++) {
					if(worldFamiliarity[y][x].get()) {
						pr.println(y + "_" + x);
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		if(pr != null) {
			pr.close();
		}
		return true;
	}
}