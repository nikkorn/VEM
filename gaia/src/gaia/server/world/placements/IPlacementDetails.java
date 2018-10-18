package gaia.server.world.placements;

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
	 * Get the placements type.
	 * @return The placements type.
	 */
	public PlacementType getType();
	
	/**
	 * Get the placements underlay.
	 * @return The placements underlay.
	 */
	public PlacementUnderlay getUnderlay();
	
	/**
	 * Get the placements overlay.
	 * @return The placements overlay.
	 */
	public PlacementOverlay getOverlay();
	
	/**
	 * Get the placements as a packed integer.
	 * @return The placements as a packed integer.
	 */
	public int asPackedInt();
}
