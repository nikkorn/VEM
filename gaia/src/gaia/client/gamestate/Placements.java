package gaia.client.gamestate;

import java.util.HashMap;

/**
 * The client-side representation of the collection of positioned world placements.
 */
public class Placements {
	/**
	 * The map of placements.
	 */
	private HashMap<String, Placement> placements = new HashMap<String, Placement>();
	
	/**
	 * Get the placement at the specified x/z position.
	 * @param x The x position of the placement.
	 * @param z The z position of the placement.
	 * @return The placement at the specified x/z position.
	 */
	public Placement getPlacementAt(int x, int z) {
		return this.placements.get(createKey(x, z));
	}
	
	/**
	 * Add a placement at the specified x/z position.
	 * @param placement The placement to add.
	 * @param x The x position to add the placement at.
	 * @param z The z position to add the placement at.
	 */
	public void add(Placement placement, int x, int z) {
		this.placements.put(createKey(x, z), placement);
	}
	
	/**
	 * Remove and return the placement at the specified x/z position. 
	 * @param x The x position to remove the placement from.
	 * @param z The z position to remove the placement from.
	 * @return The removed placement, or null if there was no placement at the position.
	 */
	public Placement remove(int x, int z) {
		return this.placements.remove(createKey(x, z));
	}
	
	/**
	 * Create a placement key based on position.
	 * @param x The placement x position.
	 * @param z The placement z position.
	 * @return A placement key based on position.
	 */
	private String createKey(int x, int z) {
		return x + "_" + z;
	}
}
