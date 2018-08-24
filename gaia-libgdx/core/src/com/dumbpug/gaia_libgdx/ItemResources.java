package com.dumbpug.gaia_libgdx;

import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import gaia.world.items.ItemType;

/**
 * Holds item resources.
 */
public class ItemResources {
	/**
	 * The item texture map.
	 */
	private HashMap<ItemType, Texture> itemTextures = new HashMap<ItemType, Texture>();
	
	/**
	 * Create a new instance of the ItemResources class.
	 */
	public ItemResources() {
		// Get the item textures.
		for (ItemType item : ItemType.values()) {
			// Get the texture for this item.
			Texture itemTexture = null;
			// Try to get the texture. It may not exist yet so just use NONE if that is the case.
			try {
				itemTexture = new Texture(Gdx.files.internal("images/items/" + item.toString() + ".png"));
			} catch (Exception e) {
				itemTexture = new Texture(Gdx.files.internal("images/items/NONE.png"));
			}
			// Add the item texture to our map.
			itemTextures.put(item, itemTexture);
		}
	}
	
	/**
	 * Get the texture for an item.
	 * @param item The item.
	 * @return The texture for an item.
	 */
	public Texture getItemTexture(ItemType item) {
		return this.itemTextures.get(item);
	}
}
