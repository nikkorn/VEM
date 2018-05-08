package server.world.generation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

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

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				int r = 0;
				int g = 0;
				int b = 0;
				
				double noise = generateNoise(worldSize, x, y, z);
				
				//System.out.println("NOISE: " + noise);

				r = 0; // 0-255
				g = 0; // 0-255
				b = (int) (noise * 1200);

				int col = (r << 16) | (g << 8) | b;
				img.setRGB(x, y, col);
			}
		}

		try {
			ImageIO.write(img, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generate some noise!
	 * @param x
	 * @param y
	 * @param z
	 * @param seed
	 * @return noise!
	 */
	private static double generateNoise(int worldSize, int x, int y, double z) {
		// Frequency = features. Higher = more features
		float frequency = 0.02f;
		float nx        = x * frequency;
		float ny        = y * frequency;
		// Generate a piece of noise for this x/y position.
		return ImprovedNoise.noise(nx, ny, z) + (0.2 * ImprovedNoise.noise(nx * 3, ny * 3, z)) + (0.25 * ImprovedNoise.noise(nx * 5, ny * 5, z));
	}
}
