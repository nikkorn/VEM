package gaia.server.world.chunk;

import gaia.server.world.placements.Placement;

/**
 * Represents a tile visitor.
 */
public interface ITileVisitor {
	
	/**
	 * Handle a tile visit.
	 * @param x The x position of the tile.
	 * @param y The y position of the tile.
	 * @param placement The placment on this tile, or null if there is no placement.
	 */
	void onVisit(int x, int y, Placement placement);
}
