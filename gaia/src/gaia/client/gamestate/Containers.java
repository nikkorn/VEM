package gaia.client.gamestate;

import java.util.HashMap;

/**
 * The client-side representation of the collection of positioned containers in the world.
 */
public class Containers implements IContainers {
	/**
	 * The map of world position keys to containers.
	 */
	private HashMap<String, Container> containers = new HashMap<String, Container>();
	
	/**
	 * Get the container at the specified x/y position.
	 * @param x The x position of the container.
	 * @param y The y position of the container.
	 * @return The placement at the specified x/y container.
	 */
	public Container getContainerAt(int x, int y) {
		return this.containers.get(createKey(x, y));
	}
	
	/**
	 * Get the details of a container at the specified x/y position.
	 * @param x The x position of the container.
	 * @param y The y position of the container.
	 * @return The details of a container at the specified x/y position.
	 */
	@Override
	public IContainerDetails getContainerDetails(int x, int y) {
		return this.containers.get(createKey(x, y));
	}
	
	/**
	 * Add a container at the specified x/y position.
	 * @param placement The container to add.
	 * @param x The x position to add the container at.
	 * @param y The y position to add the container at.
	 */
	public void add(Container container, int x, int y) {
		this.containers.put(createKey(x, y), container);
	}
	
	/**
	 * Remove and return the container at the specified x/y position. 
	 * @param x The x position to remove the container from.
	 * @param y The y position to remove the container from.
	 * @return The removed container, or null if there was no container at the position.
	 */
	public Container remove(int x, int y) {
		return this.containers.remove(createKey(x, y));
	}
	
	/**
	 * Create a container key based on position.
	 * @param x The container x position.
	 * @param y The container y position.
	 * @return A container key based on position.
	 */
	private String createKey(int x, int y) {
		return x + "_" + y;
	}
}
