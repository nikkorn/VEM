package gaia.server.world.players;

/**
 * Enumeration of the results of a player attempting to join.
 */
public enum PlayerJoinRequestResult {
	SUCCESS,
	BLACKLISTED,
	ALREADY_JOINED
}
