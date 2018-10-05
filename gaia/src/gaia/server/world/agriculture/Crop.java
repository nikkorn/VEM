package gaia.server.world.agriculture;

import gaia.time.Season;
import gaia.world.PlacementOverlay;
import gaia.world.items.ItemType;

/**
 * Represents the lifecycle of a type of crop.
 */
public abstract class Crop {
	/** 
	 * The state of the crop. 
	 */
	private CropState state = CropState.SEEDED;
	/**
	 * The number of ticks that the crop has been active for.
	 */
	private long numberOfTicksActive = 0;
	/**
	 * The unit of growth for the crop.
	 * This value defines the state of the crop.
	 */
	private long growth = 0;
	/**
	 * The health of a crop, defining how much of an overal decrementy the crop can take in its growth before dying.
	 */
	private long health;
	
	/**
	 * Create an instance of the Crop class.
	 */
	public Crop() {
		// Give the crop it's initial health.
		this.health = this.getInitialHealth();
	}

	/**
	 * Tick the crop and return whether the state of the crop has changed.
	 * @param isWatered Whether the crop is watered.
	 * @param season The current season.
	 * @return Whether the state of the crop has changed.
	 */
	public boolean tick(boolean isWatered, Season season) {
		// Has this crop died of old age?
		if (++numberOfTicksActive == this.getLifeSpan()) {
			// Our crop has died of old age!
			this.state = CropState.DEAD;
			// The state has changed.
			return true;
		}
		// Do nothing if the crop is dead.
		if (this.state == CropState.DEAD) {
			return false;
		}
		// Get the growth modification to make this tick.
		long growthModification = this.getGrowthModification(isWatered, season);
		// If we have negative growth then this will impact the health of the crop.
		if (growthModification < 0) {
			// Apply this negative growth agains the health of the crop.
			this.health += growthModification;
			// Is the crop now dead?
			if (this.health <= 0) {
				// Our crop is now dead!
				this.state = CropState.DEAD;
				// The state has changed.
				return true;
			} else {
				// The state has not changed as we are not growing, but not dead either.
				return false;
			}
		}
		// Update the growth of the crop.
		this.growth += growthModification;
		// Get the old state of the crop.
		CropState stateForCurrentGrowth = this.state;
		// Has this crop sprouted?
		if (this.growth >= this.getRequiredGrowthForSprouting()) {
			stateForCurrentGrowth = CropState.SPROUTED;
		}
		// Has this crop produced anything?
		if (this.growth >= this.getRequiredGrowthForProduce()) {
			stateForCurrentGrowth = CropState.PRODUCE;
		}
		// Get whether the state or the crop has changed as a result of the change in growth.
		if (this.state != stateForCurrentGrowth) {
			// Set the new state of the crop.
			this.state = stateForCurrentGrowth;
			// There has been a change in state.
			return true;
		} else {
			// There has been no change in state.
			return false;
		}
	}
	
	/**
	 * Get the state of the crop.
	 * @return The state of the crop.
	 */
	public CropState getState() {
		return this.state;
	}
	
	/**
	 * Gets the modification to make to the crop growth.
	 * @param isWatered Whether the crop is watered.
	 * @param season The current season.
	 * @return The modification to make to the crop growth.
	 */
	public abstract long getGrowthModification(boolean isWatered, Season season);
	
	/**
	 * Gets the type of seed that will become this crop.
	 * @return The type of seed that will become this crop.
	 */
	public abstract ItemType getSeed();
	
	/**
	 * Gets the type of item that this crop will produce.
	 * @return The type of item that this crop will produce.
	 */
	public abstract ItemType getProduce(); 
	
	/**
	 * Gets the placement overlay to use when the crop is seeded.
	 * @return The placement overlay to use when the crop is seeded.
	 */
	public abstract PlacementOverlay getSeededOverlay();
	
	/**
	 * Gets the placement overlay to use when the crop is sprouted.
	 * @return The placement overlay to use when the crop is sprouted.
	 */
	public abstract PlacementOverlay getSproutedOverlay();
	
	/**
	 * Gets the maximum life span of the crop in ticks.
	 * @return The maximum life span of the crop in ticks.
	 */
	public abstract long getLifeSpan();
	
	/**
	 * Gets the health of the crop.
	 * The health of a crop defines how much of an overal decremetn the crop can take in its growth before dying.
	 * @return The health of the crop.
	 */
	public abstract long getInitialHealth();
	
	/**
	 * Gets the required level of growth for the crop to sprout.
	 * @return The required level of growth for the crop to sprout.
	 */
	public abstract long getRequiredGrowthForSprouting();
	
	/**
	 * Gets the required level of growth for the crop to produce.
	 * @return The required level of growth for the crop to produce.
	 */
	public abstract long getRequiredGrowthForProduce();
}
