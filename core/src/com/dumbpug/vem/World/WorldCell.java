package com.dumbpug.vem.World;

/**
 * A world cell represents a X/Y coordinate. it has a ground level (0-4) and potentially a world object
 * @author Nikolas Howard 
 *
 */
public class WorldCell {
	private int groundLevel = 0;
	private WorldObject wObject = null;
	// A blemish world object is something which is like a world object but is completely unobstructive.
	// It is drawn the same time as the cells ground tile, and before the actual WorldObject
	private WorldObject blemishWorldObject = null;
	
	public WorldCell(int groundLevel) {
		this.groundLevel = groundLevel;
	}
	
	public WorldCell(int groundLevel, WorldObject wObject) {
		this.groundLevel = groundLevel;
		this.setWorldObject(wObject);
	}

	public int getGroundLevel() {
		return groundLevel;
	}

	public WorldObject getWorldObject() {
		return wObject;
	}

	public void setWorldObject(WorldObject wObject) {
		this.wObject = wObject;
	}

	public WorldObject getBlemishWorldObject() {
		return blemishWorldObject;
	}

	public void setBlemishWorldObject(WorldObject blemishWorldObject) {
		this.blemishWorldObject = blemishWorldObject;
	}
}
