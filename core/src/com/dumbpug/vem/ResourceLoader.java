package com.dumbpug.vem;

import com.dumbpug.vem.MapGeneration.MapGenerator;
import com.dumbpug.vem.World.World;

/**
 * 
 * @author Nikolas Howard
 *
 */
public class ResourceLoader {
	World worldToBeLoaded;
	TexturePack texturePackToBeLoaded;
	
	private boolean isLoaded = false;
	
	ResourceLoader(World world, TexturePack texturePack) {
		worldToBeLoaded = world;
		texturePackToBeLoaded = texturePack;
	}
	
	public void loadResources() {
		// Check if we already have a map, if not then make one
		MapGenerator mapGenerator = new MapGenerator();
		// If a map didn't exist then this is a new game.
		if(!mapGenerator.doesMapExist()) {
			mapGenerator.generateMap();
		}
		// Read world familiarity from disk
		worldToBeLoaded.initialiseWorldFamiliarity();
		// This method will fill the worldcells array using map file
		worldToBeLoaded.populateWorldArrayFromDisk();
		// This method will fill lists with special entities such as 'drones' and entities such 
		// as 'vem' from disk (anything that has its own file in bin/map)
		worldToBeLoaded.populateSpecialWorldObjectsFromDisk();
		// Load our textures/texture region arrays
		texturePackToBeLoaded.loadResources();
		// Set flag to show that we have loaded all resources
		isLoaded = true;
	}

	public boolean isLoaded() {
		return isLoaded;
	}
}