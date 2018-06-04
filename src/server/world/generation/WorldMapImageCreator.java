package server.world.generation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import server.Constants;
import server.world.tile.TileType;

/**
 * Creates world map images to write to disk.
 */
public class WorldMapImageCreator {
	
	/**
	 * Create a world map image and write it to disk.
	 * @param generator The world generator to use.
	 * @param name The map name.
	 * @param path The path.
	 */
	public static void create(WorldGenerator generator, String name, String path) {
		File outputfile   = new File(path + name + ".png");
		BufferedImage img = new BufferedImage(Constants.WORLD_SIZE, Constants.WORLD_SIZE, BufferedImage.TYPE_INT_RGB);
		
		// Calculate the distance between the world centre and the edge.
		int centreToEdge = Constants.WORLD_SIZE / 2;
		
		// Generate each static tile and draw it to our world image.
		for (int x = -centreToEdge; x < centreToEdge; x++) {
			for (int z = -centreToEdge; z < centreToEdge; z++) {
				// Draw the current tile to our world image.
				drawTileToWorldImage(img, centreToEdge, x, z, generator.getTileAt(x, z));
			}
		}
		
		// Try to write the image to disk.
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
	 * @param z The z world position.
	 * @param tileType The tile type at the position.
	 */
	private static void drawTileToWorldImage(BufferedImage image, int centreToEdge, int x, int z, TileType tileType) {
		int r = 0, g = 0, b = 0;
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
			case GRASS_TUFT_1:
				r = 72;
				g = 192; 
				b = 33;
				break;
			case GRASS_TUFT_2:
				r = 78;
				g = 170; 
				b = 32;
				break;
			case GRASS_FLOWERS_1:
				r = 225;
				g = 159; 
				b = 249;
				break;
			case GRASS_FLOWERS_2:
				r = 146;
				g = 239; 
				b = 225;
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
			case PLAINS_GRASS_TUFT:
				r = 185;
				g = 210; 
				b = 70;
				break;
			case PLAINS_GRASS_FLOWER:
				r = 198;
				g = 225; 
				b = 78;
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
		image.setRGB(x + centreToEdge, z + centreToEdge, colour);
	}
}
