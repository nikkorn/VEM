package com.dumbpug.gaia_libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Holds HUD resources
 */
public class HUDResources {
	/**
	 * The inventory bar texture.
	 */
	public static Texture INVENTORY_BAR;
	/**
	 * The inventory bar active slot texture.
	 */
	public static Texture INVENTORY_BAR_ACTIVE_SLOT;
	/**
	 * The disconnect texture.
	 */
	public static Texture DISCONNECT;
	
	static {
		INVENTORY_BAR             = new Texture(Gdx.files.internal("images/hud/INVENTORY_BAR.png"));
		INVENTORY_BAR_ACTIVE_SLOT = new Texture(Gdx.files.internal("images/hud/INVENTORY_BAR_ACTIVE_SLOT.png"));
		DISCONNECT                = new Texture(Gdx.files.internal("images/hud/DISCONNECT.png"));
	}
}
