package server.world.generation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import server.world.TileType;

public class PerlinTest {

	public static void main(String[] args){
        test();
    }

	private static void test() {
		int size         = 1024;
		int worldSize    = 1024;
		long seed        = 11223344l;
		Random rng       = new Random(seed);

		File outputfile   = new File(System.currentTimeMillis() + "_map.png");
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		
		// Our world size must be an even number.
		if ((worldSize % 2) != 0) {
			throw new RuntimeException("World size must be an even number!");
		}
		
		// Calculate the distance between the world centre and the edge.
		int centreToEdge = worldSize / 2;
		
		// Randomly generate a z position for our map based on our seed.
		double z = rng.nextDouble() * (rng.nextInt(1000));
		
		// Generate each tile static tile and draw it to our world image.
		for (int x = -centreToEdge; x < centreToEdge; x++) {
			for (int y = -centreToEdge; y < centreToEdge; y++) {
				// Generate terrain noise for the current world position.
				int terrainNoise = (int)(generateNoise(0.02f, x, y, z) * 10);
				// Generate biome noise for the current world position.
				int biomeNoise = (int)(generateNoise(0.025f, x, y, z + 100) * 10);
				// Generate boundary noise for the current world position.
				// This value will raise the edges of the world to produce the cold lands.
				int boundaryNoise = (int)(generateBoundaryNoise(0.025f, centreToEdge, x, y, z) * 5);
				// Convert this piece of noise to a tile type.
				TileType tileType = convertNoiseToTileType(terrainNoise - boundaryNoise, biomeNoise);
				// Draw the current tile to our world image.
				drawTileToWorldImage(img, centreToEdge, x, y, tileType);
			}
		}
		
		try {
			ImageIO.write(img, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Draw a tile as a pixel to a world image.
	 * @param image The world image.
	 * @param centreToEdge The distance between the world centre and the edge.
	 * @param x The x world position.
	 * @param y The y world position.
	 * @param tileType The tile type at the position.
	 */
	private static void drawTileToWorldImage(BufferedImage image, int centreToEdge, int x, int y, TileType tileType) {
		int r = 0;
		int g = 0;
		int b = 0;
		// The pixel colour will depend on the tile type.
		switch (tileType) {
			case FOREST:
				r = 30;
				g = 124; 
				b = 3;
				break;
			case GRASS:
				r = 68;
				g = 186; 
				b = 35;
				break;
			case OCEAN:
				r = 64;
				g = 170; 
				b = 237;
				break;
			case OCEAN_DEEP:
				r = 3;
				g = 105; 
				b = 168;
				break;
			case OCEAN_SHALLOWS:
				r = 176;
				g = 223; 
				b = 254;
				break;
			case PLAINS:
				r = 192;
				g = 219; 
				b = 74;
				break;
			case SAND:
				r = 247;
				g = 241; 
				b = 64;
				break;
			case METAL:
				r = 255;
				g = 0; 
				b = 0;
				break;
			case ROCK:
				r = 206;
				g = 112; 
				b = 61;
				break;
			case SNOW:
				r = 255;
				g = 255; 
				b = 255;
				break;
			default:
				break;
		}
		// Create the colour.
		int colour = (r << 16) | (g << 8) | b;
		// Set the pixel colour at the x/y position.
		image.setRGB(x + centreToEdge, y + centreToEdge, colour);
	}
	
	/**
	 * Converts a piece of noise to a static world tile type.
	 * @param terrainNoise The piece of terrain noise.
	 * @param biomeNoise The piece of biome noise.
	 * @return The tile type.
	 */
	private static TileType convertNoiseToTileType(int terrainNoise, int biomeNoise) {
		TileType type = null;
		// Get the initial tile type, without consideration for biomes.
		switch(terrainNoise) {
			case 6:
			case 5:
				type = TileType.FOREST;
				break;
			case 4:
			case 3:
				type = TileType.GRASS;
				break;
			case 2:
				type = TileType.PLAINS;
				break;
			case 1:
				type = TileType.SAND;
				break;
			case 0:
				type = TileType.OCEAN_SHALLOWS;
				break;
			case -1:
			case -2:
			case -3:
			case -4:
				type = TileType.OCEAN;
				break;
			case -5:
			case -6:
			case -7:
				type = TileType.OCEAN_DEEP;
				break;
			default:
				type = terrainNoise < 0 ? TileType.OCEAN_DEEP : TileType.SNOW;
		}
		// Apply biome noise to tile types that vary between biomes.
		switch(biomeNoise) {
			case 7:
			case 6:
				type = type == TileType.FOREST ? TileType.METAL : type;
				break;
			case 5:
			case 4:
			case 3:
				type = type == TileType.GRASS ? TileType.FOREST : type;
				break;
			case 2:
				type = type == TileType.GRASS ? TileType.ROCK : type;
				break;
			case 1:
			case 0:
			case -1:
			case -2:
				type = type == TileType.SAND ? TileType.PLAINS : type;
				type = type == TileType.OCEAN_SHALLOWS ? TileType.OCEAN : type;
				break;
			case -3:
			case -4:
			case -5:
			case -6:
			case -7:
			default:
				type = TileType.OCEAN;
				break;
		}
		// Return the tile type.
		return type;
	}
	
	/**
	 * Generate boundary noise for a world position.
	 * @param frequency The frequency.
	 * @param centreToEdge The distance between the world centre and the edge.
	 * @param x The x value.
	 * @param y The y value.
	 * @param z The z value.
	 * @return The noise value for the position.
	 */
	private static double generateBoundaryNoise(float frequency, int centreToEdge, int x, int y, double z) {
		// Generate a Manhattan distance value.
		double distance = Math.abs(x) + Math.abs(y);
		// Generate a piece of noise for this x/y position.
		return (ImprovedNoise.noise(x * frequency, y * frequency, z) + 0.8) * (1 - Math.pow(distance / centreToEdge, 3));
	}

	/**
	 * Generate noise for a world position.
	 * @param frequency The frequency.
	 * @param x The x value.
	 * @param y The y value.
	 * @param z The z value.
	 * @return The noise value for the position.
	 */
	private static double generateNoise(float frequency, int x, int y, double z) {
		float nx = x * frequency;
		float ny = y * frequency;
		// Generate a piece of noise for this x/y position.
		return ImprovedNoise.noise(nx, ny, z) + (0.2 * ImprovedNoise.noise(nx * 3, ny * 3, z)) + (0.25 * ImprovedNoise.noise(nx * 5, ny * 5, z));
	}
}
