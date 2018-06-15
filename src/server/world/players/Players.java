package server.world.players;

import java.util.ArrayList;
import server.world.Position;
import server.world.World;
import server.world.messaging.messages.PlayerSpawnRejectedMessage;
import server.world.messaging.messages.PlayerSpawnSuccessMessage;

/**
 * The players within a world.
 */
public class Players {
	/**
	 * The list of players.
	 */
	private ArrayList<Player> players;
	
	/**
	 * Add a player.
	 * @param playerId The player id.
	 * @param world The game world.
	 * @return Whether the player was added to the world.
	 */
	public boolean addPlayer(String playerId, World world) {
		// Check to make sure a player with this player id doesn't already exist.
		if (this.isPlayerPresent(playerId)) {
			// Add a world message to notify of the rejection.
			world.getWorldMessageQueue().add(new PlayerSpawnRejectedMessage(playerId, "Player with id " + playerId + " is already present!"));
			// Return false as we didn't add the player.
			return false;
		}
		// Check to make sure a player with this player id is not blacklisted.
		if (this.isPlayerBlacklisted(playerId)) {
			// Add a world message to notify of the rejection.
			world.getWorldMessageQueue().add(new PlayerSpawnRejectedMessage(playerId, "Player with id " + playerId + " is blacklisted!"));
			// Return false as we didn't add the player.
			return false;
		}
		// Get a safe spawn position for the player from the world.
		Position spawn = world.getSafeSpawnPosition();
		// We can add our player at a safe world spawn!
		this.players.add(new Player(playerId, spawn));
		// Add a world message to notify of the success.
		world.getWorldMessageQueue().add(new PlayerSpawnSuccessMessage(playerId, new Position(spawn.getX(), spawn.getY())));
		// We were able to add the player.
		return true;
	}
	
	/**
	 * Get whether the player with the specified player id is present.
	 * @param playerId The player id.
	 * @return Whether the player with the specified player id is present.
	 */
	public boolean isPlayerPresent(String playerId) {
		return this.getPlayer(playerId) != null;
	}
	
	/**
	 * Get whether the player with the specified player id is blacklisted.
	 * @param playerId The player id.
	 * @return Whether the player with the specified player id is blacklisted.
	 */
	public boolean isPlayerBlacklisted(String playerId) {
		// TODO Read this from the server config file.
		return false;
	}
	
	/**
	 * Get the player with the specified player id.
	 * @param playerId The player id.
	 * @return The player with the specified player id.
	 */
	public Player getPlayer(String playerId) {
		for (Player player : this.players) {
			if (player.getPlayerId().equals(playerId)) {
				return player;
			}
		}
		// No player with the specified id was found.
		return null;
	}
	
	/**
	 * Gets the positions of all connected players.
	 * @return The positions of all connected players.
	 */
	public ArrayList<Position> getPlayerPositions() {
		return new ArrayList<Position>();
	}
}
