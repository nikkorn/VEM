package com.dumbpug.vem.Entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dumbpug.vem.C;
import com.dumbpug.vem.Drawable;
import com.dumbpug.vem.TexturePack;
import com.dumbpug.vem.VEM;
import com.dumbpug.vem.World.WorldFamiliarity;
import com.dumbpug.vem.World.WorldObject;
import com.dumbpug.vem.World.WorldObjectType;

public class Vem implements Drawable {
	// Properties
	private WorldObject wObject = new WorldObject(WorldObjectType.VEM, this);
	private boolean canTravelOnWater = false;
	
	private double step = 0;
	private int cell_x;
	private int cell_y;
	private boolean stuck = false;
	
	private int pendingMovementCells = 0;
	private volatile Direction pendingMovementDirection = Direction.NONE;
	
	public Vem(int posY, int posX, Direction facingDirection) {
		setPosition(posX, posY);
		pendingMovementDirection = facingDirection;
	}
	
	public void tick() {
		// Let's move!
		if(pendingMovementCells > 0 || step > 0) {
			// if step is 0 then we're about to move into another cell, check that this is cool.
			// Behaviour will vary here for vem/drones. drones will wait to move forward, vem wont be fucked.
			if(step == 0) {
				switch(pendingMovementDirection) {
				case EAST:
					if(VEM.world.positionIsClear(cell_x + 1, cell_y, canTravelOnWater)) {
						setPosition(cell_x + 1, cell_y);
						step = C.MOVEMENT_UNIT_DIVISION;
						pendingMovementCells--;
						setStuck(false);
					} else {
						setStuck(true);
					}
					break;
				case NORTH:
					if(VEM.world.positionIsClear(cell_x, cell_y + 1, canTravelOnWater)) {
						setPosition(cell_x, cell_y + 1);
						step = C.MOVEMENT_UNIT_DIVISION;
						pendingMovementCells--;
						setStuck(false);
					} else {
						setStuck(true);
					}
					break;
				case SOUTH:
					if(VEM.world.positionIsClear(cell_x, cell_y - 1, canTravelOnWater)) {
						setPosition(cell_x, cell_y - 1);
						step = C.MOVEMENT_UNIT_DIVISION;
						pendingMovementCells--;
						setStuck(false);
					} else {
						setStuck(true);
					}
					break;
				case WEST:
					if(VEM.world.positionIsClear(cell_x - 1, cell_y, canTravelOnWater)) {
						setPosition(cell_x - 1, cell_y);
						step = C.MOVEMENT_UNIT_DIVISION;
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
	
	public void move(Direction dir, int cells) {
		// If we are already travelling, ignore the request
		if(isTravelling()){
			return;
		}
		// Let us face the direction we want to go in, even if we dont have to move any spaces
		pendingMovementDirection = dir;
		// If we have cells to move to then let us handle that now.
		if(cells != 0) {
			switch(dir) {
			case EAST:
				if(VEM.world.positionIsClear(cell_x + 1, cell_y, canTravelOnWater)) {
					setPosition(cell_x + 1, cell_y);
					step = C.MOVEMENT_UNIT_DIVISION;
					pendingMovementCells = cells - 1;
				}
				break;
			case NORTH:
				if(VEM.world.positionIsClear(cell_x, cell_y + 1, canTravelOnWater)) {
					setPosition(cell_x, cell_y + 1);
					step = C.MOVEMENT_UNIT_DIVISION;
					pendingMovementCells = cells - 1;
				}
				break;
			case SOUTH:
				if(VEM.world.positionIsClear(cell_x, cell_y - 1, canTravelOnWater)) {
					setPosition(cell_x, cell_y - 1);
					step = C.MOVEMENT_UNIT_DIVISION;
					pendingMovementCells = cells - 1;
				}
				break;
			case WEST:
				if(VEM.world.positionIsClear(cell_x - 1, cell_y, canTravelOnWater)) {
					setPosition(cell_x - 1, cell_y);
					step = C.MOVEMENT_UNIT_DIVISION;
					pendingMovementCells = cells - 1;
				}
				break;
			default:
				break;
			}
		}
	}
	
	public boolean isTravelling() {
		return pendingMovementCells > 0 || step > 0;
	}
	
	public void setPosition(int newPositionX, int newPositionY) {
		// First we have to remove this drones world object at its old position...
		VEM.world.removeWorldObject(cell_x, cell_y);
		// ... And add int at its new position.
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

	public int getCellX() {
		return cell_x;
	}

	public int getCellY() {
		return cell_y;
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
	
	public void draw(SpriteBatch batch, float pX, float pY) {
		TextureRegion txRegion = null;
		TexturePack texturePack = VEM.getTexturePack();
		float posX = (float) ((((int)C.WIDTH/2) * C.CELL_UNIT));
		float posY = (float) ((((int)C.HEIGHT/2) * C.CELL_UNIT));
		
		// Set the texture to draw depending on direction
		switch(pendingMovementDirection) {
		case EAST:
			txRegion = texturePack.vemTextureRegions[0][2];
			break;
		case NORTH:
			txRegion = texturePack.vemTextureRegions[0][1];
			break;
		case SOUTH:
			txRegion = texturePack.vemTextureRegions[0][0];
			break;
		case WEST:
			txRegion = texturePack.vemTextureRegions[0][3];
			break;
		default:
			txRegion = texturePack.vemTextureRegions[0][0];
			break;
		}
		
		batch.draw(txRegion, posX , posY, (float) C.CELL_UNIT, (float) C.CELL_UNIT);
	}
	
	public float getMovementOffsetX() {
		float posX = 0;
		if(step != 0) {
			switch(pendingMovementDirection) {
			case EAST:
				posX -= step * (C.CELL_UNIT / C.MOVEMENT_UNIT_DIVISION);
				break;
			case WEST:
				posX += step * (C.CELL_UNIT / C.MOVEMENT_UNIT_DIVISION);
				break;
			default:
				break;
			}
		} 
		return posX;
	}
	
	public float getMovementOffsetY() {
		float posY = 0;
		if(step != 0) {
			switch(pendingMovementDirection) {
			case NORTH:
				posY -= step * (C.CELL_UNIT / C.MOVEMENT_UNIT_DIVISION);
				break;
			case SOUTH:
				posY += step * (C.CELL_UNIT / C.MOVEMENT_UNIT_DIVISION);
				break;
			default:
				break;
			}
		} 
		return posY;
	}
	
	public Direction getFacingDirection() {
		return this.pendingMovementDirection;
	}
}