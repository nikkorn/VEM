package gaia.server.world.messaging.messages;

import gaia.Position;
import gaia.server.world.tile.placement.PlacementUnderlay;

/**
 * A message containing the details of a placement underlay change.
 */
public class PlacementUnderlayChangedMessage implements IWorldMessage {
	/**
	 * The position of the placement.
	 */
	private Position position;
	/**
	 * The new placement underlay.
	 */
	private PlacementUnderlay underlay;
	
	/**
	 * Create a new instance of the PlacementUnderlayChangedMessage class.
	 * @param underlay The new placement underlay.
	 * @param position The position of the placement.
	 */
	public PlacementUnderlayChangedMessage(PlacementUnderlay underlay, Position position) {
		this.underlay = underlay;
		this.position = position;
	}
	
	/**
	 * Get the new placement underlay.
	 * @return The new placement underlay.
	 */
	public PlacementUnderlay getUnderlay() {
		return this.underlay;
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
		return WorldMessageType.PLACEMENT_UNDERLAY_CHANGED;
	}
}
