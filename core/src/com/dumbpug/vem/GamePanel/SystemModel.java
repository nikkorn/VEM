package com.dumbpug.vem.GamePanel;

import com.badlogic.gdx.Input;
import com.dumbpug.vem.VEM;

/**
 * 
 * @author Nikolas Howard
 *
 */
public class SystemModel {
	SystemMenuValues highlightedMenuItem = SystemMenuValues.SAVE;
	
	public enum SystemMenuValues { 
		SAVE,
		SETTINGS,
		QUIT
	}
	
	/**
	 * Process an arrow key press that was made while this tab was active
	 */
	public void processArrowKeyPress(int key) {
		// Is the user scrolling up/down through menu?
		if(key == Input.Keys.UP){
			if(highlightedMenuItem.ordinal() != 0){
				highlightedMenuItem = SystemMenuValues.values()[highlightedMenuItem.ordinal() - 1];
			}
		} else if(key == Input.Keys.DOWN){
			if(highlightedMenuItem.ordinal() != (SystemMenuValues.values().length - 1)){
				highlightedMenuItem = SystemMenuValues.values()[highlightedMenuItem.ordinal() + 1];
			}
		}
	}
	
	/**
	 * Process an Enter/Return key press that was made while this tab was active
	 */
	public void enterKeyPress() {
		switch(highlightedMenuItem) {
		case QUIT:
			VEM.setQuitPending();
			break;
		case SAVE:
			VEM.setSavePending();
			break;
		case SETTINGS:
			break;
		}
	}
}
