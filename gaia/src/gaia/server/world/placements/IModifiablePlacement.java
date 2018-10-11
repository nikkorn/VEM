package gaia.server.world.placements;

import gaia.server.world.items.container.Container;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementUnderlay;

/**
 * Represents a placements modifiable in the context of an executing placements action.
 */
public interface IModifiablePlacement {
	
	/**
	 * Get the placements priority.
	 * @return The placements priority.
	 */
	public Priority getPriority();

	/**
	 * Set the placements priority.
	 * @param priority The placements priority.
	 */
	public void setPriority(Priority priority);
	
	/**
	 * Get the placements container.
	 * @return The placements container.
	 */
	public Container getContainer();
	
	/**
	 * Get the placements underlay.
	 * @return The placements underlay.
	 */
	public PlacementUnderlay getUnderlay();
	
	/**
	 * Set the placements underlay.
	 * @param underlay The placements underlay.
	 */
	public void setUnderlay(PlacementUnderlay underlay);
	
	/**
	 * Get the placements overlay.
	 * @return The placements overlay.
	 */
	public PlacementOverlay getOverlay();
	
	/**
	 * Set the placements overlay.
	 * @param overlay The placements overlay.
	 */
	public void setOverlay(PlacementOverlay overlay);
	
	/**
	 * Mark this placement for deletion.
	 */
	public void MarkForDeletion();
}
