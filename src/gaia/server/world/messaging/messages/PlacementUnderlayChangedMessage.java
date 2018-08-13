package gaia.server.world.messaging.messages;

import gaia.world.PlacementUnderlay;
import gaia.world.Position;

/**
 * A message containing the details of a placements underlay change.
 */
public class PlacementUnderlayChangedMessage implements IWorldMessage {
	/**
	 * The position of the placements.
	 */
	private Position position;
	/**
	 * The new placements underlay.
	 */
	private PlacementUnderlay underlay;
	
	/**
	 * Create a new instance of the PlacementUnderlayChangedMessage class.
	 * @param underlay The new placements underlay.
	 * @param position The position of the placements.
	 */
	public PlacementUnderlayChangedMessage(PlacementUnderlay underlay, Position position) {
		this.underlay = underlay;
		this.position = position;
	}
	
	/**
	 * Get the new placements underlay.
	 * @return The new placements underlay.
	 */
	public PlacementUnderlay getUnderlay() {
		return this.underlay;
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
		return WorldMessageType.PLACEMENT_UNDERLAY_CHANGED;
	}
}
