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
	 * Update the player's familiarity with a position.
	 * @param placement The placement at the position, or null if there is no placement.
	 * @param x The x position of the placement.
	 * @param y The y position of the placement.
	 */
	public void update(IPlacementDetails placement, short x, short y) {
		// Get the placement position key.
		String key = Placements.getPlacementKey(x, y);
		// Are we updating familiarity with a now-deleted placement?
		if (placement == null) {
			// Update the player's familiarity with the now non-existent placement.
			placementFamiliarities.remove(key);
		} else {
			// Update the player's familiarity with the placement at the position.
			placementFamiliarities.put(key, new PlacementFamiliarity(placement));
		}
	}
	
	/**
	 * Compare a placement at a position with what the player expects at the position.
	 * @param placement The placement we are comparing to the player's familiarity, or null if there is no placement.
	 * @param x The x position of the placement.
	 * @param y The y position of the placement.
	 * @return The player's expectation of what is at the position. 
	 */
	public WorldFamiliarityExpectation compareAndUpdate(IPlacementDetails placement, short x, short y) {
		// Get the placement position key.
		String key = Placements.getPlacementKey(x, y);
		// Is there a placement at the position?
		if (placement != null) {
			// Does the player know that there is no placement at this position?
			if (placementFamiliarities.containsKey(key)) {
				// The player knows that there is a placement at the position, but do they match?
				if (placementFamiliarities.get(key).compareAndUpdate(placement)) {
					// They match!
					return WorldFamiliarityExpectation.AS_EXPECTED;
				} else {
					// They do not match!
					return WorldFamiliarityExpectation.EXPECTED_DIFFERENT_PLACEMENT_STATE;
				}
			} else {
				// The player thought that there was no placement at this position but there is.
				// Update the familiarities to reflect the fact that there is a placement at the position.
				placementFamiliarities.put(key, new PlacementFamiliarity(placement));
				return WorldFamiliarityExpectation.EXPECTED_NO_PLACEMENT;
			}
		} else {
			// Does the player know that there is no placement at this position?
			if (!placementFamiliarities.containsKey(key)) {
				return WorldFamiliarityExpectation.AS_EXPECTED;
			} else {
				// The player thought that there was a placement at this position but there isn't.
				// Update the familiarities to reflect the fact that there is no placement at the position.
				placementFamiliarities.remove(key);
				return WorldFamiliarityExpectation.EXPECTED_PLACEMENT;
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
