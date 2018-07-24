package gaia.server.world.players;

import java.util.ArrayList;
import gaia.server.world.Direction;
import gaia.server.world.Position;
import gaia.server.world.World;
import gaia.server.world.messaging.messages.PlayerPositionChangedMessage;
import gaia.server.world.messaging.messages.PlayerSpawnedMessage;

/**
 * The players within a world.
 */
public class Players {
	/**
	 * The list of players.
	 */
	private ArrayList<Player> players = new ArrayList<Player>();
	
	/**
	 * Attempt to add a player.
	 * @param playerId The player id.
	 * @param world The game world.
	 * @return The result of attempting to add the player.
	 */
	public PlayerJoinRequestResult addPlayer(String playerId, World world) {
		// Check to make sure a player with this player id doesn't already exist.
		if (this.isPlayerPresent(playerId)) {
			return PlayerJoinRequestResult.ALREADY_JOINED;
		}
		// Check to make sure a player with this player id is not blacklisted.
		if (this.isPlayerBlacklisted(playerId)) {
			return PlayerJoinRequestResult.BLACKLISTED;
		}
		// Create the new player and place them at the world spawn.
		Player player = new Player(playerId, world.getPlayerSpawn());
		// We can add our player to the list of active players.
		this.players.add(player);
		// Add a world message to notify of the success the player had in joining.
		world.getWorldMessageQueue().add(new PlayerSpawnedMessage(playerId, new Position(player.getPositon().getX(), player.getPositon().getY())));
		// As the player will be spawning into the world, we regard this as a chunk change.
		world.getChunks().onPlayerChunkChange(player, world.getWorldMessageQueue());
		// We were able to add the player.
		return PlayerJoinRequestResult.SUCCESS;
	}
	
	/**
	 * Attempt to move a player in a direction.
	 * @param playerId The player id.
	 * @param direction The direction to move in.
	 * @param world The game world.
	 */
	public void movePlayer(String playerId, Direction direction, World world) {
		// Check to make sure a player with this player id already exists.
		if (!this.isPlayerPresent(playerId)) {
			// We didn't find the player.
			return;
		}
		// Get the target player.
		Player targetPlayer = this.getPlayer(playerId);
		// Find the position the player is trying to move to based on their current position.
		int newPositionX = targetPlayer.getPositon().getX();
		int newPositionY = targetPlayer.getPositon().getY();
		switch(direction) {
			case DOWN:
				newPositionY-=1;
				break;
			case LEFT:
				newPositionX-=1;
				break;
			case RIGHT:
				newPositionX+=1;
				break;
			case UP:
				newPositionY+=1;
				break;
		}
		// Is the position we are trying to move to in the world a valid one?
		if (!world.isPositionWalkable(new Position(newPositionX, newPositionY))) {
			// We cannot walk on this position.
			return;
		}
		// Lastly, check to make sure that no other players are already at this new position.
		for(Position playerPosition : this.getPlayerPositions()) {
			if (playerPosition.getX() == newPositionX && playerPosition.getY() == newPositionY) {
				// There is already a player at this position.
				return;
			}
		}
		// Get the x/y position of the chunk that the player was in before the move.
		int oldChunkXPosition = targetPlayer.getPositon().getChunkX();
		int oldChunkYPosition = targetPlayer.getPositon().getChunkY();
		// The player can move to this new position.
		targetPlayer.getPositon().setX(newPositionX);
		targetPlayer.getPositon().setY(newPositionY);
		// Add a world message to notify of the success.
		world.getWorldMessageQueue().add(new PlayerPositionChangedMessage(playerId, new Position(newPositionX, newPositionY)));
		// Has the player has moved into a different chunk?
		if (oldChunkXPosition != targetPlayer.getPositon().getChunkX() || oldChunkYPosition != targetPlayer.getPositon().getChunkY()) {
			// The player has moved into a different chunk.
			world.getChunks().onPlayerChunkChange(targetPlayer, world.getWorldMessageQueue());
		}
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
