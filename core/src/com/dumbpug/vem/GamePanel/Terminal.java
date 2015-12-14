package com.dumbpug.vem.GamePanel;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 
 * @author Nikolas Howard
 *
 */
public class Terminal {
	// Our actual shell
	private Shell shell;
	// Out in-shell editor
	private Editor editor;
	// Currently running application
	private TerminalApplication currentApp = null;;
	
	public Terminal() {
		shell = new Shell();
		editor = new Editor();
		// The default terminal app will always be the shell
		currentApp = shell;
	}
	
	/**
	 * Process a valid input char that was types while this tab was active.
	 */
	public void processASCIIChar(int decCharValue) {
		currentApp.processASCIIChar(decCharValue);
	}

	/**
	 * Process an arrow key press that was made while this tab was active
	 */
	public void processArrowKeyPress(int key) {
		currentApp.processArrowKeyPress(key);
	}

	/**
	 * Process a press of the ENTER key. Carriage returns will also be caught by 'processASCIIChar()' but only as text input.
	 */
	public void enterKeyPress() {
		currentApp.enterKeyPress();
	}

	/**
	 * Draw the current Terminal application (shell/editor)
	 * @param batch
	 */
	public void draw(SpriteBatch batch) {
		shell.act();
		editor.act();
		currentApp.draw(batch);
	}
	
	public boolean isRunningEditor() {
		return currentApp == editor;
	}
	
	public boolean isRunningShell() {
		return currentApp == shell;
	}

	public void editorSaveButtonPress() {
		editor.saveCurrentFile();
	}

	public void editorExitButtonPress() {
		editor.exitEditor();
	}
}
