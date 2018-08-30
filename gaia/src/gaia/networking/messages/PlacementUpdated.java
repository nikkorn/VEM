package gaia.networking.messages;

import gaia.networking.IMessage;
import gaia.world.Position;

/**
 * A message sent to a client notifying them of a placement update.
 */
public class PlacementUpdated implements IMessage {
	/**
	 * The position of the placement.
	 */
	private Position position;
	/**
	 * The packed integer representing:
	 *   - The placement type.
	 *   - The placement underlay.
	 *   - The placement overlay.
	 */
	private int packedComposition;

	/**
	 * Create a new instance of the PlacementUpdated class.
	 * @param position The position of the placement.
	 * @param packedComposition The packed composition of the placement.
	 */
	public PlacementUpdated(Position position, int packedComposition) {
		this.position          = position; 
		this.packedComposition = packedComposition;
	}
	
	/**
	 * Get the position of the placement.
	 * @return The position of the placement.
	 */
	public Position getPosition() {
		return this.position;
	}
	
	/**
	 * Get the packed composition of the placement, representing:
	 *   - The placement type.
	 *   - The placement underlay.
	 *   - The placement overlay.
	 * @return The packed composition of the placement.
	 */
	public int getComposition() {
		return this.packedComposition;
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.PLACEMENT_UPDATED;
	}
}
