package gaia.server.world.tile.placement;

import java.util.Collection;
import java.util.HashMap;
import gaia.Constants;

/**
 * Represents a collection of placements within a world chunk.
 */
public class Placements {
	/**
	 * The map of placements keyed on position.
	 */
	private HashMap<String, Placement> placements = new HashMap<String, Placement>();
	
	/**
	 * Get a collection of all placements.
	 * @return A collection of all placements.
	 */
	public Collection<Placement> getAll() {
		return this.placements.values();
	}

	/**
	 * Get the placement at the x/y positon, or null if the position has no placement.
	 * @param x The x position of the placement.
	 * @param y The y position of the placement.
	 * @return The placement at the x/y positon, or null if the position has no placement.
	 */
	public Placement get(int x, int y) {
		// Create the key for the position.
		String placementKey = getPlacementKey(x, y);
		// Get the placement at the position, or null if there is no placement.
		return this.placements.get(placementKey);
	}
	
	/**
	 * Add a placement to this collection.
	 * @param placement The placement.
	 */
	public void add(Placement placement) {
		// Make sure that this is a valid placement position.
		if (!isValidPlacementPosition(placement.getX(), placement.getY())) {
			throw new RuntimeException("given an invalid placement position (x: " + placement.getX() + " y:" + placement.getY() +")");
		}
		// Add the placement.
		this.placements.put(getPlacementKey(placement.getX(), placement.getY()), placement);
	}
	
	/**
	 * Gets whether the x/y position is a valid placement position.
	 * @param x The x position of the placement.
	 * @param y The y position of the placement.
	 * @return Whether the x/y position is a valid placement position.
	 */
	private boolean isValidPlacementPosition(int x, int y) {
		return (x >= 0 && x < Constants.WORLD_CHUNK_SIZE) && (y >= 0 && y < Constants.WORLD_CHUNK_SIZE);
	}
	
	/**
	 * Gets a unique hash key for a placement x/y position.
	 * @param x The placement x position.
	 * @param y The placement y position.
	 * @return The key.
	 */
	private static String getPlacementKey(int x, int y) {
		return x + "-" + y;
	}
}
