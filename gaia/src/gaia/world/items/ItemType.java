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
	BACON,
	
	// Food
	REDBERRY;
	
	/**
	 * Gets the target of use for an item.
	 * @return The target of use for an item.
	 */ 
	public ItemTarget getTarget() {
		switch(this) {
			case BACON:
				return ItemTarget.PLAYER;
			case SEED_YELLOW:
			case SEED_RED:
			case SEED_BLUE:
			case SEED_GREEN:
			case SEED_ORANGE:
			case SEED_PURPLE:
			case SMALL_WATERING_CAN:
			case SHOVEL:
			case HOE:
				return ItemTarget.FACING_TILE;
			default:
				return ItemTarget.NONE;
		}
	}
	
	/**
	 * Gets the name of an item.
	 * @return The name of an item.
	 */ 
	public String getName() {
		switch(this) {
			case NONE:
				return "";
			case BACON:
				return "Bacon";
			case FENCE_POST:
				return "Fence Post";
			case GATE:
				return "Gate";
			case HOE:
				return "Hoe";
			case REDBERRY:
				return "RedBerry";
			case SEED_BLUE:
				return "Blue Seed";
			case SEED_GREEN:
				return "Green Seed";
			case SEED_ORANGE:
				return "Orange Seed";
			case SEED_PURPLE:
				return "Purple Seed";
			case SEED_RED:
				return "Red Seed";
			case SEED_YELLOW:
				return "Yellow Seed";
			case SHOVEL:
				return "Shovel";
			case SMALL_WATERING_CAN:
				return "Small Watering Can";
			case WOOD:
				return "Wood";
			default:
				return "???";
		}
	}
	
	/**
	 * Gets whether an item is of the NONE type.
	 * @return Whether an item is of the NONE type.
	 */ 
	public boolean isNothing() {
		return this == ItemType.NONE;
	}
}
