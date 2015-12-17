package com.dumbpug.vem.Entities;

import org.mozilla.javascript.Context;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dumbpug.vem.C;
import com.dumbpug.vem.Drawable;
import com.dumbpug.vem.FontPack;
import com.dumbpug.vem.TexturePack;
import com.dumbpug.vem.VEM;
import com.dumbpug.vem.ScriptInterface.DroneIntermediary;
import com.dumbpug.vem.ScriptInterface.DroneScript;
import com.dumbpug.vem.ScriptInterface.Scriptable;
import com.dumbpug.vem.World.WorldFamiliarity;
import com.dumbpug.vem.World.WorldObject;
import com.dumbpug.vem.World.WorldObjectType;

/**
 * 
 * @author Nikolas Howard
 *
 */
public class Drone implements Scriptable, Drawable {
	// Properties
	private String id = "";
	private WorldObject wObject = new WorldObject(WorldObjectType.DRONE, this);
	private boolean canTravelOnWater = false;
	private boolean scriptStopped = false;

	// Position
	private int cell_x;
	private int cell_y;
	
	// Movement
	private boolean stuck = false;
	private volatile int pendingMovementCells = 0;
	private volatile double step = 0;
	private volatile Direction pendingMovementDirection = Direction.NONE;
	private int movementUnit = C.MOVEMENT_UNIT_DIVISION;
	
	// Our Raw Scripts
	String[] scripts = new String[5];
	
	// Speech
	public String speechContents = "";
	public long speechModalDuration = 0; // in milliseconds
	public long intialSpeechTime = 0;    // in milliseconds
	
	// Drawable Text
	private GlyphLayout layout;
	
	// Scripting
	private DroneScript script;
	private int currentScriptIndex = -1;
	
	//--------------------------- Entity Logic ---------------------------
	
	public Drone(int cellX, int cellY, String id, Direction facingDirection, String script1, String script2, String script3, String script4, String script5) {
		setPosition(cellX, cellY);
		this.setId(id);
		// Set the last (or first ever) facing direction of this drone.
		this.pendingMovementDirection = facingDirection;
		// TODO Get scripts from disk
		setScripts(script1, script2, script3, script4, script5);
		// Set up GlyphLayout and font to help with drawing speech modal
		layout = new GlyphLayout();
		// Create a new script object
		script = new DroneScript();
		// Every entity that is manipulated by a script will have its java object (via an intermediary) referenced by the identifier MACHINE.
		script.getGlobalScope().put("MACHINE", script.getGlobalScope(), Context.javaToJS(new DroneIntermediary(script, this), script.getGlobalScope()));
	}
	
	/**
	 * Set the scripts for the drone
	 * @param script1
	 * @param script2
	 * @param script3
	 * @param script4
	 * @param script5
	 */
	private void setScripts(String script1, String script2, String script3, String script4, String script5) {
		scripts[0] = script1;
		scripts[1] = script2;
		scripts[2] = script3;
		scripts[3] = script4;
		scripts[4] = script5;
	}

	/**
	 * Do some processing for the drone.
	 */
	public void tick() {
		// First of all, we need to check whether we have a script running. We may have stopped it then we want the 
		// drone to cease all activities that may have been started by the JS engine (e.g. movement)
		if(scriptStopped && !script.isRunning()){
			// Let us stop our drone from moving
			pendingMovementCells = 0;
			// reset the scriptStopped flag
			scriptStopped = false;
		}
		// Let's move!
		if(pendingMovementCells > 0 || step > 0) {
			// If step is 0 then we're about to move into another cell, check that this is cool.
			// Behaviour will vary here for vem/drones. drones will wait to move forward, vem wont be fucked.
			if(step == 0) {
				switch(pendingMovementDirection) {
				case EAST:
					if(VEM.world.positionIsClear(cell_x + 1, cell_y, canTravelOnWater)) {
						setPosition(cell_x + 1, cell_y);
						step = movementUnit;
						pendingMovementCells--;
						setStuck(false);
					} else {
						setStuck(true);
					}
					break;
				case NORTH:
					if(VEM.world.positionIsClear(cell_x, cell_y + 1, canTravelOnWater)) {
						setPosition(cell_x, cell_y + 1);
						step = movementUnit;
						pendingMovementCells--;
						setStuck(false);
					} else {
						setStuck(true);
					}
					break;
				case SOUTH:
					if(VEM.world.positionIsClear(cell_x, cell_y - 1, canTravelOnWater)) {
						setPosition(cell_x, cell_y - 1);
						step = movementUnit;
						pendingMovementCells--;
						setStuck(false);
					} else {
						setStuck(true);
					}
					break;
				case WEST:
					if(VEM.world.positionIsClear(cell_x - 1, cell_y, canTravelOnWater)) {
						setPosition(cell_x - 1, cell_y);
						step = movementUnit;
						pendingMovementCells--;
						setStuck(false);
					} else {
						setStuck(true);
					}
					break;
				default:
					break;
				}
			} else {
				// Were not starting to move into a new cell, just step.
				step--;
			}
		}
	}
	
	public synchronized void move(Direction dir, int cells) {
		// If we are already travelling, ignore the request
		if(isTravelling()){
			return;
		}
		if(cells != 0) {
			step = 0;
			pendingMovementCells = cells;
			pendingMovementDirection = dir;
		}
	}
	
	public synchronized void stop() {
		pendingMovementCells = 0;
		step = 0;
	}
	
