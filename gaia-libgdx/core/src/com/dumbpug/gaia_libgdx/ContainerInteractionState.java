package com.dumbpug.gaia_libgdx;

import gaia.client.gamestate.IContainerDetails;
import gaia.world.items.container.ContainerCategory;

/**
 * Maintains state for a player interaction with an accessible container.
 */
public class ContainerInteractionState {
	/**
	 * The container details.
	 */
	private IContainerDetails containerDetails;
	/**
	 * The currently selected container index.
	 */
	private int selectedItemIndex = 0;
	
	/**
	 * Create a new instance of the ContainerInteractionState class.
	 * @param containerDetails The container details.
	 */
	public ContainerInteractionState(IContainerDetails containerDetails) {
		this.containerDetails = containerDetails;
	}
	
	/**
	 * Gets whether this state is for the specified container details.
	 * @param containerDetails The container details.
	 * @return Whether this state is for the specified container details.
	 */
	public boolean isStateFor(IContainerDetails containerDetails) {
		return this.containerDetails == containerDetails;
	}
	
	/**
	 * Get the current selectable slot index.
	 * @return The current selectable slot index.
	 */
	public int getCurrentSelectableSlotIndex() {
		return this.selectedItemIndex;
	}
	
	/**
	 * Go to the next available selectable slot.
	 */
	public void goToNextSelectableSlot() {
		this.selectedItemIndex = findNextSlotIndex(this.selectedItemIndex, this.selectedItemIndex);
	}
	
	/**
	 * Find the next selectable slot index.
	 * @param startSlot The starting slot.
	 * @param currentSlot The index of the slot that was last checked.
	 * @return The next selectable slot index.
	 */
	private int findNextSlotIndex(int startSlot, int currentSlot) {
		// Get the next slot index.
		int nextSlotIndex = currentSlot + 1;
		// If the next slot index has exceeded the bounds of the slots list then go back to the start.
		if (nextSlotIndex >= containerDetails.getSize()) {
			nextSlotIndex = 0;
		}
		// There is no next available slot if we wre able to loop back around to the initial slot.
		if (startSlot == currentSlot) {
			return startSlot;
		}
		// If the current slot is not selectable then we need to try the next one.
		if (isSlotSelectable(nextSlotIndex)) {
			// We have found the next available slot.
			return nextSlotIndex;
			
		} else {
			// Keep looking for the next available slot.
			return findNextSlotIndex(startSlot, nextSlotIndex);
		}
	}
	
	/**
	 * Gets whether the slot at the specified index is selectable.
	 * @param slotIndex The container slot index.
	 * @return Whether the slot at the specified index is selectable.
	 */
	public boolean isSlotSelectable(int slotIndex) {
		// Is the container slot index outside the bounds of available slots?
		if (slotIndex < 0 || slotIndex >= containerDetails.getSize()) {
			return false;
		}
		// We cannot select any slots in PICKUP containers where the item is the NONE type.
		if (containerDetails.getCategory() == ContainerCategory.PICKUP && containerDetails.get(slotIndex).isNothing()) {
			return false;
		}
		// We can select this slot.
		return true;
	}
}
