package com.dumbpug.gaia_libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Holds player resources.
 */
public class PlayerResources {
	/**
	 * The player texture.
	 */
	public static Texture PLAYER_TEXTURE;
	
	static {
		PLAYER_TEXTURE = new Texture(Gdx.files.internal("images/world/player/player.png"));
	}
}
