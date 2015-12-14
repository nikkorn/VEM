package com.dumbpug.vem.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.vem.Drawable;

public class WorldObject {
	private WorldObjectType type;
	private Drawable drawable;
	
	public WorldObject(WorldObjectType type, Drawable drawableEntity) {
		this.drawable = drawableEntity;
		this.type = type;
	}

	public WorldObjectType getType() {
		return type;
	}

	public void setType(WorldObjectType type) {
		this.type = type;
	}
	
	public void draw(SpriteBatch batch, float posY, float posX) {
		drawable.draw(batch, posY, posX);
	}
	
	public boolean hasDrawable() {
		return drawable != null;
	}
}