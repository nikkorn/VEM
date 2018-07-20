package gaia.server.world.tile.placement;

/**
 * Enumeration of placement underlays.
 */
public enum PlacementUnderlay {
	NONE,
	BASIC_TREE,
	TILLED_EARTH,
	TILLED_EARTH_WET;
	
	/**
	 * Gets whether the placement underlay is walkable.
	 * @return Whether the placement underlay is walkable.
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
