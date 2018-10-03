package gaia.server.world.placements.types;

import java.util.Random;
import org.json.JSONObject;
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
		// ServerConsole.writeInfo("Just growing some plants, did you use that item on me? I will keep it!");
		return ItemType.NONE;
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
		// Return the serialised state.
		return state;
	}
}
