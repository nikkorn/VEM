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
		long seed        = 3435655458l;
		Random rng       = new Random(seed);

		File outputfile   = new File(System.currentTimeMillis() + "_map.png");
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		
		// Randomly generate a z position for our map based on our seed.
		double z = rng.nextDouble() * (rng.nextInt(1000));
		
		// Generate each tile static tile and draw it to our world image.
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				// Generate noise for the current world position.
				double noise = generateNoise(worldSize, x, y, z);
				// Convert this piece of noise to a tile type.
				TileType tileType = convertNoiseToTileType((int)(noise * 10));
				// Draw the current tile to our world image.
				drawTileToWorldImage(img, x, y, tileType);
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
	 * @param x The x world position.
	 * @param y The y world position.
	 * @param tileType The tile type at the position.
	 */
	private static void drawTileToWorldImage(BufferedImage image, int x, int y, TileType tileType) {
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
		image.setRGB(x, y, colour);
	}
	
	/**
	 * Converts a piece of noise to a static world tile type.
	 * @param noise The piece of noise.
	 * @return The tile type.
	 */
	private static TileType convertNoiseToTileType(int noise) {
		switch(noise) {
			case 6:
			case 5:
				return TileType.FOREST;
			case 4:
			case 3:
				return TileType.GRASS;
			case 2:
				return TileType.PLAINS;
			case 1:
				return TileType.SAND;
			case 0:
				return TileType.OCEAN_SHALLOWS;
			case -1:
			case -2:
			case -3:
			case -4:
				return TileType.OCEAN;
			case -5:
			case -6:
			case -7:
				return TileType.OCEAN_DEEP;
			default:
				return noise < 0 ? TileType.OCEAN_DEEP : TileType.SNOW;
		}
	}

	/**
	 * Generate noise for a world position.
	 * @param x The x value.
	 * @param y The y value.
	 * @param z The z value.
	 * @return The noise value for the position.
	 */
	private static double generateNoise(int worldSize, int x, int y, double z) {
		// Frequency = features. Higher = more features.
		float frequency = 0.02f;
		float nx        = x * frequency;
		float ny        = y * frequency;
		// Generate a piece of noise for this x/y position.
		return ImprovedNoise.noise(nx, ny, z) + (0.2 * ImprovedNoise.noise(nx * 3, ny * 3, z)) + (0.25 * ImprovedNoise.noise(nx * 5, ny * 5, z));
	}
}
