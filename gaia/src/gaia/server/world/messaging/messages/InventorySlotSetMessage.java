package gaia.server.world.messaging.messages;

import gaia.server.engine.IWorldEventsHandler;
import gaia.world.items.ItemType;

/**
 * A message containing the details of a set inventory slot.
 */
public class InventorySlotSetMessage implements IWorldMessage {
	/**
	 * The id of the player who has the inventory.
	 */
	private String playerId;
	/**
	 * The item type.
	 */
	private ItemType itemType;
	/**
	 * The inventory slot index.
	 */
	private int slotIndex;
	
	/**
	 * Create a new instance of the InventorySlotSetMessage class.
	 * @param playerId The id of the player who has the inventory.
	 * @param itemType The item type.
	 * @param slotIndex The inventory slot index.
	 */
	public InventorySlotSetMessage(String playerId, ItemType itemType, int slotIndex) {
		this.playerId  = playerId;
		this.itemType  = itemType;
		this.slotIndex = slotIndex;
	}
	
	/**
	 * Get the id of the spawning player.
	 * @return The id of the spawning player.
	 */
	public String getPlayerId() {
		return playerId;
	}

	/**
	 * Get the item type.
	 * @return The item type.
	 */
	public ItemType getItemType() {
		return this.itemType;
	}
	
	/**
	 * Get the inventory slot index.
	 * @return The inventory slot index.
	 */
	public int getSlotIndex() {
		return this.slotIndex;
	}
	
	@Override
	public void process(IWorldEventsHandler handler) {
		handler.onPlayerInventoryChange(playerId, itemType, slotIndex);
	}
}
