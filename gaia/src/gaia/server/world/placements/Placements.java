package gaia.server.world.placements;

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
	 * Get the placements at the x/y positon, or null if the position has no placements.
	 * @param x The x position of the placements.
	 * @param y The y position of the placements.
	 * @return The placements at the x/y positon, or null if the position has no placements.
	 */
	public Placement get(int x, int y) {
		// Create the key for the position.
		String placementKey = getPlacementKey(x, y);
		// Get the placements at the position, or null if there is no placements.
		return this.placements.get(placementKey);
	}
	
	/**
	 * Add a placements to this collection.
	 * @param placement The placements.
	 */
	public void add(Placement placement) {
		// Make sure that this is a valid placements position.
		if (!isValidPlacementPosition(placement.getX(), placement.getY())) {
			throw new RuntimeException("given an invalid placements position (x: " + placement.getX() + " y:" + placement.getY() +")");
		}
		// Add the placements.
		this.placements.put(getPlacementKey(placement.getX(), placement.getY()), placement);
	}
	
	/**
	 * Gets whether the x/y position is a valid placements position.
	 * @param x The x position of the placements.
	 * @param y The y position of the placements.
	 * @return Whether the x/y position is a valid placements position.
	 */
	private boolean isValidPlacementPosition(int x, int y) {
		return (x >= 0 && x < Constants.WORLD_CHUNK_SIZE) && (y >= 0 && y < Constants.WORLD_CHUNK_SIZE);
	}
	
	/**
	 * Gets a unique hash key for a placements x/y position.
	 * @param x The placements x position.
	 * @param y The placements y position.
	 * @return The key.
	 */
	private static String getPlacementKey(int x, int y) {
		return x + "-" + y;
	}
}
