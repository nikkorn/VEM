package server.world.generation;

import java.util.Random;

/**
 * Test creating a world and daring it to a file.
 */
public class PerlinTest {

	public static void main(String[] args){
		// Create a world generator, passing a random seed.
		WorldGenerator generator = new WorldGenerator(new Random().nextLong());
		// Create a world map image using our generator.
		WorldMapImageCreator.create(generator, System.currentTimeMillis() + "_map", "");
    }
}
