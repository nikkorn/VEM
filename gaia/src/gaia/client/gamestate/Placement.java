package gaia.client.gamestate;

import gaia.utils.BitPacker;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementType;
import gaia.world.PlacementUnderlay;

/**
 * A client-side representation of a tile-positioned world placement.
 */
public class Placement implements IPlacementDetails {
	/**
	 * The placement type.
	 */
	private PlacementType type;
	/**
	 * The placement underlay.
	 */
	private PlacementUnderlay underlay;
	/**
	 * The placement overlay.
	 */
	private PlacementOverlay overlay;
	
	/**
	 * Create a new instance of the Placement class.
	 * @param type The placement type.
	 * @param underlay The placement underlay.
	 * @param overlay The placement overlay.
	 */
	public Placement(PlacementType type, PlacementUnderlay underlay, PlacementOverlay overlay) {
		this.type     = type;
		this.underlay = underlay;
		this.overlay  = overlay;
	}
	
	/**
	 * Get the placement type.
	 * @return The placement type.
	 */
	@Override
	public PlacementType getType() {
		return this.type;
	}
	
	/**
	 * Get the placement underlay.
	 * @return The placement underlay.
	 */
	@Override
	public PlacementUnderlay getUnderlay() {
		return this.underlay;
	}
	
	/**
	 * Set the placement underlay.
	 * @param underlay The placement underlay.
	 */
	public void setUnderlay(PlacementUnderlay underlay) {
		this.underlay = underlay;
	}
	
	/**
	 * Get the placement overlay.
	 * @return The placement overlay.
	 */
	@Override
	public PlacementOverlay getOverlay() {
		return this.overlay;
	}
	
	/**
	 * Set the placement overlay.
	 * @param overlay The placement overlay.
	 */
	public void setOverlay(PlacementOverlay overlay) {
		this.overlay = overlay;
	}
	
	/**
	 * Create a placement based on information parsed from a packed integer.
	 * @param packed The packed integer.
	 * @return The placement.
	 */
	public static Placement fromPackedInt(int packed) {
		PlacementUnderlay underlay = PlacementUnderlay.values()[BitPacker.unpack(packed, 0, 10)];
		PlacementOverlay overlay   = PlacementOverlay.values()[BitPacker.unpack(packed, 10, 10)];
		PlacementType type         = PlacementType.values()[BitPacker.unpack(packed, 20, 10)];
		return new Placement(type, underlay, overlay);
	}
}
