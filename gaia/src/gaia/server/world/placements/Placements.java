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
	 * The highest priority of any single placement.
	 */
	private Priority highestPriority = Priority.NONE;

	/**
	 * Get the placement at the x/y position, or null if the position has no placement.
	 * @param x The x position of the placement.
	 * @param y The y position of the placement.
	 * @return The placement at the x/y position, or null if the position has no placement.
	 */
	public Placement get(int x, int y) {
		// Create the key for the position.
		String placementKey = getPlacementKey(x, y);
		// Get the placements at the position, or null if there is no placements.
		return this.placements.get(placementKey);
	}
	
	/**
	 * Get a collection of all placements.
	 * @return A collection of all placements.
	 */
	public Collection<Placement> getAll() {
		return this.placements.values();
	}
	
	/**
	 * Gets whether the specified placement exists at the expected position.
	 * @param placement The placement.
	 * @return Whether the specified placement exists at the expected position.
	 */
	public boolean has(Placement placement) {
		// Create the key for the position.
		String placementKey = getPlacementKey(placement.getX(), placement.getY());
		// Does the placement exist at the expected position?
		return this.placements.containsKey(placementKey) && this.placements.get(placementKey) == placement;
	}
	
	/**
	 * Add a placements to this collection.
	 * @param placement The placements.
	 */
	public void add(Placement placement) {
		// Make sure that this is a valid placement position.
		if (!isValidPlacementPosition(placement.getX(), placement.getY())) {
			throw new RuntimeException("given an invalid placements position (x: " + placement.getX() + " y:" + placement.getY() +")");
		}
		// Add the placement.
		this.placements.put(getPlacementKey(placement.getX(), placement.getY()), placement);
		// We will need to re-evaluate the priorities of each placement to find the highest for this collection.
		this.evaluteHighestPriority();
	}
	
	/**
	 * Remove the placement at the x/y position if there is one.
	 * @param x The x position of the placement.
	 * @param y The y position of the placement.
	 */
	public void remove(int x, int y) {
		// Create the key for the position.
		String placementKey = getPlacementKey(x, y);
		// Remove the placement.
		if (this.placements.remove(placementKey) != null) {
			// We will need to re-evaluate the priorities of each placement to find the highest for this collection.
			this.evaluteHighestPriority();
		}
	}
	
	/**
	 * Remove the specified placement if it exists at the expected position.
	 * @param placement The placement to remove.
	 * @return Whether the placement was removed.
	 */
	public boolean remove(Placement placement) {
		// Create the key for the position.
		String placementKey = getPlacementKey(placement.getX(), placement.getY());
		// Only remove the placement if it exists at the expected position.
		if (this.placements.containsKey(placementKey) && this.placements.get(placementKey) == placement) {
			// Remove the placement.
			this.placements.remove(placementKey);
			// We will need to re-evaluate the priorities of each placement to find the highest for this collection.
			this.evaluteHighestPriority();
			// We were able to remove the placement.
			return true;
		}
		// We did not remove the placement.
		return false;
	}
	
	/**
	 * Get the highest priority of any single placement.
	 * @return The highest priority of any single placement.
	 */
	public Priority getHighestPriority() {
		return this.highestPriority;
	}
	
	/**
	 * Evaluate the priorities of each placement to find the highest for this collection.
	 */
	public void evaluteHighestPriority() {
		// Keep track of the highest priority so far.
		Priority highest = Priority.LOW;
		// Iterate over each placement to find the highest priority.
		for (Placement placement : this.placements.values()) {
			if (placement.getPriority().ordinal() > highest.ordinal()) {
				highest = placement.getPriority();
			}
		}
		// Set the highest placement priority for this collection.
		this.highestPriority = highest;
	}
	
	/**
	 * Gets whether the x/y position is a valid placements position.
	 * @param x The x position of the placements.
	 * @param y The y position of the placements.
	 * @return Whether the x/y position is a valid placements position.
	 */
	private static boolean isValidPlacementPosition(int x, int y) {
		return (x >= 0 && x < Constants.WORLD_CHUNK_SIZE) && (y >= 0 && y < Constants.WORLD_CHUNK_SIZE);
	}
	
	/**
	 * Gets a unique hash key for a placements x/y position.
	 * @param x The placements x position.
	 * @param y The placements y position.
	 * @return The key.
	 */
	public static String getPlacementKey(int x, int y) {
		return x + "-" + y;
	}
}
