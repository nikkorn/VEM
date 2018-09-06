package gaia.networking.messages;

import gaia.world.Position;

/**
 * A message sent to a client notifying them of a placement being created.
 */
public class PlacementCreated extends PlacementUpdated {

	/**
	 * Create a new instance of the PlacementCreated class.
	 * @param position The position of the placement.
	 * @param packedComposition The packed composition of the placement.
	 */
	public PlacementCreated(Position position, int packedComposition) {
		super(position, packedComposition);
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.PLACEMENT_CREATED;
	}
}
