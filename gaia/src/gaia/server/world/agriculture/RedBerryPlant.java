package gaia.server.world.agriculture;

import gaia.time.Season;
import gaia.world.PlacementOverlay;
import gaia.world.items.ItemType;

/**
 * A crop that produces RedBerries.
 */
public class RedBerryPlant extends Crop {

	@Override
	public long getGrowthModification(boolean isWatered, Season season) {
		// For now, RedBerry plants are the most resilient little buggers.
		return 5;
	}

	@Override
	public ItemType getSeed() {
		return ItemType.SEED_RED;
	}

	@Override
	public ItemType getProduce() {
		return ItemType.REDBERRY;
	}

	@Override
	public PlacementOverlay getSeededOverlay() {
		return PlacementOverlay.SEED_RED;
	}

	@Override
	public PlacementOverlay getSproutedOverlay() {
		return PlacementOverlay.SAPLING_RED;
	}
	
	@Override
	public PlacementOverlay getProducedOverlay() {
		return PlacementOverlay.GROWN_REDBERRY;
	}
	
	@Override
	public PlacementOverlay getDeadOverlay() {
		return PlacementOverlay.SAPLING_DEAD;
	}

	@Override
	public long getLifeSpan() {
		return 150l;
	}

	@Override
	public long getInitialHealth() {
		return 100;
	}

	@Override
	public long getRequiredGrowthForSprouting() {
		return 100;
	}

	@Override
	public long getRequiredGrowthForProduce() {
		return 200;
	}

}
