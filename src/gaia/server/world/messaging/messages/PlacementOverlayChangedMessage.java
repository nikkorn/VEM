package gaia.server.world.messaging.messages;

import gaia.server.world.Position;
import gaia.server.world.tile.placement.PlacementOverlay;

/**
 * A message containing the details of a placement overlay change.
 */
public class PlacementOverlayChangedMessage implements IWorldMessage {
	/**
	 * The position of the placement.
	 */
	private Position position;
	/**
	 * The new placement overlay.
	 */
	private PlacementOverlay overlay;
	
	/**
	 * Create a new instance of the PlacementOverlayChangedMessage class.
	 * @param overlay The new placement overlay.
	 * @param position The position of the placement.
	 */
	public PlacementOverlayChangedMessage(PlacementOverlay overlay, Position position) {
		this.overlay  = overlay;
		this.position = position;
	}
	
	/**
	 * Get the new placement overlay.
	 * @return The new placement overlay.
	 */
	public PlacementOverlay getOverlay() {
		return this.overlay;
	}

	/**
	 * Get the position of the placement.
	 * @return The position of the placement.
	 */
	public Position getPosition() {
		return position;
	}

	@Override
	public WorldMessageType getMessageType() {
		return WorldMessageType.PLACEMENT_OVERLAY_CHANGED;
	}
}
