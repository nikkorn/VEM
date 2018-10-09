package gaia.server.world.placements.types;

import java.util.Random;
import org.json.JSONObject;
import gaia.server.world.agriculture.Crop;
import gaia.server.world.agriculture.CropState;
import gaia.server.world.agriculture.RedBerryPlant;
import gaia.server.world.placements.IModifiablePlacement;
import gaia.server.world.placements.Placement;
import gaia.time.Time;
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
	 * The crop that is planted.
	 */
	private Crop plantedCrop = null;

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
		// Do we have a crop planted?
		if (this.plantedCrop != null) {
			// Is the crop dead?
			if (this.plantedCrop.getState() == CropState.DEAD) {
				// If the crop is dead then set the placement overlay.
				placement.setOverlay(this.plantedCrop.getDeadOverlay());
				return;
			}
			// We have a living crop! Tick it and check whether the state has changed
			boolean cropStateChanged = this.plantedCrop.tick(wateredTicks > 0, time.getSeason());
			// Handle state changes!
			if (cropStateChanged) {
				// Has the crop produced anything?
				if (this.plantedCrop.getState() == CropState.PRODUCE) {
					// TODO Create container with produce if we have produced anything.
					System.out.println("We have produced a: " + this.plantedCrop.getProduce());
				}
				// Update the placement overlay to reflect the change of crop state.
				placement.setOverlay(this.plantedCrop.getCurrentOverlay());
			}
		}
	}

	@Override
	public ItemType onInteraction(IModifiablePlacement placement, ItemType item) {
		// Is the player trying to plant a seed and there isn't one planted already?
		if (isItemSeed(item) && this.plantedCrop == null) {
			// We can plant the seed! Get the crop for it.
			this.plantedCrop = getCropForSeed(item);
			// Set the overlay that matches the crops planted seed.
			placement.setOverlay(this.plantedCrop.getSeededOverlay());
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
	 * Get the relevant crop for a specific seed. 
	 * @param seed The seed.
	 * @return The relevant crop for a specific seed. 
	 */
	private static Crop getCropForSeed(ItemType seed) {
		switch (seed) {
			case SEED_RED:
				return new RedBerryPlant();
			default:
				return null;
		}
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
		// Record the planted crop.
		state.put("planted-crop", this.plantedCrop.serialise());
		// Return the serialised state.
		return state;
	}
}
