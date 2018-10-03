package gaia.world.items;

/**
 * Enumeration of item types.
 */
public enum ItemType {
	NONE,
	
	// Seeds
	SEED_YELLOW,
	SEED_RED,
	SEED_BLUE,
	SEED_GREEN,
	SEED_ORANGE,
	SEED_PURPLE,
	
	WOOD,
	FENCE_POST,
	GATE,
	SMALL_WATERING_CAN,
	SHOVEL,
	HOE,
	BACON;
	
	/**
	 * Gets the target of use for an item.
	 * @return The target of use for an item.
	 */ 
	public ItemTarget getTarget() {
		switch(this) {
			case BACON:
				return ItemTarget.PLAYER;
			case SMALL_WATERING_CAN:
			case SHOVEL:
			case HOE:
				return ItemTarget.FACING_TILE;
			default:
				return ItemTarget.NONE;
		}
	}
}