	public boolean isTravelling() {
		return pendingMovementCells > 0 || step > 0;
	}

	public int getCellX() {
		return cell_x;
	}

	public int getCellY() {
		return cell_y;
	}
	
	public Direction getFacingDirection() {
		return this.pendingMovementDirection;
	}
	
	public void setPosition(int newPositionX, int newPositionY) {
		// First we have to remove this drones world object at its old position...
		VEM.world.removeWorldObject(cell_x, cell_y);
		// ... And add it at its new position.
		VEM.world.setWorldObject(wObject, newPositionX, newPositionY);
		// We now need to work out if we have visited this part of the map (familiarity).
		// If not then this are needs to be marked as familiar
		WorldFamiliarity wFamiliarity = VEM.world.getWorldFamiliarity(); 
		int relativeAreaY =  (int) ((C.CONSOLE_MAP_FAMILIARITY_GRID_SIZE) * ((float) newPositionY / C.WORLD_CELL_SIZE));
		int relativeAreaX =  (int) ((C.CONSOLE_MAP_FAMILIARITY_GRID_SIZE) * ((float) newPositionX / C.WORLD_CELL_SIZE));
		if(!wFamiliarity.isCellFamiliar(relativeAreaY, relativeAreaX)) {
			wFamiliarity.markCellAsFamiliar(relativeAreaY, relativeAreaX);
		}
		// Actually set the new position
		cell_x = newPositionX;
		cell_y = newPositionY;
	}

	public double getStep() {
		return step;
	}

	public void setStep(double step) {
		this.step = step;
	}

	public boolean isStuck() {
		return stuck;
	}

	private void setStuck(boolean stuck) {
		this.stuck = stuck;
	}
	
	public void setSpeed(int movementUnit){
		this.movementUnit = movementUnit;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Return the raw script code for the script at position scriptIndex
	 * @param scriptIndex
	 * @return
	 */
	public String getScriptCode(int scriptIndex) {
		return scripts[scriptIndex];
	}
	
	/**
	 * Returns the index of running script.
	 * @return index of running script, -1 if none are running
	 */
	public int getRunningScriptIndex() {
		if(!script.isRunning()) {
			return -1;
		} else {
			return currentScriptIndex;
		}
	}
	
	public void draw(SpriteBatch batch, float pY, float pX) {
		TextureRegion txRegion = null;
		TexturePack texturePack = VEM.getTexturePack();
		TextureRegion[][] droneTextureRegions;
		float posX = pX;
		float posY = pY;
		
		// Calculate our actual position in relation to motion step
		if(step != 0) {
			switch(pendingMovementDirection) {
			case EAST:
				posX -= step * (C.CELL_UNIT / movementUnit);
				break;
			case NORTH:
				posY -= step * (C.CELL_UNIT / movementUnit);
				break;
			case SOUTH:
				posY += step * (C.CELL_UNIT / movementUnit);
				break;
			case WEST:
				posX += step * (C.CELL_UNIT / movementUnit);
				break;
			default:
				break;
			}
		} 
		
		// The drone will look different if it is running a script at the time.
		if(script.isRunning()) {
			droneTextureRegions = texturePack.droneTextureRegions_Running;
		} else {
			droneTextureRegions = texturePack.droneTextureRegions_Stopped;
		}
		
		// Set the texture to draw depending on direction
		switch(pendingMovementDirection) {
		case EAST:
			txRegion = droneTextureRegions[0][2];
			break;
		case NORTH:
			txRegion = droneTextureRegions[0][1];
			break;
		case SOUTH:
			txRegion = droneTextureRegions[0][0];
			break;
		case WEST:
			txRegion = droneTextureRegions[0][3];
			break;
		default:
			txRegion = droneTextureRegions[0][0];
			break;
		}
		
		// Draw the actual drone
		batch.draw(txRegion, posX, posY, (float) C.CELL_UNIT, (float) C.CELL_UNIT);
		
		// Draw the speech modal (if we are currently saying anything)
		if(!speechContents.isEmpty()){
			if((System.currentTimeMillis() - intialSpeechTime) <= speechModalDuration){
				BitmapFont font = FontPack.getFont(FontPack.FontType.SPEECH_BOX_FONT);
				layout.setText(font, speechContents);
				batch.draw(texturePack.modalBackgroundTexture, (float) (posX - ((C.MODAL_BORDER/2) + (layout.width/2)) + (C.CELL_UNIT/2)),(float) ((posY + (C.CELL_UNIT + ((layout.height + C.MODAL_BORDER)/2))) - (C.MODAL_BORDER/2)), layout.width + C.MODAL_BORDER, layout.height + C.MODAL_BORDER);
				font.draw(batch, speechContents, (float) (posX - (layout.width/2) + (C.CELL_UNIT/2)), (float) ((posY + (C.CELL_UNIT + ((layout.height + C.MODAL_BORDER)/2))) + layout.height));
			} else {
				speechContents = "";
				intialSpeechTime = 0;
				speechModalDuration = 0;
			}
		}
	}

	//----------------------------------------------------------------------
	
	@Override
	public void runScript() {
		script.run();
	}
	
	@Override
	public void stopScript() {
		if(script.isRunning()) {
			script.stop();
			// Set a flag to show that the running script was recently stopped
			scriptStopped = true;
		}
	}

	@Override
	public boolean isCurrentlyRunningScript() {
		return script.isRunning();
	}

	@Override
	public void setScript(int index) {
		currentScriptIndex = index;
		script.setScript(scripts[index]);
	}
}
