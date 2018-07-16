package server.world.players;

/**
 * An interface exposing handlers for player events.
 */
public interface IPlayerEventHandler {
	
	/**
	 * Called when a player has changed chunks.
	 * This can happen in response to:
	 *    - A player spawning into the chunk.
	 *    - A player walking into a chunk.
	 * @param player The player that has move into the chunk
	 */
	void onChunkChange(Player player);
}
