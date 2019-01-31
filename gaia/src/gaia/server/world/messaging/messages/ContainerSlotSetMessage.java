package gaia.server.world.messaging.messages;

import java.util.List;
import gaia.server.engine.IWorldEventsHandler;
import gaia.world.Position;
import gaia.world.items.ItemType;

/**
 * A message containing the details of a container slot change.
 */
public class ContainerSlotSetMessage implements IWorldMessage {
	/**
	 * The ids of any players who care about the added container.
	 */
	private String[] playerIds;
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
	 * Create a new instance of the ContainerSlotSetMessage class.
	 * @param slotIndex The index of the changed slot.
	 * @param itemType The new item type of the changed slot.
	 * @param position The position of the container.
	 */
	public ContainerSlotSetMessage(List<String> playerIds, int slotIndex, ItemType itemType, Position position) {
		this.playerIds = playerIds.toArray(new String[playerIds.size()]);
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
	public void process(IWorldEventsHandler handler) {
		handler.onContainerSlotSet(playerIds, position, itemType, slotIndex);
	}
}
