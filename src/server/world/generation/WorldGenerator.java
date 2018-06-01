package server.world.generation;

import java.util.Random;
import server.Constants;
import server.world.tile.TileType;

/**
 * Generates portions of the world based on noise and RNG.
 */
public class WorldGenerator {
	/**
	 * The world height offset on the Y axis.
	 * Seeding Perlin noise is just a matter of applying a random offset to an axis.
	 */
	private double yOffset;
	/**
	 * The seed.
	 */
	private long seed;
	/**
	 * The world tile lottos.
	 * These lottos are used in determining whether any static tiles should be substituted based on position.
	 */
	private TileLottos tileLottos = new TileLottos();
	
	/**
	 * Creates a new instance of the WorldGenerator class.
	 * @param seed The world seed.
	 */
	public WorldGenerator(long seed) {
		// Our world size must be an even number.
		if ((Constants.WORLD_SIZE % 2) != 0) {
			throw new RuntimeException("World size must be an even number!");
		}
		// Randomly generate a y axis offset for our map based on our seed.
		this.seed  = seed;
		Random rng = new Random(seed);
		yOffset    = rng.nextDouble() * (rng.nextInt(1000));
	}

	/**
	 * Get the seed.
	 * @return The seed.
	 */
	public long getSeed() {
		return this.seed;
	}
	
	/**
	 * Determine the type of tile at the specified position.
	 * @param x The x position of the tile.
	 * @param z The z position of the tile.
	 * @return The tile type.
	 */
	public TileType getTileAt(int x, int z) {
		// Calculate the distance between the world centre and the edge.
		int centreToEdge = Constants.WORLD_SIZE / 2;
		// Generate terrain noise for the current world position.
		int terrainNoise = (int)(generateNoise(0.02f, x, yOffset, z) * 10);
		// Generate biome noise for the current world position.
		int biomeNoise = (int)(generateNoise(0.025f, x, yOffset + 100, z) * 10);
		// Generate boundary noise for the current world position.
		// This value will raise the edges of the world to produce the cold lands.
		int boundaryNoise = (int)(generateBoundaryNoise(0.025f, centreToEdge, x, yOffset, z) * 5);
		// Convert this piece of noise to a tile type and return it.
		return convertNoiseToTileType(terrainNoise - boundaryNoise, biomeNoise, getPositionSeed(x, z));
	}
	
	/**
	 * Get a unique seed for a position.
	 * @param x The tile x position.
	 * @param y The tile y position.
	 * @return A unique seed for a position.
	 */
	public long getPositionSeed(int x, int y) {
		return seed + (x << 16 + y);
	}
	
	/**
	 * Converts a piece of noise to a static world tile type.
	 * @param terrainNoise The piece of terrain noise.
	 * @param biomeNoise The piece of biome noise.
	 * @param biomeNoise The unique seed for the position at which we are creating a tile.
	 * @return The tile type.
	 */
	private TileType convertNoiseToTileType(int terrainNoise, int biomeNoise, long positionSeed) {
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
		// We may need to substitute the tile type.
		type = this.tileLottos.getSubstitutionForTile(type, positionSeed);
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
	private static double generateBoundaryNoise(float frequency, int centreToEdge, int x, double y, int z) {
		// Generate a Manhattan distance value.
		double distance = Math.abs(x) + Math.abs(z);
		// Generate a piece of noise for this x/z/y position.
		return (ImprovedNoise.noise(x * frequency, y, z * frequency) + 0.8) * (1 - Math.pow(distance / centreToEdge, 3));
	}

	/**
	 * Generate noise for a world position.
	 * @param frequency The frequency.
	 * @param x The x value.
	 * @param y The y value.
	 * @param z The z value.
	 * @return The noise value for the position.
	 */
	private static double generateNoise(float frequency, int x, double y, int z) {
		float nx = x * frequency;
		float nz = z * frequency;
		// Generate a piece of noise for this x/z/y position.
		return ImprovedNoise.noise(nx, y, nz) + (0.2 * ImprovedNoise.noise(nx * 3, y, nz * 3)) + (0.25 * ImprovedNoise.noise(nx * 5, y, nz * 5));
	}
}
