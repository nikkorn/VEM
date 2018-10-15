package gaia.networking.messages;

import java.util.ArrayList;
import gaia.networking.IMessage;
import gaia.world.Position;
import gaia.world.items.container.ContainerCategory;
import gaia.world.items.container.ContainerType;
import gaia.world.items.container.IndexedSlot;
import gaia.world.items.container.Slot;

/**
 * The message sent to all clients when a container is added to the world.
 */
public class ContainerCreated implements IMessage {
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
	 * The size of the container.
	 */
	private int size;
	/**
	 * The indexed slots in the container.
	 */
	private ArrayList<IndexedSlot> indexedSlots;
	
	/**
	 * Create a new instance of the ContainerCreated class.
	 * @param position The container position.
	 * @param type The container type.
	 * @param category The container category.
	 * @param size The size of the container, as in the number of slots.
	 * @param indexedSlots The indexed slots in the container.
	 */
	public ContainerCreated(Position position, ContainerType type, ContainerCategory category, int size, ArrayList<IndexedSlot> indexedSlots) {
		this.position          = position;
		this.containerType     = type;
		this.containerCategory = category;
		this.size              = size;
		this.indexedSlots      = indexedSlots;
	}
	
	/**
	 * Create a new instance of the ContainerCreated class.
	 * @param position The container position.
	 * @param type The container type.
	 * @param category The container category.
	 * @param slots The list of slots within this container.
	 */
	public ContainerCreated(Position position, ContainerType type, ContainerCategory category, ArrayList<Slot> slots) {
		this(position, type, category, slots.size(), IndexedSlot.createIndexedSlotList(slots));
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
	 * Gets the size of this container, as in number of slots.
	 * @return The size of this container, as in number of slots.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Gets the indexed slots in the container.
	 * @return The indexed slots in the container.
	 */
	public ArrayList<IndexedSlot> getIndexedSlots() {
		return this.indexedSlots;
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.CONTAINER_CREATED;
	}
}
