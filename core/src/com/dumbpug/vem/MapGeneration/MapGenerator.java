package com.dumbpug.vem.MapGeneration;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.imageio.ImageIO;
import com.dumbpug.vem.C;
import com.dumbpug.vem.Math.Math;
import com.dumbpug.vem.Math.SimplexNoise;
import com.dumbpug.vem.World.WorldObjectType;

public class MapGenerator {
	private static float mapFreq 	= 0.0011f;  // THIS IS GREAT FOR WORLD MAP!
	private static float mapWeight 	= 1.1f;  
	private static int mapHeight 	= 4;
	
	// Used to generate clusters of WorldObjects
	WorldObjectClusterGenerator worldObjectClusterGenerator;
	
	public void generateMap() {
		File mapFile = new File("vem.map");
		SpawnPoint spawnPoint = new SpawnPoint();
		worldObjectClusterGenerator = new WorldObjectClusterGenerator();
		
		// TODO this will eventually not have a seed, seed is for testing
		Random ran = new Random(235);
		// Random ran = new Random();
		
		if(mapFile.exists()) {
			return;
		}

		// Generate a map tile and a biome tile to go over it.
		int tileMap[][] = genTileData(C.WORLD_CELL_SIZE, 0, 0, ran.nextInt(Integer.MAX_VALUE), mapFreq, mapWeight, mapHeight);
		
		// Generate tilemaps until we get one that meets our requirements
		int failCount = 0;
		int attemptLimit = 50;
		while(!mapMeetsRequirements(tileMap, spawnPoint) && (failCount < attemptLimit)) {
			tileMap = genTileData(C.WORLD_CELL_SIZE, 0, 0, ran.nextInt(Integer.MAX_VALUE), mapFreq, mapWeight, mapHeight);
			failCount++;
		}

		if(failCount != attemptLimit) {
			genMap("vemmap_img", tileMap, spawnPoint);
			tileMap = genMapWithWorldObjects(tileMap, spawnPoint, ran);
			// Write our actual map file to disk
			writeMapToFile(tileMap, spawnPoint);
			// Generate a new map familiarity save file.
			generateMapFamiliarityFile(spawnPoint);
			// Genereate VEM character special save file.
			generateCharacterSaveFile(spawnPoint);
		} else {
			System.out.println("We were not able to generate a valid map!");
		}
	}

	/**
	 * This method is required for determining 
	 * @param tileMap
	 * @param spawnPoint 
	 */
	private boolean mapMeetsRequirements(int[][] tileMap, SpawnPoint spawnPoint) {
		boolean isMapOk = true;
		// Here we will do a check to see if there are any parts of the map that 
		// are suitable for a spawn point (has the max map height and is far 
		// enough from map edge).
		int borderSize = 150;
		boolean foundSpawn = false;
		for (int z = borderSize; z < (tileMap[0].length - borderSize); z++) {
			for (int x = borderSize; x < (tileMap[0].length - borderSize); x++) {
				if(tileMap[z][x] == 4) {
					foundSpawn = true;
					spawnPoint.posY = z;
					spawnPoint.posX = x;
					break;
				}
			}
			if(foundSpawn) {
				break;
			}
		}
		return isMapOk;
	}

