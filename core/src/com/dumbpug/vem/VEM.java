package com.dumbpug.vem;

import java.util.ArrayList;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.vem.Entities.Drone;
import com.dumbpug.vem.Entities.Vem;
import com.dumbpug.vem.GamePanel.GamePanel;
import com.dumbpug.vem.Input.VemInputProcessor;
import com.dumbpug.vem.Persistance.ResourceSaver;
import com.dumbpug.vem.World.World;

public class VEM extends ApplicationAdapter {
	SpriteBatch batch;
	BitmapFont font;
	FPSLogger logger;
	// Responsible for loading application resources/heavy lifting.
	ResourceLoader resourceLoader = null;
	// Holds all textures/ texture region arrays for the application.
	private static TexturePack texturePack;
	// Our Screen Drawer.
	private ScreenDrawer screenDrawer;
	// Texture for loading screen
	private Texture loadingTexture;
	// Our world object. 
	public static World world;
	// Our GamePanel
	public GamePanel gamePanel;
	// Out InputProcessor
	private VemInputProcessor inputProcessor = null;
	
	// -------------------------- System Flags --------------------------------
	private boolean loadingScreenShown = false;
	private boolean savingScreenShown = false;
    private static boolean isSavePending = false;
    private static boolean isQuitPending = false;
	// private boolean isInputIgnored = false;  // user input does nothing when true
	// ------------------------------------------------------------------------
	
	@Override
	public void create () {
		texturePack = new TexturePack();
		FontPack.loadFonts();
		batch = new SpriteBatch();
		font = new BitmapFont();
		logger = new FPSLogger();
		// Create a new world object and load the console map texture.
		world = new World();
		// Create a new gamepanel
		gamePanel = new GamePanel();
		// Create a new ScreenDrawer.
		screenDrawer = new ScreenDrawer();
		// load our loading texture as we will need this to show our loading screen
		loadingTexture = new Texture("images/misc/loading.png");
		// Load our resources in a new thread (so we can draw our loading screen and stuff)
		resourceLoader = new ResourceLoader(world, texturePack);
		// Inisialise our InputProcessor and set it as our primary InputProcessor.
		inputProcessor = new VemInputProcessor();
		Gdx.input.setInputProcessor(inputProcessor);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Have we shown the loading screen? if not then this is application startup and we should show it.
		if(!loadingScreenShown) {
			batch.begin();
			batch.draw(loadingTexture, Gdx.graphics.getWidth() - loadingTexture.getWidth() , 0);
			batch.end();
			// we have now shown the loading screen, set a flag so we dont repaint it.
			loadingScreenShown = true;
			// Return as our resources are not ready and we can't continue.
			return;
		}
		
		// If our resources are not loaded then we need to do this now.
		if(!resourceLoader.isLoaded()) {
			resourceLoader.loadResources();
			texturePack.loadConsoleMap();
		}
		
		Vem vem = world.getVem();
		ArrayList<Drone> drones = world.getDrones();
		
		// ----------------Get input ------------------------------------------------------------------------------------------------
		// Handle input, only if we are not currently saving
		if(!isSavePending) {
			// Check for movement, only if we are not currently saving
			inputProcessor.processPendingPlayerMovement(vem);
			// Process any key-presses picked up by the InputProcessor
			inputProcessor.feedInputToGamePanel(gamePanel);
		}
		// --------------------------------------------------------------------------------------------------------------------------
		
		// ----------------- LOGIC --------------------------------------------------------------------------------------------------
		

		
		// --------------------------------------------------------------------------------------------------------------------------
	
		// ---------------- Ticks ---------------------------------------------------------------------------------------------------
		vem.tick();
		for(Drone drone : drones) {
			drone.tick();
		}
		// --------------------------------------------------------------------------------------------------------------------------
	
		// ---------------- Drawing -------------------------------------------------------------------------------------------------
		// Eventually we will notice that drawing things randomly will sometimes make sprites overlap
		// where we draw sprites that are in front first. To fix this we could maybe have one big pool
		// where all drawable entites are sorted (might be costly) by y position. Or we could draw each  
		// Y position at a time and iterate over all drawable collections/entites and only draw the ones 
		// at that Y position, this method may be slow if we dont mark the entities that have been drawn.
		
		// First we get Vem's position.
		int vemPosY = vem.getCellY();
		int vemPosX = vem.getCellX();
		// Then we get the offset of vems position. This will be used to offset every other drawable
		float vemOffsetY = vem.getMovementOffsetY();
		float vemOffsetX = vem.getMovementOffsetX();
		
		batch.begin();
		// Draw the screen
		screenDrawer.drawScreen(batch, world, gamePanel, vemPosY, vemPosX, vemOffsetY, vemOffsetX);
		batch.end();
		
		// If a save is currently being carried out, cover screen in 'saving' splash screen 
		if(isSavePending && !savingScreenShown) {
			batch.begin();
			screenDrawer.drawSavingScreen(batch);
			batch.end();
			// Set a flag to show that we have displayed our saving splash screen
			savingScreenShown = true;
			// Even after just paining the save screen we still need to invoke the logger
			logger.log();
			return;
		}
		// -------------------------------------------------------------------------------------------------------------------------
		
		// ----------------PENDING--------------------------------------------------------------------------------------------------
		if(isSavePending) {
			// Invoke the resource saver to save everything
			ResourceSaver.save(world);
			// Unset flag
			isSavePending = false;
			savingScreenShown = false;
		} 
		if(isQuitPending) {
			// Exit the game
			Gdx.app.exit();
		}
		// -------------------------------------------------------------------------------------------------------------------------
		
		// Give some lovin' to our supporting threads
		try { Thread.sleep(5); } catch (InterruptedException e) {}
		// FPS Logging!!
		logger.log();
	}

	/**
	 * Returns the texture pack for use elsewhere in the app
	 * @return
	 */
	public static TexturePack getTexturePack() {
		return texturePack;
	}

	/**
	 * This method is called by the GamePanel When the user has selected the 'Save' option from system menu.
	 * Prompts a save dialog and a subsequent save.
	 */
	public static void setSavePending() {
		isSavePending = true;
	}
	
	/**
	 * This method is called by the GamePanel When the user has selected the 'Quit' option from system menu.
	 */
	public static void setQuitPending() {
		isQuitPending = true;
	}
}
