package gaia.server.world.players;

import java.util.HashMap;
import gaia.server.world.placements.IPlacementDetails;
import gaia.server.world.placements.Placements;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementType;
import gaia.world.PlacementUnderlay;

/**
 * A representation of which placements a player knows about in the world.
 */
public class PlayerWorldFamiliarity {
	/**
	 * The mapping of world positions to placement familiarities.
	 */
	private HashMap<String, PlacementFamiliarity> placementFamiliarities = new HashMap<String, PlacementFamiliarity>();
	
	/**
	 * Add a familiarity for a placement.
	 * @param placement The placement to add a familiarity for.
	 * @param x The x position of the placement.
	 * @param y The y position of the placement.
	 */
	public void addFamiliarity(IPlacementDetails placement, short x, short y) {
		// Get the placement position key.
		String key = Placements.getPlacementKey(x, y);
		// Add a familiarity for the placement.
		placementFamiliarities.put(key, new PlacementFamiliarity(placement));
	}
	
	/**
	 * Compare a placement at a position with what the player expects at the positition.
	 * @param placement The placement we are comparing to the player's familiarity, or null if there is no placement.
	 * @param x The x position of the placement.
	 * @param y The y position of the placement.
	 * @return Whether the player's expectation of what is at the position matches reality. 
	 */
	public boolean compareAndUpdate(IPlacementDetails placement, short x, short y) {
		// Get the placement position key.
		String key = Placements.getPlacementKey(x, y);
		// Is there a placement at the position?
		if (placement != null) {
			// Does the player know that there is no placement at this position?
			if (placementFamiliarities.containsKey(key)) {
				// The player knows that there is a placement at the position, but do they match?
				return placementFamiliarities.get(key).compareAndUpdate(placement);
			} else {
				// The player thought that there was no placement at this position but there is.
				// Update the familiarities to reflect the fact that there is a placement at the position.
				placementFamiliarities.put(key, new PlacementFamiliarity(placement));
				return false;
			}
		} else {
			// Does the player know that there is no placement at this position?
			if (!placementFamiliarities.containsKey(key)) {
				return true;
			} else {
				// The player thought that there was a placement at this position but there isn't.
				// Update the familiarities to reflect the fact that there is no placement at the position.
				placementFamiliarities.remove(key);
				return false;
			}
		}
	}
	
	/**
	 * Represents a players familiarity with a placement.
	 */
	private class PlacementFamiliarity {
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
}
