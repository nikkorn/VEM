package com.dumbpug.vem;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 
 * @author Nikolas Howard
 *
 */
public class TexturePack {
	// Textures
	public Texture modalBackgroundTexture;
	public Texture GROUND_SEA;
	public Texture GROUND_SAND_1;
	public Texture GROUND_SAND_2;
	public Texture GROUND_SAND_3_4;
	public Texture ROCK_1;
	public Texture ROCK_2;
	public Texture ROCK_3;
	public Texture ROCK_4;
	public Texture ROCK_5;
	public Texture BUSH_1;
	public Texture SHORE;
	public Texture GEM_STAGE_1;
	public Texture GEM_STAGE_2;
	public Texture GEM_STAGE_3;
	public Texture SCRAP_STAGE_1;
	public Texture SCRAP_STAGE_2;
	public Texture SCRAP_STAGE_3;
	
	public Texture consoleBackground;
	public Texture consoleHeaderInfo;
	public Texture consoleHeaderShell;
	public Texture consoleHeaderMap;
	public Texture consoleHeaderInv;
	public Texture consoleHeaderSystem;
	public Texture consoleMap;
	public Texture consoleMapBackdrop;
	public Texture consoleDarkGreenSquare;
	public Texture consoleDarkGreenSquarePlain;
	public Texture consoleMapPlayerMarker;
	public Texture consoleMapDroneMarker;
	
	public Texture systemDialogBox;
	
	// Texture Regions
	public TextureRegion[][] vemTextureRegions;
	public TextureRegion[][] droneTextureRegions;
	
	public void loadResources() {
		consoleBackground = new Texture("images/misc/vem_console.png");
		consoleHeaderInfo = new Texture("images/misc/console_header_info.png");
		consoleHeaderShell = new Texture("images/misc/console_header_shell.png");
		consoleHeaderMap = new Texture("images/misc/console_header_map.png");
		consoleHeaderInv = new Texture("images/misc/console_header_inventory.png");
		consoleHeaderSystem = new Texture("images/misc/console_header_system.png");
		consoleMapPlayerMarker = new Texture("images/misc/playerMarker.png");
		consoleMapBackdrop = new Texture("images/misc/console_map_backdrop.png");
		consoleMapDroneMarker = new Texture("images/misc/droneMarker.png");
		consoleDarkGreenSquare = new Texture("images/misc/console_dark_green_cube.png");
		consoleDarkGreenSquarePlain = new Texture("images/misc/console_dark_green_cube_plain.png");
		
		systemDialogBox = new Texture("images/misc/dialog_box.png");
		
		GROUND_SEA = new Texture("images/world/SEA.png");
		GROUND_SAND_1 = new Texture("images/world/SAND_34.png");
		GROUND_SAND_2 = new Texture("images/world/SAND_2.png");
		GROUND_SAND_3_4 = new Texture("images/world/SAND_34.png");
		ROCK_1 = new Texture("images/world/ROCK_1.png");
		ROCK_2 = new Texture("images/world/ROCK_2.png");
		ROCK_3 = new Texture("images/world/ROCK_3.png");
		ROCK_4 = new Texture("images/world/ROCK_4.png");
		ROCK_5 = new Texture("images/world/ROCK_5.png");
		BUSH_1 = new Texture("images/world/BUSH_1.png");
		SHORE = new Texture("images/world/SHORE.png");
		GEM_STAGE_1 = new Texture("images/world/GEM_STAGE_1.png");
		GEM_STAGE_2 = new Texture("images/world/GEM_STAGE_2.png");
		GEM_STAGE_3 = new Texture("images/world/GEM_STAGE_3.png");
		SCRAP_STAGE_1 = new Texture("images/world/SCRAP_STAGE_1.png");
		SCRAP_STAGE_2 = new Texture("images/world/SCRAP_STAGE_2.png");
		SCRAP_STAGE_3 = new Texture("images/world/SCRAP_STAGE_3.png");
		modalBackgroundTexture = new Texture("images/misc/speech_back.png");
		droneTextureRegions = TextureRegion.split(new Texture("images/entities/BasicDrone_2.png"), 33, 33);
		vemTextureRegions = TextureRegion.split(new Texture("images/entities/vem.png"), 25, 25);
	}
	
	public void loadConsoleMap() {
		consoleMap = new Texture("bin/map/vemmap_img_console.png");
	}
}
