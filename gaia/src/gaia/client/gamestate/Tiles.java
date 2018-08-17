package gaia.client.gamestate;

import java.util.HashMap;
import gaia.Constants;
import gaia.world.TileType;
import gaia.world.generation.TileGenerator;

/**
 * The client-side representation of the collection of static world tiles.
 */
public class Tiles {
	/**
	 * The map of tiles.
	 */
	private HashMap<String, TileType> tiles = new HashMap<String, TileType>();
	
	private Tiles() {}
	
	/**
	 * Get the tile at the specified x/z position.
	 * @param x The x position of the tile.
	 * @param z The z position of the tile.
	 * @return The tile at the specified x/z position.
	 */
	public TileType getTileAt(int x, int z) {
		return this.tiles.get(createKey(x, z));
	}
	
	/**
	 * Generate a collection of tiles using a tile generator.
	 * @param tileGenerator The tile generator.
	 * @return A collection of tiles using a tile generator.
	 */
	public static Tiles generate(TileGenerator tileGenerator) {
		Tiles tiles = new Tiles();
		// Calculate the distance between the world centre and the edge.
		int centreToEdge = Constants.WORLD_SIZE / 2;
		// Generate each static tile and add it to our tiles array.
		for (int x = -centreToEdge; x < centreToEdge; x++) {
			for (int z = -centreToEdge; z < centreToEdge; z++) {
				tiles.add(tileGenerator.getTileAt(x, z), x, z);
			}
		}
		// Return the tiles collection.
		return tiles;
	}
	
	/**
	 * Add a tile at the specified x/z position.
	 * @param type The type of tile to add.
	 * @param x The x position to add the tile at.
	 * @param z The z position to add the tile at.
	 */
	private void add(TileType tileType, int x, int z) {
		this.tiles.put(createKey(x, z), tileType);
	}
	
	/**
	 * Create a tile key based on position.
	 * @param x The tile x position.
	 * @param z The tile z position.
	 * @return A tile key based on position.
	 */
	private String createKey(int x, int z) {
		return x + "_" + z;
	}
}
