package com.dumbpug.gaia_libgdx;

import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementUnderlay;

/**
 * Holds placement resources.
 */
public class PlacementResources {
	/**
	 * The underlay texture map.
	 */
	private HashMap<PlacementUnderlay, Texture> underlayTextures = new HashMap<PlacementUnderlay, Texture>();
	/**
	 * The overlay texture map.
	 */
	private HashMap<PlacementOverlay, Texture> overlayTextures = new HashMap<PlacementOverlay, Texture>();
	
	/**
	 * Create a new instance of the PlacementResources class.
	 */
	public PlacementResources() {
		// Get the placement underlay textures.
		for (PlacementUnderlay underlay : PlacementUnderlay.values()) {
			// Get the texture for this underlay.
			Texture tileTexture = new Texture(Gdx.files.internal("images/world/underlays/" + underlay.toString() + ".png"));
			// Add the underlay texture to our map.
			underlayTextures.put(underlay, tileTexture);
		}
		// Get the placement overlay textures.
		for (PlacementOverlay overlay : PlacementOverlay.values()) {
			// Get the texture for this overlay.
			Texture tileTexture = new Texture(Gdx.files.internal("images/world/overlays/" + overlay.toString() + ".png"));
			// Add the overlay texture to our map.
			overlayTextures.put(overlay, tileTexture);
		}
	}
	
	/**
	 * Get the texture for an underlay.
	 * @param underlay The tile underlay.
	 * @return The texture for an underlay.
	 */
	public Texture getUnderlayTexture(PlacementUnderlay underlay) {
		return this.underlayTextures.get(underlay);
	}
	
	/**
	 * Get the texture for an overlay.
	 * @param overlay The tile overlay.
	 * @return The texture for an overlay.
	 */
	public Texture getOverlayTexture(PlacementOverlay overlay) {
		return this.overlayTextures.get(overlay);
	}
}
