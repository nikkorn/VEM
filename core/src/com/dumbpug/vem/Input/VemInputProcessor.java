package com.dumbpug.vem.Input;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.dumbpug.vem.Entities.Direction;
import com.dumbpug.vem.Entities.Vem;
import com.dumbpug.vem.GamePanel.GamePanel;

/**
 * Processes all input for the game
 * @author Nikolas Howard
 *
 */
public class VemInputProcessor implements InputProcessor {
	// List of all pending key presses to be handled by main game loop.
	LinkedList<KeyPress> pendingKeyPresses = new LinkedList<KeyPress>();
	// List of all pending char input to be processed by the GamePanel Shell/Editor.
	LinkedList<Integer> pendingCharInput = new LinkedList<Integer>();

	@Override
	public boolean keyDown(int keycode) {
		// Are either of the CTRL keys pressed?
		if(!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && !Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
			// Firstly, are these arrow key presses for player movement? (where the CTRL key is NOT held) if they are
			// then we do nothing as processPendingPlayerMovement() will take care of that
			if(keycode == Input.Keys.UP || keycode == Input.Keys.DOWN || keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT) {
				return true;
			}
			// Handle buttons that have the same functionality regardless of whether CTRL is pressed
			synchronized(pendingKeyPresses) {
				pendingKeyPresses.add(new KeyPress(keycode, false));
			}
		} else {
			// Handle special buttons that have different functionality when the CTRL button is pressed
			synchronized(pendingKeyPresses) {
				pendingKeyPresses.add(new KeyPress(keycode, true));
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		//Is this actually a character that can be processedby the Shell/Editor?
		if(ValidCharInput.isValid((int)character)) {
			// Add the decimal value of this to our list of pending characters
			synchronized(pendingCharInput) {
				pendingCharInput.add((int)character);
			}
		}
		return false;
	}
	
	/**
	 * Checks to see if the CTRL button is not pressed and an arrow button is (conditions for moving character)
	 * @param vem
	 */
	public void processPendingPlayerMovement(Vem vem) {
		// Make sure neither of the CTRL buttons are held
		if(!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && !Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) { 
			// Move character based on arrow direction
			if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
				vem.move(Direction.NORTH, 1);
			} else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				vem.move(Direction.SOUTH, 1);
			} else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				vem.move(Direction.WEST, 1);
			} else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				vem.move(Direction.EAST, 1);
			}
		}
	}
	
	/**
	 * Passes each pressed key to the gamepanel, as the gamepanel will decide what it wants to do with the input.
	 * @param gamePanel
	 */
	public void feedInputToGamePanel(GamePanel gamePanel) {
		// Process pending key presses for system.
		LinkedList<KeyPress> pendingKeyPressesCopy = new LinkedList<KeyPress>();
		synchronized(pendingKeyPresses) {
			// Only do processing if we have some pending key presses
			if(pendingKeyPresses.size() > 0) {
				for(KeyPress press : pendingKeyPresses) {
					pendingKeyPressesCopy.add(new KeyPress(press.key, press.isCTRLPressed));
				}
				// Clear the list of pending keypresses
				pendingKeyPresses.clear();
			}
		}
		for(KeyPress press : pendingKeyPressesCopy) {
			gamePanel.consumeKey(press.key, press.isCTRLPressed);
		}
		// Process pending char input for Shell/Editor
		LinkedList<Integer> pendingCharInputCopy = new LinkedList<Integer>();
		synchronized(pendingCharInput) {
			// Only do processing if we have some pending key presses
			if(pendingCharInput.size() > 0) {
				for(Integer charDecValue : pendingCharInput) {
					pendingCharInputCopy.add(charDecValue);
				}
				// Clear the list of pending keypresses
				pendingCharInput.clear();
			}
		}
		for(Integer charDecValue : pendingCharInputCopy) {
			gamePanel.consumeASCIIValue(charDecValue);
		}
	}
	
	//---------------------------------------------------------------------------
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Represents a key press to be passed to the GamePanel
	 * @author Nikolas Howard
	 *
	 */
	public class KeyPress {
		public int key;
		public boolean isCTRLPressed = false;
		
		public KeyPress(int key, boolean isCTRLPressed) {
			this.key = key;
			this.isCTRLPressed = isCTRLPressed;
		}
	}
}
