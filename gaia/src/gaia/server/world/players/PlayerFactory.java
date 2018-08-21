package gaia.server.world.players;

import java.io.File;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import gaia.server.Helpers;
import gaia.server.ServerConsole;
import gaia.world.Position;
import gaia.world.items.ItemType;

/**
 * Factory for creating Player instances.
 */
public class PlayerFactory {
	/**
	 * The players that have details saved to disk.
	 */
	private HashMap<String, Player> persistedPlayers = new HashMap<String, Player>();
	
	/**
	 * Create a new instance of the PlayerFactory class.
	 */
	public PlayerFactory() {}
	
	/**
	 * Create a new instance of the PlayerFactory class.
	 * @param playerDirectoryPath The player directory path.
	 */
	public PlayerFactory(String playerDirectoryPath) {
		// Grab a handle to the player saved state directory.
		File playerDirectory = new File(playerDirectoryPath);
		// Is the path valid?
		if ((!playerDirectory.exists()) || (!playerDirectory.isDirectory())) {
			ServerConsole.writeError("The player saved state directory cannot be found");
			System.exit(0);
		}
		// Populate the map of players that have had their details saved to disk.
		this.populatePersistedPlayers(playerDirectory);
	}
	
	/**
	 * Create a player.
	 * @param playerId The player id.
	 * @param position The player position.
	 * @return The created player.
	 */
	public Player create(String playerId, Position position) {
		// Check whether this player has had their details saved before.
		if (persistedPlayers.containsKey(playerId)) {
			// The player with this id has joined this world before.
			return this.persistedPlayers.get(playerId);
		} else {
			// We are creating a new player.
			return new Player(playerId, position);
		}
	}
	
	/**
	 * Populate the map of players that have had their details saved to disk.
	 */
	private void populatePersistedPlayers(File playerDirectory) {
		// Each file in the player directory represents an individual player.
		for (File playerFile : playerDirectory.listFiles()) {
			// Convert the saved player details to JSON.
			JSONObject playerDetails = Helpers.readJSONObjectFromFile(playerFile);
			// Get the player id.
			String playerId = playerDetails.getString("id");
			// Get the player position.
			Position position = new Position((short)playerDetails.getInt("x"), (short)playerDetails.getInt("y"));
			// Create the player.
			Player player = new Player(playerId, position);
			// Add any player items to their inventory.
			JSONArray inventoryArray = playerDetails.getJSONArray("inventory");
			for (int slotIndex = 0; slotIndex < inventoryArray.length(); slotIndex++) {
				// Get the item at the current slot.
				ItemType itemType = ItemType.values()[inventoryArray.getInt(slotIndex)];
				// We do not care about the NONE item type, this is essentially an empty slot.
				if (itemType == ItemType.NONE) {
					continue;
				}
				// The player had an item in this slot!
				player.getInventory().set(itemType, slotIndex);
			}
			// Add the player to the list of persisted players.
			this.persistedPlayers.put(playerId, player);
		}
	}
}
