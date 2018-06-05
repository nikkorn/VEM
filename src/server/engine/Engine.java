package server.engine;

import server.players.ConnectedPlayerPool;
import server.world.Position;
import server.world.World;
import server.world.chunk.Chunk;

/**
 * The server-side game engine.
 */
public class Engine {
	/**
	 * The game world.
	 */
	private World world;
	/**
	 * The pool of connected players.
	 */
	private ConnectedPlayerPool connectedPlayerPool;
	
	/**
	 * Create a new instance of the Engine class.
	 * @param world The game world.
	 * @param connectedPlayerPool The pool of connected players.
	 */
	public Engine(World world, ConnectedPlayerPool connectedPlayerPool) {
		this.world               = world;
		this.connectedPlayerPool = connectedPlayerPool;
	}
	
	/**
	 * Tick the game engine.
	 */
	public void tick() {
		// Update the world time and get whether it has changed.
		// It does not change every server tick, just ever game minute.
		boolean timeChanged = this.world.getTime().update();
		// Tick each of our cached chunks.
		for (Chunk chunk : world.getCachedChunks()) {
			// Are any players within the vicinity of this chunk?
			boolean arePlayersNearChunk = arePlayersInChunkVicinity(chunk);
			// We only want to tick chunks that are active. An active chunk either:
			// - Contains a high priority placement.
			// - Has any players in the vicinity.
			if (arePlayersNearChunk || chunk.hasHighPriorityPlacement()) {
				chunk.tick(timeChanged, this.world.getTime(), arePlayersNearChunk);
			}
		}
	}
	
	/**
	 * Checks whether any connected players are within the vicinity of the specified chunk.
	 * @param chunk The chunk.
	 * @return Whether any connected players are within the vicinity of the specified chunk.
	 */
	private boolean arePlayersInChunkVicinity(Chunk chunk) {
		// Check the position of each connected player.
		for (Position playerPosition : connectedPlayerPool.getPlayerPositions()) {
			// Is the currently connected player in the vicinity of this chunk?
			if (chunk.isPositionInVicinity(playerPosition)) {
				return true;
			}
		}
		// No connected players were in the vicinity of this chunk.
		return false;
	}
}
