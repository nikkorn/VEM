package gaia.server.world.tile.placement;

import gaia.server.world.container.Container;
import gaia.server.world.tile.placement.state.IPlacementState;

/**
 * Represents a placement modifiable in the context of an exectuing placement action.
 */
public interface IModifiablePlacement {
	
	/**
	 * Get the placement priority.
	 * @return The placement priority.
	 */
	public Priority getPriority();

	/**
	 * Set the placement priority.
	 * @param priority The placement priority.
	 */
	public void setPriority(Priority priority);
	
	/**
	 * Get the placement container.
	 * @return The placement container.
	 */
	public Container getContainer();
	
	/**
	 * Get the state of the placement.
	 * @return The state of the placement.
	 */
	public IPlacementState getState();
	
	/**
	 * Get the placement underlay.
	 * @return The placement underlay.
	 */
	public PlacementUnderlay getUnderlay();
	
	/**
	 * Set the placement underlay.
	 * @param underlay The placement underlay.
	 */
	public void setUnderlay(PlacementUnderlay underlay);
	
	/**
	 * Get the placement overlay.
	 * @return The placement overlay.
	 */
	public PlacementOverlay getOverlay();
	
	/**
	 * Set the placement overlay.
	 * @param overlay The placement overlay.
	 */
	public void setOverlay(PlacementOverlay overlay);
}
