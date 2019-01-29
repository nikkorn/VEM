package gaia.server.world.players.requests;

import gaia.server.world.World;
import gaia.server.world.messaging.messages.InventorySlotSetMessage;
import gaia.server.world.players.Player;
import gaia.world.items.ItemType;
import gaia.world.players.PositionedPlayer;

/**
 * A request to use an inventory item.
 */
public class UseItemRequest extends PlayerRequest {
	/**
	 * The index of the inventory slot containing the item the player wishes to use.
	 */
	private int slotIndex;
	/**
	 * The item type that was present in the inventory slot at the time of making the request.
	 */
	private ItemType expected;

	/**
	 * Create a new instance of the MoveRequest class.
	 * @param playerId The id of the player requesting to use the item. 
	 * @param slotIndex The index of the inventory slot containing the item the player wishes to use.
	 * @param expected The item type that was present in the inventory slot at the time of making the request.
	 */
	public UseItemRequest(String playerId, int slotIndex, ItemType expected) {
		super(playerId);
		this.slotIndex = slotIndex;
		this.expected  = expected;
	}

	@Override
	public void satisfy(World world) {
		// Get the player.
		Player player = world.getPlayers().getPlayer(this.getRequestingPlayerId());
		// Do nothing if the player was not found.
		if (player == null) {
			return;
		}
		// Get the item from the player inventory at the specified slot.
		ItemType item = player.getInventory().get(slotIndex);
		// We do not want to do anything if this item was not the one we expected.
		// If it does not match then it means that this slot has changed since the
		// client made the request to use the item.
		if (item != this.expected) {
			return;
		}
		// Keep track of any modifications made to the item when we use it.
		ItemType modification = item; 
		// What we do with this item depends on its target.
		switch(item.getTarget()) {
			case FACING_TILE:
				// We are targeting the position that the player is currently facing.
				modification = world.useItem(item, PositionedPlayer.getFacingPosition(player.getPosition(), player.getFacingDirection()));
				break;
			case PLAYER:
				// Use the item on the player directly.
				modification = player.onItemUse(item);
				break;
			default:
				// We cant do anything with this item!
		}
		// Using this item may have consumed or modified it.
		if (item != modification) {
			// The player inventory has to be updated to reflect the change.
			player.getInventory().set(modification, slotIndex);
			// Add a world message to notify of this change.
			world.getWorldMessageQueue().add(new InventorySlotSetMessage(this.getRequestingPlayerId(), modification, slotIndex));
		}
	}
}
