package gaia.world;

/**
 * Enumeration of placements overlays.
 */
public enum PlacementOverlay {
	NONE,
	PLANTED_SEED,
	PLANTED_SEED_SPROUTING;
	
	/**
	 * Gets whether the placements overlay is walkable.
	 * @return Whether the placements overlay is walkable.
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
