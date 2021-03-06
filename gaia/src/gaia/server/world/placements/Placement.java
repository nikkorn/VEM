package gaia.server.world.placements;

import org.json.JSONObject;
import gaia.server.world.PlacementModificationsHandler;
import gaia.server.world.messaging.WorldMessageQueue;
import gaia.server.world.messaging.messages.ContainerSlotChangedMessage;
import gaia.server.world.placements.state.IPlacementState;
import gaia.utils.BitPacker;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementType;
import gaia.world.PlacementUnderlay;
import gaia.world.Position;
import gaia.world.items.ItemType;
import gaia.world.items.container.Container;

/**
 * Represents a tile-positioned placements.
 */
public class Placement implements IModifiablePlacement, IPlacementDetails {
	/**
	 * The placements type.
	 */
	private PlacementType type;
	/**
	 * The placements position within its parent chunk.
	 */
	private short x, y;
	/**
	 * The container of the placements.
	 */
	private Container container;
	/**
	 * The placements priority.
	 */
	private Priority priority = Priority.NONE;
	/**
	 * The placements state.
	 */
	private IPlacementState state;
	/**
	 * The placements actions to be executed per game engine tick and/or time update.
	 * This could also be done per interaction if the placements priority is LOW.
	 */
	private IPlacementActions actions;
	/**
	 * The placements underlay.
	 */
	private PlacementUnderlay underlay = PlacementUnderlay.NONE;
	/**
	 * The placements overlay.
	 */
	private PlacementOverlay overlay = PlacementOverlay.NONE;
	/**
	 * Whether this placement is marked for deletion.
	 */
	private boolean isMarkedForDeletion = false;
	
	/**
	 * Create a new instance of the Placement class.
	 * @param type The placements type.
	 * @param x The x position of the placements with its parent chunk.
	 * @param y The y position of the placements with its parent chunk.
	 */
	public Placement(PlacementType type, short x, short y) {
		this.type = type;
		this.x    = x;
		this.y    = y;
	}
	
	/**
	 * Get the placements x position within its parent chunk.
	 * @return The placements x position within its parent chunk.
	 */
	public short getX() {
		return this.x;
	}

	/**
	 * Get the placements y position within its parent chunk.
	 * @return The placements y position within its parent chunk.
	 */
	public short getY() {
		return this.y;
	}
	
	/**
	 * Get the placements type.
	 * @return The placements type.
	 */
	public PlacementType getType() {
		return this.type;
	}

	/**
	 * Get the placements priority.
	 * @return The placements priority.
	 */
	public Priority getPriority() {
		return priority;
	}

	/**
	 * Set the placements priority.
	 * @param priority The placements priority.
	 */
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	/**
	 * Get the placement actions.
	 * @return The placement actions.
	 */
	public IPlacementActions getActions() {
		return actions;
	}

	/**
	 * Set the placement actions.
	 * @param action The placement actions.
	 */
	public void setActions(IPlacementActions actions) {
		this.actions = actions;
	}

	/**
	 * Get the placements container.
	 * @return The placements container.
	 */
	public Container getContainer() {
		return container;
	}

	/**
	 * Set the placements container.
	 * @param container The placements container.
	 */
	public void setContainer(Container container) {
		this.container = container;
	}

	/**
	 * Get the state of the placements.
	 * @return The state of the placements.
	 */
	public IPlacementState getState() {
		return state;
	}

	/**
	 * Set the state of the placements.
	 * @param state The state of the placements.
	 */
	public void setState(IPlacementState state) {
		this.state = state;
	}
	
	/**
	 * Get the placements underlay.
	 * @return The placements underlay.
	 */
	public PlacementUnderlay getUnderlay() {
		return this.underlay;
	}
	
	/**
	 * Set the placements underlay.
	 * @param underlay The placements underlay.
	 */
	public void setUnderlay(PlacementUnderlay underlay) {
		this.underlay = underlay;
	}
	
	/**
	 * Get the placements overlay.
	 * @return The placements overlay.
	 */
	public PlacementOverlay getOverlay() {
		return this.overlay;
	}
	
	/**
	 * Set the placements overlay.
	 * @param overlay The placements overlay.
	 */
	public void setOverlay(PlacementOverlay overlay) {
		this.overlay = overlay;
	}
	
	/**
	 * Get whether this position is walkable.
	 * @return Whether this position is walkable.
	 */
	public boolean isWalkable() {
		// A position with a placement is walkable if both the placement overlay and underlay are walkable.
		return this.getOverlay().isWalkable() && this.getUnderlay().isWalkable();
	}
	
