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
	 * The container bar texture.
	 */
	public static Texture CONTAINER_BAR;
	/**
	 * The item bar active slot texture.
	 */
	public static Texture ITEM_BAR_ACTIVE_SLOT;
	/**
	 * The item bar inactive slot texture.
	 */
	public static Texture ITEM_BAR_INACTIVE_SLOT;
	/**
	 * The inventory bar active slot texture.
	 */
	public static Texture INVENTORY_BAR_INACTIVE_SLOT;
	/**
	 * The disconnect texture.
	 */
	public static Texture DISCONNECT;
	
	static {
		INVENTORY_BAR          = new Texture(Gdx.files.internal("images/hud/INVENTORY_BAR.png"));
		CONTAINER_BAR          = new Texture(Gdx.files.internal("images/hud/CONTAINER_BAR.png"));
		ITEM_BAR_ACTIVE_SLOT   = new Texture(Gdx.files.internal("images/hud/ITEM_BAR_ACTIVE_SLOT.png"));
		ITEM_BAR_INACTIVE_SLOT = new Texture(Gdx.files.internal("images/hud/ITEM_BAR_INACTIVE_SLOT.png"));
		DISCONNECT             = new Texture(Gdx.files.internal("images/hud/DISCONNECT.png"));
	}
}
