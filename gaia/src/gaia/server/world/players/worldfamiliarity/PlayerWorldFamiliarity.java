package gaia.server.world.players.worldfamiliarity;

import java.util.HashMap;
import gaia.server.world.items.container.Container;
import gaia.server.world.placements.IPlacementDetails;
import gaia.server.world.placements.Placements;

/**
 * A representation of which placements and containers a player knows about in the world.
 */
public class PlayerWorldFamiliarity {
	/**
	 * The mapping of world positions to placement familiarities.
	 */
	private HashMap<String, PlacementFamiliarity> placementFamiliarities = new HashMap<String, PlacementFamiliarity>();
	/**
	 * The mapping of world positions to container familiarities.
	 */
	private HashMap<String, ContainerFamiliarity> containerFamiliarities = new HashMap<String, ContainerFamiliarity>();

	/**
	 * Update the player's familiarity with a position.
	 * @param placement The placement at the position, or null if there is no placement.
	 * @param x The x position of the placement.
	 * @param y The y position of the placement.
	 */
	public void updatePlacementFamiliarity(IPlacementDetails placement, short x, short y) {
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
	 * Update the player's familiarity with a container.
	 * @param container The container at the position, or null if there is no container.
	 * @param x The x position of the container.
	 * @param y The y position of the container.
	 */
	public void updateContainerFamiliarity(Container container, short x, short y) {
		// ...
	}
	
	/**
	 * Compare a placement at a position with what the player expects at the position.
	 * @param placement The placement we are comparing to the player's familiarity, or null if there is no placement.
	 * @param x The x position of the placement.
	 * @param y The y position of the placement.
	 * @return The player's expectation of what is at the position. 
	 */
	public WorldFamiliarityExpectation compareAndUpdatePlacementFamiliarity(IPlacementDetails placement, short x, short y) {
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
					return WorldFamiliarityExpectation.EXPECTED_DIFFERENT_STATE;
				}
			} else {
				// The player thought that there was no placement at this position but there is.
				// Update the familiarities to reflect the fact that there is a placement at the position.
				placementFamiliarities.put(key, new PlacementFamiliarity(placement));
				return WorldFamiliarityExpectation.EXPECTED_NOT_PRESENT;
			}
		} else {
			// Does the player know that there is no placement at this position?
			if (!placementFamiliarities.containsKey(key)) {
				return WorldFamiliarityExpectation.AS_EXPECTED;
			} else {
				// The player thought that there was a placement at this position but there isn't.
				// Update the familiarities to reflect the fact that there is no placement at the position.
				placementFamiliarities.remove(key);
				return WorldFamiliarityExpectation.EXPECTED_PRESENT;
			}
		}
	}
	
	/**
	 * Compare a container at a position with what the player expects at the position.
	 * @param container The container we are comparing to the player's familiarity, or null if there is no container.
	 * @param x The x position of the container.
	 * @param y The y position of the container.
	 * @return The player's expectation of what is at the position. 
	 */
	public WorldFamiliarityExpectation compareAndUpdateContainerFamiliarity(Container container, short x, short y) {
		// ...
		return WorldFamiliarityExpectation.AS_EXPECTED;
	}
}
