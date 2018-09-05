package gaia.networking.messages;

import gaia.networking.IMessage;
import gaia.world.PlacementType;
import gaia.world.Position;

/**
 * A message sent to a client notifying them of a placement being removed from the world.
 */
public class PlacementRemoved implements IMessage {
	/**
	 * The position of the placement.
	 */
	private Position position;
	/**
	 * The expected placement type of the placement being removed.
	 */
	private PlacementType expectedPlacementType;

	/**
	 * Create a new instance of the PlacementRemoved class.
	 * @param position The position of the placement.
	 * @param expectedPlacementType The expected type of the placement being removed.
	 */
	public PlacementRemoved(Position position, PlacementType expectedPlacementType) {
		this.position              = position; 
		this.expectedPlacementType = expectedPlacementType;
	}
	
	/**
	 * Get the position of the placement.
	 * @return The position of the placement.
	 */
	public Position getPosition() {
		return this.position;
	}
	
	/**
	 * Get the expected type of the placement being removed.
	 * @return The expected type of the placement being removed.
	 */
	public PlacementType getExpectedPlacementType() {
		return this.expectedPlacementType;
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.PLACEMENT_REMOVED;
	}
}
