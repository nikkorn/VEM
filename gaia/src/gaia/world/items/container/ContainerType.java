package gaia.world.items.container;

/**
 * Enumeration of container types.
 */
public enum ContainerType {
	UNKNOWN,
	CHEST;
	
	/**
	 * Gets the name of the container based on its type.
	 * @return The name of the container based on its type.
	 */
	public String getName() {
		switch(this) {
			case CHEST:
				return "Chest";
			default:
				return "";
		}
	}
	
	/**
	 * Gets the default number of slots for the container type.
	 * @return The default number of slots for the container type.
	 */
	public int getNumberOfSlots() {
		switch(this) {
			case CHEST:
				return 20;
			default:
				return 5;
		}
	}
}
