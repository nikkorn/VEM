package com.dumbpug.gaia_libgdx;

import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import gaia.world.TileType;

/**
 * Holds tile resources.
 */
public class TileResources {
	/**
	 * The tile texture map.
	 */
	private HashMap<TileType, Texture> tileTextures = new HashMap<TileType, Texture>();
	
	/**
	 * Create a new instance of the TileResources class.
	 */
	public TileResources() {
		for (TileType type : TileType.values()) {
			// Get the texture for this tile.
			Texture tileTexture = new Texture(Gdx.files.internal("images/world/tiles/" + type.toString() + ".png"));
			// Add the tile texture to our map.
			tileTextures.put(type, tileTexture);
		}
	}
	
	/**
	 * Get the texture for a tile type.
	 * @param type The tile type.
	 * @return The texture for a tile type.
	 */
	public Texture get(TileType type) {
		return this.tileTextures.get(type);
	}
}
