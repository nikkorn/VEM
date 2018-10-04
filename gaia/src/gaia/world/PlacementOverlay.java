package gaia.world;

/**
 * Enumeration of placement overlays.
 */
public enum PlacementOverlay {
	NONE,
	SEED_YELLOW,
	SEED_RED,
	SEED_BLUE,
	SEED_GREEN,
	SEED_ORANGE,
	SEED_PURPLE;
	
	/**
	 * Gets whether the placement overlay is walkable.
	 * @return Whether the placement overlay is walkable.
	 */
	public boolean isWalkable() {
		switch(this) {
			case NONE:
				return true;
			default:
				return false;
		}
	}
}
