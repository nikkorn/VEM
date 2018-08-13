package gaia.server.world.messaging.messages;

import java.util.Collection;
import gaia.server.world.placements.IPlacementDetails;

/**
 * A message containing the details of a chunk load.
 */
public class ChunkLoadedMessage implements IWorldMessage {
	/**
	 * The placements in the loaded chunk.
	 */
	private Collection<? extends IPlacementDetails> placements;
	/**
	 * The id of the player that instigated the load.
	 */
	private String playerId;
	/**
	 * The x/y position of the chunk. 
	 */
	private short x, y;
	
	/**
	 * Create a new instance of the ChunkLoadedMessage class.
	 * @param placementDetails The placements details of the loaded chunk.
	 * @param x The x position of the chunk.
	 * @param y The y position of the chunk.
	 * @param playerId The id of the player that instigated the load.
	 */
	public ChunkLoadedMessage(Collection<? extends IPlacementDetails> placements, short x, short y, String playerId) {
		this.placements = placements;
		this.x          = x;
		this.y          = y;
		this.playerId   = playerId;
	}
	
	/**
	 * Get the placements details of the loaded chunk.
	 * @return The placements details of the loaded chunk.
	 */
	public Collection<? extends IPlacementDetails> getPlacements() {
		return this.placements;
	}
	
	/**
	 * Get the x chunk position.
	 * @return The x chunk position.
	 */
	public short getX() {
		return this.x;
	}
	
	/**
	 * Get the y chunk position.
	 * @return The y chunk position.
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
		return WorldMessageType.CHUNK_LOADED;
	}
}
