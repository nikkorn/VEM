package gaia.networking.messages;

import gaia.networking.IMessage;
import gaia.world.Position;
import gaia.world.items.ItemType;
import gaia.world.items.container.ContainerCategory;
import gaia.world.items.container.ContainerType;

/**
 * The message sent to all clients when a container is added to the world.
 */
public class ContainerAdded implements IMessage {
	/**
	 * The position of the container.
	 */
	private Position position;
	/**
	 * The container type.
	 */
	private ContainerType containerType;
	/**
	 * The container category.
	 */
	private ContainerCategory containerCategory;
	/**
	 * The items held.
	 */
	private ItemType[] items;
	
	/**
	 * Create a new instance of the ContainerAdded class.
	 * @param position The container position.
	 * @param type The container type.
	 * @param category The container category.
	 * @param size The size of the container, as in the number of slots.
	 * @param indexedSlots The indexed slots in the container.
	 */
	public ContainerAdded(Position position, ContainerType type, ContainerCategory category, ItemType[] items) {
		this.position          = position;
		this.containerType     = type;
		this.containerCategory = category;
		this.items             = items;
	}
	
	/**
	 * Gets the position of the added container.
	 * @return The position of the added container.
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Gets the container type.
	 * @return The container type.
	 */
	public ContainerType getContainerType() {
		return containerType;
	}

	/**
	 * Gets the container category.
	 * @return The container category.
	 */
	public ContainerCategory getContainerCategory() {
		return containerCategory;
	}
	
	/**
	 * Gets the array of items held in the container.
	 * @return The array of items held in the container.
	 */
	public ItemType[] getItemsHeld() {
		return this.items;
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.CONTAINER_ADDED;
	}
}
