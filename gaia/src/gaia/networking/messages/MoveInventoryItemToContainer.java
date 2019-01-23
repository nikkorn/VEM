package gaia.networking.messages;

import gaia.networking.IMessage;

/**
 * A message sent to the server requesting to move an inventory item to a container.
 */
public class MoveInventoryItemToContainer implements IMessage  {

	@Override
	public int getTypeId() {
		return MessageIdentifier.MOVE_INVENTORY_ITEM_TO_CONTAINER;
	}
}