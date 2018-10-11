package gaia.client.gamestate;

/**
 * An immutable collection of containers details.
 */
public interface IContainers {
	
	/**
	 * Get the details of a container at the specified x/y position.
	 * @param x The x position of the container.
	 * @param y The y position of the container.
	 * @return The details of a container at the specified x/y position.
	 */
	public IContainerDetails getContainerDetails(int x, int y);
}
