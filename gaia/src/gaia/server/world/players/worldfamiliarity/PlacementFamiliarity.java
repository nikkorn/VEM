package gaia.server.world.players.worldfamiliarity;

import gaia.server.world.placements.IPlacementDetails;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementType;
import gaia.world.PlacementUnderlay;

/**
 * Represents a players familiarity with a placement.
 */
public class PlacementFamiliarity {
	/**
	 * The placement type.
	 */
	private PlacementType type;
	/**
	 * The placement underlay.
	 */
	private PlacementUnderlay underlay;
	/**
	 * The placement overlay.
	 */
	private PlacementOverlay overlay;
	
	/**
	 * Create a new instance of the PlacementFamiliarity class.
	 * @param placmement The placement that this familiarity represents.
	 */
	public PlacementFamiliarity(IPlacementDetails placement) {
		this.type     = placement.getType();
		this.underlay = placement.getUnderlay();
		this.overlay  = placement.getOverlay();
	}
	
	/**
	 * Get whether the specified placement matches what the player is familiar with and update differences.
	 * @param placement The placement we are checking mathces this familiarity.
	 * @return Whether the specified placement matches what the player is familiar with.
	 */
	public boolean compareAndUpdate(IPlacementDetails placement) {
		boolean matches = true;
		// Is there a type mismatch?
		if (this.type != placement.getType()) {
			this.type = placement.getType();
			matches   = false;
		}
		// Is there an underlay mismatch?
		if (this.underlay != placement.getUnderlay()) {
			this.underlay = placement.getUnderlay();
			matches       = false;
		}
		// Is there an overlay mismatch?
		if (this.overlay != placement.getOverlay()) {
			this.overlay = placement.getOverlay();
			matches   = false;
		}
		// Return whether the placement matched our familiarity with it.
		return matches;
	}
}