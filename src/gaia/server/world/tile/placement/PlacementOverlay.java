package gaia.server.world.tile.placement;

/**
 * Enumeration of placement overlays.
 */
public enum PlacementOverlay {
	NONE,
	PLANTED_SEED,
	PLANTED_SEED_SPROUTING;
	
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
