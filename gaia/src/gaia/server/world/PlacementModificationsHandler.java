package gaia.server.world;

import java.util.ArrayList;
import gaia.Constants;
import gaia.server.world.messaging.WorldMessageQueue;
import gaia.server.world.messaging.messages.PlacementChangedMessage;
import gaia.server.world.messaging.messages.PlacementCreatedMessage;
import gaia.server.world.messaging.messages.PlacementRemovedMessage;
import gaia.server.world.placements.IPlacementDetails;
import gaia.server.world.players.Player;
import gaia.server.world.players.Players;
import gaia.world.Position;

/**
 * Handler for placement modifications.
 */
public class PlacementModificationsHandler {
	/**
	 * The collection of connected players.
	 */
	private Players players;
	/**
	 * The world message queue.
	 */
	private WorldMessageQueue worldMessageQueue;
	
	/**
	 * Create a new instance of the PlacementModificationsHandler class.
	 * @param players The collection of connected players.
	 * @param worldMessageQueue The world message queue.
	 */
	public PlacementModificationsHandler(Players players, WorldMessageQueue worldMessageQueue) {
		this.players           = players;
		this.worldMessageQueue = worldMessageQueue;
	}
	
	/**
	 * Called when a placement is created at a position.
	 * @param placement The placement that has been added.
	 * @param position The position of the changed placement.
	 */
	public void onPlacementCreated(IPlacementDetails placement, Position position) {
		// Create a list to hold the ids of any players that care about the placement creation.
		ArrayList<String> concernedPlayerIds = new ArrayList<String>();
		// Get all the players that are close enough to this placement to care about it.
		for (Player player : players.getAllPlayers()) {
			// We do not care about this player if they are not within the view range of the placement.
			if (!position.isWithinDistanceOf(player.getPosition(), Constants.PLAYER_VIEW_DISTANCE)) {
				continue;
			}
			// This player is close enough to the placement to care about it.
			concernedPlayerIds.add(player.getId());
			// Update the player's familiarity with the placement.
			player.getWorldFamiliarity().update(placement, position.getX(), position.getY());
		}
		// Add a world message to notify any concerned players of the placement change.
		if (concernedPlayerIds.size() > 0) {
			worldMessageQueue.add(new PlacementCreatedMessage(concernedPlayerIds, placement, position));
		}
	}
	
	/**
	 * Called when a placement changes at a position.
	 * @param placement The placement that has changed.
	 * @param position The position of the changed placement.
	 */
	public void onPlacementChanged(IPlacementDetails placement, Position position) {
		// Create a list to hold the ids of any players that care about the placement change.
		ArrayList<String> concernedPlayerIds = new ArrayList<String>();
		// Get all the players that are close enough to this placement to care about it.
		for (Player player : players.getAllPlayers()) {
			// We do not care about this player if they are not within the view range of the placement.
			if (!position.isWithinDistanceOf(player.getPosition(), Constants.PLAYER_VIEW_DISTANCE)) {
				continue;
			}
			// This player is close enough to the placement to care about it.
			concernedPlayerIds.add(player.getId());
			// Update the player's familiarity with the placement.
			player.getWorldFamiliarity().update(placement, position.getX(), position.getY());
		}
		// Add a world message to notify any concerned players of the placement change.
		if (concernedPlayerIds.size() > 0) {
			worldMessageQueue.add(new PlacementChangedMessage(concernedPlayerIds, placement, position));
		}
	}
	
	/**
	 * Called when a placement is removed.
	 * @param placement The placement that has been removed.
	 * @param position The position of the removed placement.
	 */
	public void onPlacementRemoved(IPlacementDetails placement, Position position) {
		// Create a list to hold the ids of any players that care about the placement deletion.
		ArrayList<String> concernedPlayerIds = new ArrayList<String>();
		// Get all the players that are close enough to this placement to care about it.
		for (Player player : players.getAllPlayers()) {
			// We do not care about this player if they are not within the view range of the placement.
			if (!position.isWithinDistanceOf(player.getPosition(), Constants.PLAYER_VIEW_DISTANCE)) {
				continue;
			}
			// This player is close enough to the placement to care about it.
			concernedPlayerIds.add(player.getId());
			// Update the player's familiarity with the placement.
			player.getWorldFamiliarity().update(null, position.getX(), position.getY());
		}
		// Add a world message to notify any concerned players of the placement deletion.
		if (concernedPlayerIds.size() > 0) {
			worldMessageQueue.add(new PlacementRemovedMessage(concernedPlayerIds, placement.getType(), position));
		}
	}
}
