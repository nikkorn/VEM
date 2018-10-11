package gaia.client.gamestate;

import java.util.HashMap;

/**
 * The client-side representation of the collection of positioned world placements.
 */
public class Placements implements IPlacements {
	/**
	 * The map of world position keys to placements.
	 */
	private HashMap<String, Placement> placements = new HashMap<String, Placement>();
	
	/**
	 * Get the placement at the specified x/y position.
	 * @param x The x position of the placement.
	 * @param y The y position of the placement.
	 * @return The placement at the specified x/y position.
	 */
	public Placement getPlacementAt(int x, int y) {
		return this.placements.get(createKey(x, y));
	}
	
	/**
	 * Get the details of a placement at the specified x/y position.
	 * @param x The x position of the placement.
	 * @param y The y position of the placement.
	 * @return The details of a placement at the specified x/y position.
	 */
	@Override
	public IPlacementDetails getPlacementDetails(int x, int y) {
		return this.placements.get(createKey(x, y));
	}
	
	/**
	 * Add a placement at the specified x/y position.
	 * @param placement The placement to add.
	 * @param x The x position to add the placement at.
	 * @param y The y position to add the placement at.
	 */
	public void add(Placement placement, int x, int y) {
		this.placements.put(createKey(x, y), placement);
	}
	
	/**
	 * Remove and return the placement at the specified x/y position. 
	 * @param x The x position to remove the placement from.
	 * @param y The y position to remove the placement from.
	 * @return The removed placement, or null if there was no placement at the position.
	 */
	public Placement remove(int x, int y) {
		return this.placements.remove(createKey(x, y));
	}
	
	/**
	 * Create a placement key based on position.
	 * @param x The placement x position.
	 * @param y The placement y position.
	 * @return A placement key based on position.
	 */
	private String createKey(int x, int y) {
		return x + "_" + y;
	}
}
