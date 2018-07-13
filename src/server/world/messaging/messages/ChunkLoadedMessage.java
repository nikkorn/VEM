package server.world.messaging.messages;

import server.world.chunk.IChunkDetails;

/**
 * A message containing the details of a loaded chunk.
 * This message will be sent every time a player moves/spawns into the vicinity
 * of a chunk that they have not visited yet. 
 */
public class ChunkLoadedMessage implements IWorldMessage {
	/**
	 * The id of the player the chunk has been loaded for.
	 */
	private String playerId;
	/**
	 * The details of the loaded chunk.
	 */
	private IChunkDetails chunkDetails;
	
	/**
	 * Create a new instance of the ChunkLoadedMessage class.
	 * @param playerId The id of the player the chunk has been loaded for.
	 * @param chunkDetails The details of the loaded chunk.
	 */
	public ChunkLoadedMessage(String playerId, IChunkDetails chunkDetails) {
		this.playerId      = playerId;
		this.chunkDetails = chunkDetails;
	}
	
	/**
	 * Get the id of the player the chunk has been loaded for.
	 * @return The id of the player the chunk has been loaded for.
	 */
	public String getPlayerId() {
		return playerId;
	}

	/**
	 * Get the details of the loaded chunk.
	 * @return The details of the loaded chunk.
	 */
	public IChunkDetails getChunkDetails() {
		return this.chunkDetails;
	}

	@Override
	public WorldMessageType getMessageType() {
		return WorldMessageType.CHUNK_LOADED;
	}
}
