package com.dumbpug.vem.GamePanel;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.vem.C;
import com.dumbpug.vem.FontPack;
import com.dumbpug.vem.VEM;
import com.dumbpug.vem.Entities.Drone;
import com.dumbpug.vem.Entities.Vem;
import com.dumbpug.vem.World.WorldFamiliarity;

/**
 * 
 * @author Nikolas Howard
 *
 */
public class GamePanel {
	// The type of the current tab
	private GamePanelTab currentTab;
	// The objects which model each tab.
	private SystemModel systemTabModel = null;
	private Terminal terminalTabModel = null;
	private Inventory inventoryTabModel = null;
	
	public GamePanel() {
		// Our startup tab will be our info tab
		currentTab = GamePanelTab.INFO;
		// Initialise the tab models
		systemTabModel = new SystemModel();
		terminalTabModel = new Terminal();
		inventoryTabModel = new Inventory();
	}
	
	public void setTab(GamePanelTab tab) {
		this.currentTab = tab;
	}

	public GamePanelTab getTab() {
		return currentTab;
	}
	
	/**
	 * When we get a valid ASCII character it should be passed to the Shell IF the shell is the current tab.
	 */
	public void consumeASCIIValue(Integer charDecValue) {
		// Is the shell the current tab?
		if(currentTab == GamePanelTab.SHELL){
			terminalTabModel.processASCIIChar(charDecValue);
		}
	}
	
	/**
	 * When we get a key-press it should be passed to the currently active tab.
	 */
	public void consumeKey(int key, boolean isCTRLPressed) {
		// Process the key press
		switch(key) {
		// ----------CASES TO SET THE CURRENT TAB---------------
		case Input.Keys.F1:
			this.setTab(GamePanelTab.INFO);
			break;
		case Input.Keys.F2:
			this.setTab(GamePanelTab.SHELL);
			break;
		case Input.Keys.F3:
			this.setTab(GamePanelTab.MAP);
			break;
		case Input.Keys.F4:
			this.setTab(GamePanelTab.INVENTORY);
			break;
		case Input.Keys.F5:
			this.setTab(GamePanelTab.SYSTEM);
			break;
		// ------------------------------------------------------
			
		//TODO Add cases for all possible inputs
			
		// ----------CASES TO HANDLE SPECIAL COMMANDS IN TERMINAL EDITOR--------
		case Input.Keys.S: // CTRL + S : Save current script
			if(isCTRLPressed && terminalTabModel.isRunningEditor()) {
				terminalTabModel.editorSaveButtonPress();
			}
			break;
		case Input.Keys.X: // CTRL + X : Exit editor to shell
			if(isCTRLPressed && terminalTabModel.isRunningEditor()) {
				terminalTabModel.editorExitButtonPress();
			}
			break;
		// ------------------------------------------------------
			
		// ----------CASES TO HANDLE RETURN/ENTER PRESSES--------
		case Input.Keys.ENTER:
			// Do something different based on which interactive tab is active
			switch(currentTab) {
			case INVENTORY:
				inventoryTabModel.enterKeyPress();
				break;
			case SHELL:
				terminalTabModel.enterKeyPress();
				break;
			case SYSTEM:
				systemTabModel.enterKeyPress();
				break;
			default:
				break;
			}
			break;
		// ------------------------------------------------------
			
		// ----------CASES TO HANDLE ARROW KEY PRESSES-----------
		case Input.Keys.UP:
		case Input.Keys.DOWN:
		case Input.Keys.LEFT:
		case Input.Keys.RIGHT:
			// Do something different based on which interactive tab is active
			switch(currentTab) {
			case INVENTORY:
				inventoryTabModel.processArrowKeyPress(key);
				break;
			case SHELL:
				terminalTabModel.processArrowKeyPress(key);
				break;
			case SYSTEM:
				systemTabModel.processArrowKeyPress(key);
				break;
			default:
				break;
			}
			
			break;
		// ------------------------------------------------------
		default:
			// We havent got a case for this type of key yet
		}
	}
	
