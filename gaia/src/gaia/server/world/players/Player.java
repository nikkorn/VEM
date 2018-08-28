package gaia.server.world.players;

import java.util.ArrayList;
import gaia.server.ServerConsole;
import gaia.server.world.chunk.Chunk;
import gaia.world.Direction;
import gaia.world.Position;
import gaia.world.items.Inventory;
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
	 * The list of id's of chunks that this player has visited (been in the vicinity of).
	 */
	private ArrayList<String> chunksVisited = new ArrayList<String>();

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
	public String getPlayerId() {
		return this.id;
	}

	/**
	 * Get the player position.
	 * @return The player position.
	 */
	public Position getPositon() {
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
	 * Add a chunk that the player has visited.
	 * Keeping track of chunk visitors helps us determine who has been here.
	 * @param chunk The visited chunk.
	 */
	public void addVisitedChunk(Chunk chunk) {
		this.chunksVisited.add(chunk.getKey());
	}
	
	/**
	 * Get whether this player has visited the specified chunk before.
	 * @param chunk The chunk.
	 * @return Whether this player has visited the specified chunk before.
	 */
	public boolean hasVisitedChunk(Chunk chunk) {
		return this.chunksVisited.contains(chunk.getKey());
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
