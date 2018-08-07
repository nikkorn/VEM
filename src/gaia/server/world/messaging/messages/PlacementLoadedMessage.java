package gaia.server.world.messaging.messages;

import gaia.server.world.tile.placement.IPlacementDetails;

/**
 * A message containing the details of a placement load.
 */
public class PlacementLoadedMessage implements IWorldMessage {
	/**
	 * The placement details.
	 */
	private IPlacementDetails placementDetails;
	/**
	 * The id of the player that instigated the load.
	 */
	private String playerId;
	/**
	 * The absolute x/y position of the position. 
	 */
	private short x, y;
	
	/**
	 * Create a new instance of the PlacementLoadedMessage class.
	 * @param placementDetails The details of the loaded placement.
	 * @param x The absolute x position of the placement.
	 * @param y The absolute y position of the placement.
	 * @param playerId The id of the player that instigated the load.
	 */
	public PlacementLoadedMessage(IPlacementDetails placementDetails, short x, short y, String playerId) {
		this.placementDetails = placementDetails;
		this.x                = x;
		this.y                = y;
		this.playerId         = playerId;
	}
	
	/**
	 * Get the details of the loaded placement.
	 * @return The details of the loaded placement.
	 */
	public IPlacementDetails getPlacementDetails() {
		return this.placementDetails;
	}
	
	/**
	 * Get the absolute x placement position.
	 * @return The absolute x placement position.
	 */
	public short getX() {
		return this.x;
	}
	
	/**
	 * Get the absolute y placement position.
	 * @return The absolute y placement position.
	 */
	public short getY() {
		return this.y;
	}
	
	/**
	 * Get the id of the player that instigated the load.
	 * @return The id of the player that instigated the load.
	 */
	public String getInstigatingPlayerId() {
		return this.playerId;
	}

	@Override
	public WorldMessageType getMessageType() {
		return WorldMessageType.PLACEMENT_LOADED;
	}
}