	/**
	 * Draw the entire GamePanel and its contents
	 * @param batch
	 */
	public void draw(SpriteBatch batch) {
		// Firstly, draw the main panel background
		batch.draw(VEM.getTexturePack().consoleBackground, C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX, 0f, (float) C.CONSOLE_VIEWPORT_WIDTH_PX, (float) C.HEIGHT_PX);
		// Now draw the panel header
		switch(currentTab) {
		case INFO:
			batch.draw(VEM.getTexturePack().consoleHeaderInfo, C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX, C.HEIGHT_PX - VEM.getTexturePack().consoleHeaderInfo.getHeight());
			drawInfoTab(batch);
			break;
		case INVENTORY:
			batch.draw(VEM.getTexturePack().consoleHeaderInv, C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX, C.HEIGHT_PX - VEM.getTexturePack().consoleHeaderInfo.getHeight());
			drawInventoryTab(batch);
			break;
		case SHELL:
			batch.draw(VEM.getTexturePack().consoleHeaderShell, C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX, C.HEIGHT_PX - VEM.getTexturePack().consoleHeaderInfo.getHeight());
			drawShellTab(batch);
			break;
		case SYSTEM:
			batch.draw(VEM.getTexturePack().consoleHeaderSystem, C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX, C.HEIGHT_PX - VEM.getTexturePack().consoleHeaderInfo.getHeight());
			drawSystemTab(batch);
			break;
		case MAP:
			batch.draw(VEM.getTexturePack().consoleHeaderMap, C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX, C.HEIGHT_PX - VEM.getTexturePack().consoleHeaderInfo.getHeight());
			drawMapTab(batch);
			break;
		default:
			break;
		}
	}

	/**
	 * Draw the map tab contents
	 * @param batch
	 */
	private void drawMapTab(SpriteBatch batch) {
		// Draw the actual map.
		batch.draw(VEM.getTexturePack().consoleMap, (C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX) + C.CONSOLE_MAP_INSET_X_PX, C.CONSOLE_MAP_INSET_Y_PX, C.CONSOLE_MAP_SIZE_PX, C.CONSOLE_MAP_SIZE_PX);
		// Draw the map fog based on map familiarity.
		WorldFamiliarity wFamiliarity = VEM.world.getWorldFamiliarity();
		float fogPanelSize = (C.CONSOLE_MAP_SIZE_PX/C.CONSOLE_MAP_FAMILIARITY_GRID_SIZE) * 1.038f;
		for(int fogPanelPosY = 0; fogPanelPosY < C.CONSOLE_MAP_FAMILIARITY_GRID_SIZE; fogPanelPosY++) {
			for(int fogPanelPosX = 0; fogPanelPosX < C.CONSOLE_MAP_FAMILIARITY_GRID_SIZE; fogPanelPosX++) {
				if(!wFamiliarity.isCellFamiliar(fogPanelPosY, fogPanelPosX)) {
					batch.draw(VEM.getTexturePack().consoleDarkGreenSquare, (C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX) + C.CONSOLE_MAP_INSET_X_PX + (fogPanelSize*fogPanelPosX), 
							C.CONSOLE_MAP_INSET_Y_PX + (fogPanelSize*fogPanelPosY), fogPanelSize, fogPanelSize);
				}
			}
		}
		// Draw the mini-map border
		batch.draw(VEM.getTexturePack().consoleMapBackdrop, (C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX) + C.CONSOLE_MAP_INSET_X_PX, C.CONSOLE_MAP_INSET_Y_PX, C.CONSOLE_MAP_SIZE_PX * 1.01f, C.CONSOLE_MAP_SIZE_PX * 1.01f);
		// Draw the key above the map
		BitmapFont font = FontPack.getFont(FontPack.FontType.SHELL_FONT);
		// Draw the player key
		batch.draw(VEM.getTexturePack().consoleMapPlayerMarker, (C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX) + 30, 535, C.CONSOLE_MAP_MARKER_SIZE, C.CONSOLE_MAP_MARKER_SIZE);
		font.draw(batch, "Player", (C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX) + 60, 552);
		// Draw the drone key
		batch.draw(VEM.getTexturePack().consoleMapDroneMarker, (C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX) + 210, 535, C.CONSOLE_MAP_MARKER_SIZE, C.CONSOLE_MAP_MARKER_SIZE);
		font.draw(batch, "Drone", (C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX) + 240, 552);
		// Let us draw the drone markers
		for(Drone drone : VEM.world.getDrones()) {
			float droneMarkerPosX = C.CONSOLE_MAP_SIZE_PX * ((float) drone.getCellX()/C.WORLD_CELL_SIZE)- (C.CONSOLE_MINI_MAP_MARKER_SIZE/2);
			float droneMarkerPosY = C.CONSOLE_MAP_SIZE_PX * ((float) drone.getCellY()/C.WORLD_CELL_SIZE) - (C.CONSOLE_MINI_MAP_MARKER_SIZE/2);
			batch.draw(VEM.getTexturePack().consoleMapDroneMarker, (C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX) + C.CONSOLE_MAP_INSET_X_PX + droneMarkerPosX, C.CONSOLE_MAP_INSET_Y_PX + droneMarkerPosY, C.CONSOLE_MINI_MAP_MARKER_SIZE, C.CONSOLE_MINI_MAP_MARKER_SIZE);
		}
		// Let us draw the player marker.
		Vem vem = VEM.world.getVem();
		float playerMarkerPosX = C.CONSOLE_MAP_SIZE_PX * ((float) vem.getCellX()/C.WORLD_CELL_SIZE)- (C.CONSOLE_MAP_MARKER_SIZE/2);
		float playerMarkerPosY = C.CONSOLE_MAP_SIZE_PX * ((float) vem.getCellY()/C.WORLD_CELL_SIZE) - (C.CONSOLE_MAP_MARKER_SIZE/2);
		batch.draw(VEM.getTexturePack().consoleMapPlayerMarker, (C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX) + C.CONSOLE_MAP_INSET_X_PX + playerMarkerPosX, C.CONSOLE_MAP_INSET_Y_PX + playerMarkerPosY, C.CONSOLE_MAP_MARKER_SIZE, C.CONSOLE_MAP_MARKER_SIZE);
	}

