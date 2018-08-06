package gaia.networking.messages;

import gaia.networking.IMessage;

/**
 * A message sent to a client notifying them of a placement load when they are nearby.
 */
public class PlacementLoaded implements IMessage {
	/**
	 * The packed x/y world position of the placement.
	 */
	private int packedPosition;
	/**
	 * The packed integer representing:
	 *   - The placement type.
	 *   - The placement underlay.
	 *   - The placement overlay.
	 */
	private int packedComposition;
	
	/**
	 * Create a new instance of the PlacementLoaded class.
	 * @param placement The packed integer representing the placement composition.
	 * @param position The packed integer representing the placement position.
	 */
	public PlacementLoaded(int composition, int position) {
		this.packedPosition    = position;
		this.packedComposition = composition;
	}
	
	/**
	 * Get the packed x/y world position of the placement.
	 * @return The packed x/y world position of the placement.
	 */
	public int getPackedPosition() {
		return this.packedPosition;
	}
	
	/**
	 * Get the packed composition of the placement.
	 * @return The packed composition of the placement.
	 */
	public int getPackedComposition() {
		return this.packedComposition;
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.PLACEMENT_LOADED;
	}
}
