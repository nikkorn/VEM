package com.dumbpug.vem;

public class C {
	public static final float MODAL_BORDER = 20;
	public static final int MODAL_MAX_LENGTH = 70;
	public static final int WIDTH = 15; // !!Dont change this unless you are willing to fix a lot of hardcoded stuff
	public static final int HEIGHT = 15; // !!Dont change this unless you are willing to fix a lot of hardcoded stuff
	public static final int WIDTH_PX = 1100;
	public static final int HEIGHT_PX = 600;
	public static final double CELL_UNIT = HEIGHT_PX / WIDTH;
	public static final int MOVEMENT_UNIT_DIVISION = 16;
	public static final int MOVEMENT_UNIT_DIVISION_FAST = 12;
	public static final int MOVEMENT_UNIT_DIVISION_SUPERFAST = 8;
	public static final double MOVEMENT_UNIT = CELL_UNIT / MOVEMENT_UNIT_DIVISION;
	public static final int WORLD_CELL_SIZE = 1000;
	
	//------ GamePanel Specific---------
	public static final int CONSOLE_VIEWPORT_WIDTH_PX = WIDTH_PX - HEIGHT_PX;
	//---------------------- MAP -------
	public static final int CONSOLE_MAP_SIZE_PX = (int) (CONSOLE_VIEWPORT_WIDTH_PX * 0.91);
	public static final int CONSOLE_MAP_INSET_X_PX = (int) (CONSOLE_VIEWPORT_WIDTH_PX * 0.05);
	public static final int CONSOLE_MAP_INSET_Y_PX = (int) (CONSOLE_MAP_INSET_X_PX * 2.2);
	public static final int CONSOLE_MAP_MARKER_SIZE = (int) (CONSOLE_VIEWPORT_WIDTH_PX * 0.04);
	public static final int CONSOLE_MINI_MAP_MARKER_SIZE = (int) (CONSOLE_VIEWPORT_WIDTH_PX * 0.025);
	public static final int CONSOLE_MAP_FAMILIARITY_GRID_SIZE = 40;
	
	// Save File Paths
	public static final String WORLD_FAMILIARITY_SAVE_FILE = "bin/map/worldFm";
	public static final String ENTITY_VEM_SAVE_FILE = "bin/map/specials/vem.char";
	public static final String ENTITY_DRONE_SAVE_DIRECTORY = "bin/map/specials/drones/";
}
