package com.dumbpug.vem.MapGeneration;

import java.util.Random;

import com.dumbpug.vem.World.WorldObjectType;

/**
 * 
 * @author Nikolas Howard
 *
 */
public class WorldObjectClusterGenerator {
	// Types of object that can be clustered (have 3 stages)
	public enum ClusterableWorldObjectType {
		GEM,
		SCRAP
	}
	// The size of the temporary grid used to generate a cluster
	private int gridSize = 20;
	private int[][] world = new int[gridSize][gridSize];
	// Tweaking the below values will alter the spawn chances for each stage.
	private int spawnChances[][] = new int[][] {{15,5,0}, {30,25,5}, {40,30,10}};
	// The higher the spawnPercentage, the smaller the cluster
	private int spawnPercentage = 50;
	
	public void generateCluster(ClusterableWorldObjectType type, int[][] tileMap, int[][] populatedMap, int inY, int inX, int stage, Random ran) {
		// Refresh our temporary grid
		for(int y = 0; y < gridSize; y++) {
			for(int x = 0; x < gridSize; x++) {
				world[y][x] = -1;
			}
		}
		// Generate our cluster
		go(10, 10, stage, ran);
		applyClusterToWorld(type, tileMap, populatedMap, inY, inX);
	}

	private void go(int inY, int inX, int stage, Random ran) {
		world[inY][inX] = stage;
		for(int y = inY - 1; y <= (inY + 1); y++) {
			for(int x = inX - 1; x <= (inX + 1); x++) {
				if(y>=0 && y < world.length && x>=0 && x < world.length) {
					if(world[y][x] == -1){
						int cellPrize = 0;
						for(int stageChance = spawnChances[stage - 1].length - 1; stageChance >= 0; stageChance--) {
							if(ran.nextInt(spawnPercentage) <= spawnChances[stage - 1][stageChance]) {
								cellPrize = stageChance + 1;
							}
						}
						world[y][x] = cellPrize;
						if(cellPrize != 0) {
							go(y, x, cellPrize, ran);
						}
					}
				}
			}
		}
	}
	
	private void applyClusterToWorld(ClusterableWorldObjectType type, int[][] tileMap, int[][] populatedMap, int inY, int inX) {
		int startCellY = inY - 9;
		int startCellX = inX - 9;
		// Copy our temporary map and apply it to our real one, creating any generated WorldObjects along the way
		for(int y = startCellY, locY = 0; y < (startCellY + 20); y++, locY++) {
			for(int x = startCellX, locX = 0; x < (startCellX + 20); x++, locX++) {
				// Check that we have actually generated a new object on the local world map array and that the relative spot
				// on the real map isn't over water
				if((y >= 0) && (y < populatedMap[0].length) && (x >= 0) && (x < populatedMap[0].length)) {
					if((world[locY][locX] > 0) && (tileMap[y][x] != 0)){
						WorldObjectType wObject = WorldObjectType.valueOf(type.toString() + "_STAGE_" + world[locY][locX]);
						// Set the world object on our actual map
						populatedMap[y][x] = populatedMap[y][x] | wObject.ordinal();
					}
				}
			}
		}
	}
}