	/**
	 * Get whether this placement is marked for deletion.
	 * @return Whether this placement is marked for deletion.
	 */
	public boolean isMarkedForDeletion() {
		return this.isMarkedForDeletion;
	}
	
	/**
	 * Mark this placement for deletion.
	 */
	@Override
	public void MarkForDeletion() {
		this.isMarkedForDeletion = true;
	}
	
	/**
	 * Get the placements as a packed integer.
	 *   bits 0-9   - Underlay Type
	 *   bits 10-19 - Overlay Type
	 *   bits 20-29 - Placement Type
	 *   bits 30-31 - ?
	 * @return The placements as a packed integer.
	 */
	public int asPackedInt() {
		// Pack the underlay type.
		int packed = BitPacker.pack(0, this.underlay.ordinal(), 0, 10);
		// Pack the overlay type.
		packed = BitPacker.pack(packed, this.overlay.ordinal(), 10, 10);
		// Pack the placements type.
		packed = BitPacker.pack(packed, this.type.ordinal(), 20, 10);
		// Return the packed value.
		return packed;
	}
	
	/**
	 * Execute relevant actions for the placement using a placement actions executor.
	 * @param position The absolute world position of the placement.
	 * @param executor The actions executor.
	 * @param placementModificationsHandler The placement modification handler.
	 * @return The potential modification that was made to an item used in an interaction with the placement.
	 */
	public ItemType executeActions(Position position, IPlacementActionsExecutor executor, PlacementModificationsHandler placementModificationsHandler, WorldMessageQueue worldMessageQueue) {
		// A side effect of executing placement actions could be changes to overlay, underlay and container state.
		// We need to respond to any of these changes and add a message to the world message queue to let people know.
		PlacementUnderlay preActionUnderlay    = underlay;
		PlacementOverlay preActionOverlay      = overlay;
		ItemType[] preActionContainerItemTypes = null;
		// The placements may not even have a container.
		if (getContainer() != null) {
			preActionContainerItemTypes = container.asItemTypeArray();
		}
		// Execute the placement actions using the actions executor provided.
		// One of these actions could be to interact with the placement using an item.
		// In this case the modification made to the item will be returned by the executor.
		ItemType modification = executor.execute(this.actions);
		// Was the placement marked for deletion while the placement actions were being executed?
		if (this.isMarkedForDeletion) {
			// We are trying to delete this placement! Handle the placement deletion.
			placementModificationsHandler.onPlacementRemoved(this, position);
			// We do not care about modifications made to the placement, just that it has been deleted.
			return modification;
		}
		// We are finished executing our actions. Has the underlay or overlay changed?
		if (underlay != preActionUnderlay || overlay != preActionOverlay) {
			// Handle the placement change.
			placementModificationsHandler.onPlacementChanged(this, position);
		}
		// Handle changes to the state of the container if we have one.
		if (container != null) {
			for (int containerItemIndex = 0; containerItemIndex < container.size(); containerItemIndex++) {
				// Has the item type at the current container slot changed?
				if (preActionContainerItemTypes[containerItemIndex] != container.get(containerItemIndex)) {
					// Add a message to the world message queue to notify of the change.
					worldMessageQueue.add(new ContainerSlotChangedMessage(containerItemIndex, container.get(containerItemIndex), position));
				}
			}
		}
		// Return the potential modification that was made to an item used in an interaction with the placement.
		return modification;
	}
	
	/**
	 * Serialise the placements to JSON to be persisted to disk.
	 * @return The serialised placements.
	 */
	public JSONObject serialise() {
		// Create the JSON object that will hold the information about this placements.
		JSONObject placement = new JSONObject();
		// Set the position.
		placement.put("x", this.getX());
		placement.put("y", this.getY());
		// Set the type.
		placement.put("type", this.type.ordinal());
		// Set the underlay.
		placement.put("underlay", this.underlay.ordinal());
		// Set the overlay.
		placement.put("overlay", this.overlay.ordinal());
		// Set container array (if this placements has one).
		if (this.container != null) {
			placement.put("container", this.container.serialise());
		}
		// Set state (if this placements has any).
		if (this.state != null) {
			placement.put("state", this.state.asJSON());
		}
		// Set the priority.
		placement.put("priority", this.priority.ordinal());
		// Return the serialised placements.
		return placement;
	}
}
