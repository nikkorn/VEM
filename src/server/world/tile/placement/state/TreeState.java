package server.world.tile.placement.state;

import org.json.JSONObject;
import lotto.Lotto;
import server.items.ItemType;

/**
 * Represents state of a plain tree.
 */
public class TreeState implements IPlacementState {
	/*
	 * The lotto to use in determining whether to produce wood.
	 */
	public Lotto<ItemType> woodProductionLotto;
	
	/**
	 * Create a new instance of the TreeState class.
	 */
	public TreeState() {
		// Create the lotto to use in determining whether to produce wood.
		woodProductionLotto = new Lotto<ItemType>()
				.add(ItemType.WOOD)
				.add(ItemType.NONE, 100);
	}

	@Override
	public JSONObject asJSON() {
		return null;
	}
}
