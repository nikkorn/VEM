package com.dumbpug.vem.MapGeneration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

import com.dumbpug.vem.World.WorldObjectType;

/**
 * This class is responsible for picking winning world objects when randomly generating terrain.
 * @author Nikolas Howard
 *
 */
public class WorldObjectLottery {
	// These maps will hold the potential winners for each level, as well as the number of tickets they hold.
	LinkedHashMap<WorldObjectType, Integer> potentialWinners_level0 = new LinkedHashMap<WorldObjectType, Integer>();
	LinkedHashMap<WorldObjectType, Integer> potentialWinners_level1 = new LinkedHashMap<WorldObjectType, Integer>();
	LinkedHashMap<WorldObjectType, Integer> potentialWinners_level2 = new LinkedHashMap<WorldObjectType, Integer>();
	LinkedHashMap<WorldObjectType, Integer> potentialWinners_level3 = new LinkedHashMap<WorldObjectType, Integer>();
	// These numbers represent the chance (as a percentage) the we will even spawn a world object at this position
	int spawnChance_level0 = 1;
	int spawnChance_level1 = 8;
	int spawnChance_level2 = 5;
	int spawnChance_level3 = 3;
	
	// The higher this number, the less overall spawns
	int spawnPickLimit = 350;
	
	public WorldObjectLottery() {
		// Potential winners for level l (Sea)
		potentialWinners_level0.put(WorldObjectType.ROCK_5, 1);
		
		// Potential winners for level 2 (Dark Dangerous sand)
		potentialWinners_level1.put(WorldObjectType.ROCK_1, 20);
		potentialWinners_level1.put(WorldObjectType.ROCK_2, 20);
		potentialWinners_level1.put(WorldObjectType.ROCK_3, 20);
		potentialWinners_level1.put(WorldObjectType.ROCK_4, 20);
		potentialWinners_level1.put(WorldObjectType.BUSH_1, 15);
		potentialWinners_level1.put(WorldObjectType.GEM_STAGE_2, 3);
		potentialWinners_level1.put(WorldObjectType.GEM_STAGE_3, 1);
		potentialWinners_level1.put(WorldObjectType.SCRAP_STAGE_2, 2);
		potentialWinners_level1.put(WorldObjectType.SCRAP_STAGE_3, 1);
		
		// Potential winners for level l (Safer Sand)
		potentialWinners_level2.put(WorldObjectType.ROCK_1, 20);
		potentialWinners_level2.put(WorldObjectType.ROCK_2, 20);
		potentialWinners_level2.put(WorldObjectType.ROCK_3, 20);
		potentialWinners_level2.put(WorldObjectType.ROCK_4, 20);
		potentialWinners_level1.put(WorldObjectType.GEM_STAGE_1, 2);
		potentialWinners_level1.put(WorldObjectType.SCRAP_STAGE_1, 1);
		
		// Potential winners for level l (Safe Starting Sand)
		potentialWinners_level3.put(WorldObjectType.ROCK_1, 1);
		potentialWinners_level3.put(WorldObjectType.ROCK_2, 1);
		potentialWinners_level3.put(WorldObjectType.ROCK_3, 1);
		potentialWinners_level3.put(WorldObjectType.ROCK_4, 1);
	}
	
	public WorldObjectType pick(WorldLevel level, Random ran) {
		//Let us calculate whether we even want to spawn a world object.
		int val = ran.nextInt(spawnPickLimit);
		WorldObjectType returnType = null;
		
		switch(level) {
		case LEVEL_0:
			if(val <= spawnChance_level0) {
				returnType = doLottery(potentialWinners_level0, ran);
			}
			break;
		case LEVEL_1:
			if(val <= spawnChance_level1) {
				returnType = doLottery(potentialWinners_level1, ran);
			}
			break;
		case LEVEL_2:
			if(val <= spawnChance_level2) {
				returnType = doLottery(potentialWinners_level2, ran);
			}
			break;
		case LEVEL_3:
			if(val <= spawnChance_level3) {
				returnType = doLottery(potentialWinners_level3, ran);
			}
			break;
		}
		return returnType;
	}
	
	private WorldObjectType doLottery(LinkedHashMap<WorldObjectType,Integer> players, Random ran) {
		WorldObjectType returnType = null;
		ArrayList<WorldObjectType> ticketList = new ArrayList<WorldObjectType>();
		// If there are no players then just return null
		if(players.isEmpty()) {
			return null;
		}
		// Get the number of tickets
		for (WorldObjectType key : players.keySet()) {
			for(int i = 0; i < players.get(key).intValue(); i++) {
				ticketList.add(key);
			}
		}
		// Now for the draw
		returnType = ticketList.get(ran.nextInt(ticketList.size()));
		return returnType;
	}
	
	public enum WorldLevel {
		LEVEL_0,
		LEVEL_1,
		LEVEL_2,
		LEVEL_3,
	}
}
