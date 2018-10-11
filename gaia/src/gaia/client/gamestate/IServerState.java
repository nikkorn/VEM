package gaia.client.gamestate;

import gaia.client.gamestate.players.IPlayersDetails;

/**
 * Details of the state of connected server.
 */
public interface IServerState {
	
	/**
	 * Get the details of all the players.
	 * @return The details of all the players.
	 */
	IPlayersDetails getPlayersDetails();
	
	/**
	 * Get the tiles.
	 * @return The tiles.
	 */
	Tiles getTiles();
	
	/**
	 * Get the details of all the placements.
	 * @return The the details of all placements.
	 */
	IPlacements getPlacements();
	
	/**
	 * Get the details of all the containers.
	 * @return The the details of all containers.
	 */
	IContainers getContainers();
	
	/**
	 * Get the world seed.
	 * @return the world seed.
	 */
	long getWorldSeed();
    
    /**
     * Gets whether this server state snapshot is stale.
     * @return Whether this server state snapshot is stale.
     */
    boolean isStale();
    
    /**
     * Refresh this state.
     */
    public void refresh();
}
