package gaia.server.world.messaging.messages;

import gaia.world.PlacementOverlay;
import gaia.world.Position;

/**
 * A message containing the details of a placements overlay change.
 */
public class PlacementOverlayChangedMessage implements IWorldMessage {
	/**
	 * The position of the placements.
	 */
	private Position position;
	/**
	 * The new placements overlay.
	 */
	private PlacementOverlay overlay;
	
	/**
	 * Create a new instance of the PlacementOverlayChangedMessage class.
	 * @param overlay The new placements overlay.
	 * @param position The position of the placements.
	 */
	public PlacementOverlayChangedMessage(PlacementOverlay overlay, Position position) {
		this.overlay  = overlay;
		this.position = position;
	}
	
	/**
	 * Get the new placements overlay.
	 * @return The new placements overlay.
	 */
	public PlacementOverlay getOverlay() {
		return this.overlay;
	}

	/**
	 * Get the position of the placements.
	 * @return The position of the placements.
	 */
	public Position getPosition() {
		return position;
	}

	@Override
	public WorldMessageType getMessageType() {
		return WorldMessageType.PLACEMENT_OVERLAY_CHANGED;
	}
}
