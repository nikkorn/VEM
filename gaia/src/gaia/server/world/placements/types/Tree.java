package gaia.server.world.placements.types;

import java.util.Random;
import org.json.JSONObject;
import gaia.lotto.Lotto;
import gaia.server.world.items.container.Container;
import gaia.server.world.items.container.NoFreeSlotException;
import gaia.server.world.placements.IModifiablePlacement;
import gaia.server.world.placements.Placement;
import gaia.time.Time;
import gaia.world.PlacementType;
import gaia.world.PlacementUnderlay;
import gaia.world.items.ItemType;

/**
 * A 'Tree' placement.
 */
public class Tree extends Placement {
	/*
	 * The lotto to use in determining whether to produce wood.
	 */
	public Lotto<ItemType> woodProductionLotto;

	/**
	 * Create a new instance of the Tree class.
	 * @param x The placements x position within its parent chunk.
	 * @param y The placements y position within its parent chunk.
	 */
	public Tree(short x, short y) {
		super(x, y);
		
		// Create the lotto to use in determining whether to produce wood.
		woodProductionLotto = new Lotto<ItemType>()
				.add(ItemType.WOOD)
				.add(ItemType.NONE, 100);
	}
	
	@Override
	public void create(Random random) {}

	@Override
	public void create(JSONObject state) {}

	@Override
	public void onServerTick(IModifiablePlacement placement) {
		// Get the placements container.
		Container container = placement.getContainer();
		// We may be producing some wood! But only if the placements container is not full.
		if (container.isFull()) {
			return;
		}
		// Our tree state holds a lotto that is used to determine whether to produce some wood.
		ItemType produced = woodProductionLotto.draw();
		// If we were able to produce some wood then put it in the container of this tree placements.
		if (produced != ItemType.NONE) {
			try {
				container.add(produced);
			} catch (NoFreeSlotException e) {
				// We have no free slot for our wood, this is fine.
			}
		}
	}

	@Override
	public void onTimeUpdate(IModifiablePlacement placement, Time time) {}

	@Override
	public ItemType onInteraction(IModifiablePlacement placement, ItemType item) {
		// TODO Remove this! You cannot chop down a tree with a hoe!
		if (item == ItemType.HOE) {
			// Set the underlay of the placement to NONE so that it seems we chopped it down.
			placement.setUnderlay(PlacementUnderlay.NONE);
			// Give the player some wood in exchange for their nice hoe.
			return ItemType.WOOD;
		}
		// Using any other item does nothing.
		return item;
	}

	@Override
	public PlacementType getType() {
		return PlacementType.TREE;
	}

	@Override
	public JSONObject getState() {
		// TODO Auto-generated method stub
		return null;
	}
}
