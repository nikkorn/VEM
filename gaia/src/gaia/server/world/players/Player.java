package gaia.server.world.players;

import gaia.server.ServerConsole;
import gaia.server.world.items.container.types.Inventory;
import gaia.server.world.players.worldfamiliarity.PlayerWorldFamiliarity;
import gaia.world.Direction;
import gaia.world.Position;
import gaia.world.items.ItemType;

/**
 * Represents a player in the world.
 */
public class Player {
	/**
	 * The player id.
	 */
	private String id;
	/**
	 * The player position.
	 */
	private Position position;
	/**
	 * The direction that the player is facing.
	 */
	private Direction direction = Direction.DOWN;
	/**
	 * The player inventory.
	 */
	private Inventory inventory = new Inventory();
	/**
	 * The player's familiarity with the world.
	 */
	private PlayerWorldFamiliarity worldFamiliarity = new PlayerWorldFamiliarity();

	/**
	 * Create an instance of the Player class.
	 * @param id The player id.
	 * @param position The initial player position.
	 */
	public Player(String id, Position position) {
		this.id       = id;
		this.position = position;
	}
	
	/**
	 * Get the player id.
	 * @return The player id.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Get the player position.
	 * @return The player position.
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Get the facing direction of the player.
	 * @return The facing direction of the player.
	 */
	public Direction getFacingDirection() {
		return this.direction;
	}
	
	/**
	 * Set the facing direction of the player.
	 * @param direction The facing direction of the player.
	 */
	public void setFacingDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * Get the player inventory.
	 * @return The player inventory.
	 */
	public Inventory getInventory() {
		return this.inventory;
	}
	
	/**
	 * Get the player's familiarity with the world.
	 * @return The player's familiarity with the world.
	 */
	public PlayerWorldFamiliarity getWorldFamiliarity() {
		return this.worldFamiliarity;
	}
	
	/**
	 * Called when an item is used on the player.
	 * @return The modification made to the item type in its use.
	 */
	public ItemType onItemUse(ItemType item) {
		// TODO Handle this properly.
		ServerConsole.writeDebug("The item " + item.toString() + " was used on player '" + this.id + "'!");
		// Return the modified item type.
		return item;
	}
}
