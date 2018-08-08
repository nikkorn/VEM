package gaia.server.world.tile.placement;

import gaia.world.PlacementOverlay;
import gaia.world.PlacementType;
import gaia.world.PlacementUnderlay;

/**
 * Exposes the immutable properties of a placement.
 */
public interface IPlacementDetails {
	
	/**
	 * Get the placements x position within its parent chunk.
	 * @return The placements x position within its parent chunk.
	 */
	public short getX();

	/**
	 * Get the placements y position within its parent chunk.
	 * @return The placements y position within its parent chunk.
	 */
	public short getY();
	
	/**
	 * Get the placement type.
	 * @return The placement type.
	 */
	public PlacementType getType();
	
	/**
	 * Get the placement underlay.
	 * @return The placement underlay.
	 */
	public PlacementUnderlay getUnderlay();
	
	/**
	 * Get the placement overlay.
	 * @return The placement overlay.
	 */
	public PlacementOverlay getOverlay();
	
	/**
	 * Get the placement as a packed integer.
	 * @return The placement as a packed integer.
	 */
	public int asPackedInt();
}
