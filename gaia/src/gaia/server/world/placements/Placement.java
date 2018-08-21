package gaia.server.world.placements;

import org.json.JSONObject;

import gaia.server.world.placements.state.IPlacementState;
import gaia.utils.BitPacker;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementType;
import gaia.world.PlacementUnderlay;
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
	 * The placements action to be executed per game engine tick and/or time update.
	 * This could also be done per interaction if the placements priority is LOW.
	 */
	private IPlacementAction action;
	/**
	 * The placements underlay.
	 */
	private PlacementUnderlay underlay = PlacementUnderlay.NONE;
	/**
	 * The placements overlay.
	 */
	private PlacementOverlay overlay = PlacementOverlay.NONE;
	
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
	 * Get the placements action.
	 * @return The placements action.
	 */
	public IPlacementAction getAction() {
		return action;
	}

	/**
	 * Set the placements action.
	 * @param action The placements action.
	 */
	public void setAction(IPlacementAction action) {
		this.action = action;
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