	/**
	 * Generate the bitmaps for the map
	 * @param outputFileName
	 * @param map
	 * @param spawnPoint
	 */
	public static void genMap(String outputFileName, int[][] map, SpawnPoint spawnPoint){
		File outputfile = new File("bin/map/", outputFileName + ".png");
		File outputConsoleFile = new File("bin/map/", outputFileName + "_console.png");
		BufferedImage img = new BufferedImage(map[0].length, map[0].length, BufferedImage.TYPE_INT_RGB);
		BufferedImage consoleImg = new BufferedImage(map[0].length, map[0].length, BufferedImage.TYPE_INT_RGB);
		for (int z = 0; z < map[0].length; z++) {
			for (int x = 0; x < map[0].length; x++) {
				// We don't want below sea level
				if(map[z][x] <= 0){
					// If we are below sea level, make this pixel sea blue :D
					int col = (16 << 16) | (46 << 8) | 120;
					int colCon = (48 << 16) | (48 << 8) | 48;
					img.setRGB(x, z, col);
					consoleImg.setRGB(x, z, colCon);
				} else {
					if(map[z][x] == 4f){
						// Light sand (safest, and where we can start)
						int col = (230 << 16) | (230 << 8) | 92;
						int colCon = (49 << 16) | (215 << 8) | 40;
						consoleImg.setRGB(x, z, colCon);
						img.setRGB(x, z, col);
					} else if(map[z][x] == 3f) {
						// Light sand 
						int col = (230 << 16) | (230 << 8) | 92;
						int colCon = (49 << 16) | (215 << 8) | 40;
						consoleImg.setRGB(x, z, colCon);
						img.setRGB(x, z, col);
					} else if(map[z][x] == 2f) {
						// Average sand
						int col = (220 << 16) | (220 << 8) | 82;
						int colCon = (49 << 16) | (205 << 8) | 40;
						consoleImg.setRGB(x, z, colCon);
						img.setRGB(x, z, col);
					} else {
						// Darker Sand (near ocean where resources are richer)
						int col = (210 << 16) | (210 << 8) | 78;
						int colCon = (49 << 16) | (215 << 8) | 40;
						consoleImg.setRGB(x, z, colCon);
						img.setRGB(x, z, col);
					}
				}
			}
		}
		// Draw marker for spawn point
		int col = (245 << 16) | (0 << 8) | 245;
		for(int z = spawnPoint.posY - 4; z <= spawnPoint.posY + 4; z++) {
			for(int x = spawnPoint.posX - 4; x <= spawnPoint.posX + 4; x++) {
				img.setRGB(z, x, col);
			}
		}
		// This will be drawn upside down, flip it
		AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -img.getHeight()));
        img = createTransformed(img, at);
        consoleImg = createTransformed(consoleImg, at);
        
		try {
			ImageIO.write(img, "png", outputfile);
			ImageIO.write(consoleImg, "png", outputConsoleFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Flips our image, totally stole this code 
	 * @param image
	 * @param at
	 * @return
	 */
	private static BufferedImage createTransformed(BufferedImage image, AffineTransform at)
    {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }
	
	/**
	 * Populates our world with WorldObjects
	 * @param tileMap
	 * @param spawnPoint
	 * @param ran
	 * @return
	 */
	private int[][] genMapWithWorldObjects(int[][] tileMap, SpawnPoint spawnPoint, Random ran) {
		int[][] populatedMap = new int[tileMap[0].length][tileMap[0].length];
		WorldObjectLottery wObjectLottery = new WorldObjectLottery();
		for (int y = 0; y < tileMap[0].length; y++) {
			for (int x = 0; x < tileMap[0].length; x++) {
				populatedMap[y][x] = 0;
				// Set the ground level first
				int groundLevel = tileMap[y][x] > 3 ? 3 : tileMap[y][x];
				if(groundLevel < 0) {
					groundLevel = 0;
				}
				populatedMap[y][x] = groundLevel << 6;
				// We have set the ground level. now generate some world objects (as long as were not too close to the spawn)
				int spawnRadius = 10;
				if(y > (spawnPoint.posY - spawnRadius) && y < (spawnPoint.posY + spawnRadius) 
						&& x > (spawnPoint.posX - spawnRadius) && x < (spawnPoint.posX + spawnRadius)) {
					// Do nothing here as this is in the spawn area and we will need to add certain things here manually.
				} else {
					WorldObjectType wObject = null;
					// TODO Randomly generate some world objects based on ground level.
					switch(groundLevel) {
					case 0:
						wObject = wObjectLottery.pick(WorldObjectLottery.WorldLevel.LEVEL_0, ran);
						break;
					case 1:
						wObject = wObjectLottery.pick(WorldObjectLottery.WorldLevel.LEVEL_1, ran);
						break;
					case 2:
						wObject = wObjectLottery.pick(WorldObjectLottery.WorldLevel.LEVEL_2, ran);
						break;
					case 3:
						wObject = wObjectLottery.pick(WorldObjectLottery.WorldLevel.LEVEL_3, ran);
						break;
					}
					// If the world object type is null then we didn't win. There will be no object at this z/x position.
					if(wObject != null) {
						//If we have a resource (like electrogem or scrappile) then let us generate a cluster for it
						if(wObject == WorldObjectType.GEM_STAGE_1 || wObject == WorldObjectType.SCRAP_STAGE_1
								|| wObject == WorldObjectType.GEM_STAGE_2 || wObject == WorldObjectType.SCRAP_STAGE_2
								|| wObject == WorldObjectType.GEM_STAGE_3 || wObject == WorldObjectType.SCRAP_STAGE_3) {
							// Lets us get the worldobject stage and type from the enum name. A bit dirty but should do the job.
							// TODO FIX!!!!!!!! int stage = Integer.parseInt(wObject.toString().split("_")[2]);
							// TODO FIX!!!!!!!! ClusterableWorldObjectType type = ClusterableWorldObjectType.valueOf(wObject.toString().split("_")[0]);
							// Generate a cluster around this winning worldobject
							// TODO FIX!!!!!!!! worldObjectClusterGenerator.generateCluster(type, tileMap, populatedMap, y, x, stage, ran);
						}
						// We now need to take the ordinal of the winning WorldObjectType and set that in our z/x position
						populatedMap[y][x] = populatedMap[y][x] | wObject.ordinal();
					}
				}
			}
		}
		//-----------------------------------------------
		// TODO manually add our spawn site world objects
		//-----------------------------------------------
		return populatedMap;
	}
	
	/**
	 * Creates the World Familiarity Save File
	 * @param spawnPoint
	 */
	private void generateMapFamiliarityFile(SpawnPoint spawnPoint) {
		File targetFile = new File(C.WORLD_FAMILIARITY_SAVE_FILE);
		PrintWriter pr = null;
		try {
			pr = new PrintWriter(targetFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// Using the spawn point for out or first familiar area
		int relativeAreaY =  (int) ((C.CONSOLE_MAP_FAMILIARITY_GRID_SIZE) * ((float) spawnPoint.posY / C.WORLD_CELL_SIZE));
		int relativeAreaX =  (int) ((C.CONSOLE_MAP_FAMILIARITY_GRID_SIZE) * ((float) spawnPoint.posX / C.WORLD_CELL_SIZE));
		pr.println(relativeAreaY + "_" + relativeAreaX);
		pr.flush();
		if(pr != null) {
			pr.close();
		}
	}
	
	/**
	 * Generate save file for main character entity
	 * @param spawnPoint
	 */
	private void generateCharacterSaveFile(SpawnPoint spawnPoint) {
		File targetFile = new File(C.ENTITY_VEM_SAVE_FILE);
		targetFile.getParentFile().mkdirs();
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
		pr.println(spawnPoint.posY + "_" + spawnPoint.posX); // Position
		pr.println("SOUTH"); // Direction
		pr.flush();
		if(pr != null) {
			pr.close();
		}
	}
	
	/**
	 * Writes the map file to disk
	 * @param tileMap
	 * @param spawnPoint
	 */
	private void writeMapToFile(int[][] tileMap, SpawnPoint spawnPoint) {
		File targetFile = new File("bin/map/vem.map");
		PrintWriter pr = null;
		try {
			pr = new PrintWriter(targetFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (int y = 0; y < tileMap[0].length; y++) {
			for (int x = 0; x < tileMap[0].length; x++) {
				pr.println(tileMap[y][x]);
			}
		}
		if(pr != null) {
			pr.close();
		}
	}
	
	private static int[][] genTileData(int size, int offsetx, int offsetz, int seed, float layerf, float weight, int height) {
		int[][] map = new int[size][size];
		float[][] noise = generateNoise(size, size, seed, offsetx, offsetz, layerf, weight);
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				map[x][z] = (int) (noise[x][z] * height);  
			}
		}
		return map;
	}
	
	private static float[][] generateNoise(int width, int height, int seed, int offxetx, int offsety, float freq, float weight) {
		SimplexNoise snt = new SimplexNoise(seed);
		float[][] noise = new float[width][height];
		for(int i = 0; i < 3; i++) {
			for(int x = 0; x < width; x++) {
				for(int y = 0; y < height; y++) {
					noise[x][y] += (float) snt.noise((x+offxetx) * freq, (y+offsety) * freq) * weight;
					noise[x][y] = Math.clamp(noise[x][y], -1.0f, 1.0f);
				}
			}
			freq *= 3.5f;
			weight *= 0.5f;
		}
		return noise;
	}
	
	public class SpawnPoint {
		public int posY;
		public int posX;
	}

	public boolean doesMapExist() {
		// return false;
		return new File("bin/map/vem.map").exists();
	}
}