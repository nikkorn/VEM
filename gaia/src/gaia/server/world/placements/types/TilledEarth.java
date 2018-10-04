package gaia.server.world.placements.types;

import java.util.Random;
import org.json.JSONObject;
import gaia.server.world.placements.IModifiablePlacement;
import gaia.server.world.placements.Placement;
import gaia.time.Time;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementType;
import gaia.world.items.ItemType;

/**
 * A 'Tilled Earth' placement.
 */
public class TilledEarth extends Placement {
	/**
	 * The number of minutes that the tilled earth is watered for.
	 */
	public int wateredTicks = 100;
	/**
	 * The seed that is planted.
	 */
	private ItemType plantedSeed = ItemType.NONE;

	/**
	 * Create a new instance of the TilledEarth class.
	 * @param x The placements x position within its parent chunk.
	 * @param y The placements y position within its parent chunk.
	 */
	public TilledEarth(short x, short y) {
		super(x, y);
	}
	
	@Override
	public void create(Random random) {
		// TODO Auto-generated method stub
	}

	@Override
	public void create(JSONObject state) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onServerTick(IModifiablePlacement placement) {
		// ServerConsole.writeInfo("Just growing some plants at 100% capacity!");
	}

	@Override
	public void onTimeUpdate(IModifiablePlacement placement, Time time) {
		// ServerConsole.writeInfo("Just growing some plants, oh did the time change?");
	}

	@Override
	public ItemType onInteraction(IModifiablePlacement placement, ItemType item) {
		// Is the player trying to plant a seed and there isn't one planted already?
		if (isItemSeed(item) && this.plantedSeed == ItemType.NONE) {
			// We can plant the seed!
			this.plantedSeed = item;
			// Set the overlay that matches the planted seed.
			placement.setOverlay(getUnderlayForSeed(this.plantedSeed));
			// The player no longer has the seed.
			return ItemType.NONE;
			
		}
		// Nothing happened with the item.
		return item;
	}
	
	/**
	 * Gets whether the item is a plantable seed.
	 * @param item The item.
	 * @return Whether the item is a plantable seed.
	 */
	private static boolean isItemSeed(ItemType item) {
		switch (item) {
			case SEED_YELLOW:
			case SEED_RED:
			case SEED_BLUE:
			case SEED_GREEN:
			case SEED_ORANGE:
			case SEED_PURPLE:
				return true;
			default:
				return false;
		}
	}
	
	/**
	 * Get the relevant planted seed overlay for a specifiec seed. 
	 * @param seed The seed.
	 * @return The relevant planted seed overlay for a specifiec seed. 
	 */
	private static PlacementOverlay getUnderlayForSeed(ItemType seed) {
		return PlacementOverlay.valueOf(seed.toString());
	}

	@Override
	public PlacementType getType() {
		return PlacementType.TILLED_EARTH;
	}

	@Override
	public JSONObject getState() {
		// Create the JSON object that will hold the information about this placements state.
		JSONObject state = new JSONObject();
		// Record the number of ticks that the tilled earth is watered for.
		state.put("watered-ticks", this.wateredTicks);
		// Record the planted seed.
		state.put("planted-seed", this.plantedSeed.ordinal());
		// Return the serialised state.
		return state;
	}
}
