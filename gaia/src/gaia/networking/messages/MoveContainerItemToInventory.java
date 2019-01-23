package gaia.networking.messages;

import gaia.networking.IMessage;
import gaia.world.items.ItemType;

/**
 * A message sent to the server requesting to move a container item to an inventory.
 */
public class MoveContainerItemToInventory implements IMessage  {
	
	/**
	 * Create a new instance of the MoveContainerItemToInventory class.
	 * @param containerSlotIndex The index of the container slot containing the item the player wishes to move.
	 * @param expectedContainerItem The item that was in the container slot at the time of making the request.
	 * @param inventorySlotIndex The index of the inventory slot that the player wishes to move the item to.
	 * @param expectedInventoryItem The item that was in the inventory slot at the time of making the request.
	 */
	public MoveContainerItemToInventory(int containerSlotIndex, ItemType expectedContainerItem, int inventorySlotIndex, ItemType expectedInventoryItem) {
		// TODO
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.MOVE_CONTAINER_ITEM_TO_INVENTORY;
	}
}