	/**
	 * Draw the System tab contents
	 * @param batch
	 */
	private void drawSystemTab(SpriteBatch batch) {
		BitmapFont font = FontPack.getFont(FontPack.FontType.GAME_PANEL_FONT);
		// Make unselected items slightly transparent
		font.setColor(255, 255, 255, (float) 0.2);
		
		//----------------------------- Draw the Menu Items ---------------------------------
		if(SystemModel.SystemMenuValues.SAVE == systemTabModel.highlightedMenuItem) {
			font.setColor(255, 255, 255, (float) 1);
		}
		font.draw(batch, "SAVE", (C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX) + 30, 552);
		font.setColor(255, 255, 255, (float) 0.2);
		
		if(SystemModel.SystemMenuValues.SETTINGS == systemTabModel.highlightedMenuItem) {
			font.setColor(255, 255, 255, (float) 1);
		}
		font.draw(batch, "SETTINGS", (C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX) + 30, 522);
		font.setColor(255, 255, 255, (float) 0.2);
		
		if(SystemModel.SystemMenuValues.QUIT == systemTabModel.highlightedMenuItem) {
			font.setColor(255, 255, 255, (float) 1);
		}
		font.draw(batch, "QUIT", (C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX) + 30, 492);
		//-----------------------------------------------------------------------------------
		
		// Set our font transparency back to normal
		font.setColor(255, 255, 255, (float) 1);
	}

	/**
	 * Draw the Shell Tab Contents.
	 * @param batch
	 */
	private void drawShellTab(SpriteBatch batch) {
		batch.end();
		batch.begin();
		// Do the draw in the shell object
		terminalTabModel.draw(batch);
		batch.end();
		batch.begin();
	}

	/**
	 * Draw the Inventory Tab Contents.
	 * @param batch
	 */
	private void drawInventoryTab(SpriteBatch batch) {
		// TODO Auto-generated method stub
	}

	/**
	 * Draw the Info Tab Contents.
	 * @param batch
	 */
	private void drawInfoTab(SpriteBatch batch) {
		// Write the players current Y/X coordinates to the screen
		BitmapFont font = FontPack.getFont(FontPack.FontType.GAME_PANEL_FONT);
		font.draw(batch, "POSITION", (C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX) + 40, 65);
		font.draw(batch, "Y: " + VEM.world.getVem().getCellY(), (C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX) + 220, 65);
		font.draw(batch, "X: " + VEM.world.getVem().getCellX(), (C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX) + 370, 65);
	}
}
