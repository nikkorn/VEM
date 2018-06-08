package server.world.container;

import server.items.ItemType;

/**
 * The exception thrown when attempting to add an item to a container without a free slot.
 */
public class NoFreeSlotException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create an instance of the NoFreeSlotException class.
	 * @param itemType The type of item that was added.
	 */
	public NoFreeSlotException(ItemType itemType) {
        super("cannot add item " + itemType + " to container as no free slot is available");
    }
}
