package com.dumbpug.gaia_libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import gaia.world.Direction;

/**
 * Holds player resources.
 */
public class PlayerResources {
	/** 
	 * The player textures.
	 */
	private static Texture PLAYER_TEXTURE_UP;
	private static Texture PLAYER_TEXTURE_DOWN;
	private static Texture PLAYER_TEXTURE_LEFT;
	private static Texture PLAYER_TEXTURE_RIGHT;
	
	static {
		PLAYER_TEXTURE_UP    = new Texture(Gdx.files.internal("images/world/player/player_up.png"));
		PLAYER_TEXTURE_DOWN  = new Texture(Gdx.files.internal("images/world/player/player_down.png"));
		PLAYER_TEXTURE_LEFT  = new Texture(Gdx.files.internal("images/world/player/player_left.png"));
		PLAYER_TEXTURE_RIGHT = new Texture(Gdx.files.internal("images/world/player/player_right.png"));
	}
	
	/**
	 * Get the player texture for the specified facing direction.
	 * @param direction The facing direction.
	 */
	public static Texture getPlayerTexture(Direction direction) {
		switch (direction) {
			case DOWN:
				return PLAYER_TEXTURE_DOWN;
			case LEFT:
				return PLAYER_TEXTURE_LEFT;
			case RIGHT:
				return PLAYER_TEXTURE_RIGHT;
			case UP:
				return PLAYER_TEXTURE_UP;
			default:
				return PLAYER_TEXTURE_DOWN;
		}
	}
}
