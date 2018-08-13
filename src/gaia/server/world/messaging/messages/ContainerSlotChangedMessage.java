package gaia.server.world.messaging.messages;

import gaia.server.items.ItemType;
import gaia.world.Position;

/**
 * A message containing the details of a container slot change.
 */
public class ContainerSlotChangedMessage implements IWorldMessage {
	/**
	 * The position of the container.
	 */
	private Position position;
	/**
	 * The index of the changed slot.
	 */
	private int slotIndex;
	/**
	 * The new item type of the changed slot.
	 */
	private ItemType itemType;
	
	/**
	 * Create a new instance of the ContainerSlotChangedMessage class.
	 * @param slotIndex The index of the changed slot.
	 * @param itemType The new item type of the changed slot.
	 * @param position The position of the container.
	 */
	public ContainerSlotChangedMessage(int slotIndex, ItemType itemType, Position position) {
		this.slotIndex = slotIndex;
		this.itemType  = itemType;
		this.position  = position;
	}
	
	/**
	 * Get the index of the changed slot.
	 * @return The index of the changed slot.
	 */
	public int getSlotIndex() {
		return this.slotIndex;
	}
	
	/**
	 * Get the new item type of the changed slot.
	 * @return The new item type of the changed slot.
	 */
	public ItemType getSlotItemType() {
		return this.itemType;
	}

	/**
	 * Get the position of the placements.
	 * @return The position of the placements.
	 */
	public Position getPosition() {
		return position;
	}

	@Override
	public WorldMessageType getMessageType() {
		return WorldMessageType.CONTAINER_SLOT_CHANGED;
	}
}
