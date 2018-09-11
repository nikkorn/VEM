package gaia.client.gamestate;

/**
 * An immutable collection of placement's details.
 */
public interface IPlacements {
	
	/**
	 * Get the details of a placement at the specified x/y position.
	 * @param x The x position of the placement.
	 * @param y The y position of the placement.
	 * @return The details of a placement at the specified x/y position.
	 */
	public IPlacementDetails getPlacementDetails(int x, int y);
}
