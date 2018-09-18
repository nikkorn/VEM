package gaia.client.gamestate;

import gaia.world.PlacementOverlay;
import gaia.world.PlacementType;
import gaia.world.PlacementUnderlay;

/**
 * Represents immutable placement details.
 */
public interface IPlacementDetails {
	
	/**
	 * Get the placement type.
	 * @return The placement type.
	 */
	PlacementType getType();
	
	/**
	 * Get the placement underlay.
	 * @return The placement underlay.
	 */
	PlacementUnderlay getUnderlay();
	
	/**
	 * Get the placement overlay.
	 * @return The placement overlay.
	 */
	PlacementOverlay getOverlay();
}
