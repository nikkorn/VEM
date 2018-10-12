package gaia.networking.messages;

import gaia.networking.IMessage;
import gaia.world.Position;
import gaia.world.items.ItemType;

/**
 * A message sent to a client notifying them of a slot being set in a container.
 */
public class ContainerSlotSet implements IMessage {
	/**
	 * The position of the container.
	 */
	private Position position;
	/**
	 * The index of the set slot.
	 */
	private int slotIndex;
	/**
	 * The type of item that is now held in the slot.
	 */
	private ItemType itemHeld; 

	/**
	 * Create a new instance of the ContainerSlotSet class.
	 * @param position The index of the set slot.
	 * @param slotIndex The packed composition of the placement.
	 * @param itemHeld The type of item that is now held in the slot.
	 */
	public ContainerSlotSet(Position position, int slotIndex, ItemType itemHeld) {
		this.position  = position; 
		this.slotIndex = slotIndex;
		this.itemHeld  = itemHeld; 
	}
	
	/**
	 * Get the position of the container.
	 * @return The position of the container.
	 */
	public Position getPosition() {
		return this.position;
	}
	
	/**
	 * Get the index of the set slot.
	 * @return The index of the set slot.
	 */
	public int getSlotIndex() {
		return this.slotIndex;
	}
	
	/**
	 * Get the type of item that is now held in the slot.
	 * @return The type of item that is now held in the slot.
	 */
	public ItemType getItemHeld() {
		return this.itemHeld;
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.CONTAINER_SLOT_SET;
	}
}
