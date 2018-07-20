package gaia.server.world.chunk;

import gaia.server.world.tile.TileType;
import gaia.server.world.tile.placement.Placement;

/**
 * Exposes details of a loaded chunk.
 */
public interface IChunkDetails {
	
	/**
	 * Get the x position of the chunk.
	 * @return The x position of the chunk.
	 */
	int getX();
	
	/**
	 * Get the y position of the chunk.
	 * @return The y position of the chunk.
	 */
	int getY();
	
	/**
	 * Get the tiles array.
	 * @return The tiles array.
	 */
	TileType[][] getTiles();
	
	/**
	 * Get the placements array.
	 * @return The placements array.
	 */
	Placement[][] getPlacements();
}
