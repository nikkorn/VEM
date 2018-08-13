package gaia.client.gamestate;

import gaia.utils.BitPacker;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementType;
import gaia.world.PlacementUnderlay;

/**
 * A client-side representation of a tile-positioned world placements.
 */
public class Placement {
	/**
	 * The placements type.
	 */
	private PlacementType type;
	/**
	 * The placements underlay.
	 */
	private PlacementUnderlay underlay;
	/**
	 * The placements overlay.
	 */
	private PlacementOverlay overlay;
	
	/**
	 * Create a new instance of the Placement class.
	 * @param type The placements type.
	 * @param underlay The placements underlay.
	 * @param overlay The placements overlay.
	 */
	public Placement(PlacementType type, PlacementUnderlay underlay, PlacementOverlay overlay) {
		this.type     = type;
		this.underlay = underlay;
		this.overlay  = overlay;
	}
	
	/**
	 * Get the placements type.
	 * @return The placements type.
	 */
	public PlacementType getType() {
		return this.type;
	}
	
	/**
	 * Get the placements underlay.
	 * @return The placements underlay.
	 */
	public PlacementUnderlay getUnderlay() {
		return this.underlay;
	}
	
	/**
	 * Set the placements underlay.
	 * @param underlay The placements underlay.
	 */
	public void setUnderlay(PlacementUnderlay underlay) {
		this.underlay = underlay;
	}
	
	/**
	 * Get the placements overlay.
	 * @return The placements overlay.
	 */
	public PlacementOverlay getOverlay() {
		return this.overlay;
	}
	
	/**
	 * Set the placements overlay.
	 * @param overlay The placements overlay.
	 */
	public void setOverlay(PlacementOverlay overlay) {
		this.overlay = overlay;
	}
	
	/**
	 * Create a placements based on information parsed from a packed integer.
	 * @param packed The packed integer.
	 * @return The placements.
	 */
	public static Placement fromPackedInt(int packed) {
		PlacementUnderlay underlay = PlacementUnderlay.values()[BitPacker.unpack(packed, 0, 10)];
		PlacementOverlay overlay   = PlacementOverlay.values()[BitPacker.unpack(packed, 10, 10)];
		PlacementType type         = PlacementType.values()[BitPacker.unpack(packed, 20, 10)];
		return new Placement(type, underlay, overlay);
	}
}
