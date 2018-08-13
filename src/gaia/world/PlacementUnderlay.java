package gaia.world;

/**
 * Enumeration of placements underlays.
 */
public enum PlacementUnderlay {
	NONE,
	BASIC_TREE,
	TILLED_EARTH,
	TILLED_EARTH_WET;
	
	/**
	 * Gets whether the placements underlay is walkable.
	 * @return Whether the placements underlay is walkable.
	 */
	public boolean isWalkable() {
		switch(this) {
			case NONE:
			case TILLED_EARTH:
			case TILLED_EARTH_WET:
				return true;
			default:
				return false;
		}
	}
}
