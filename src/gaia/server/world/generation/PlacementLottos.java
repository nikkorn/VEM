package gaia.server.world.generation;

import java.util.HashMap;
import java.util.Random;
import gaia.lotto.Lotto;
import gaia.server.world.tile.TileType;
import gaia.world.PlacementType;

/**
 * Represents a map of lottos are used in determining whether any placmements should be placed at a tile position.
 */
public class PlacementLottos extends HashMap<TileType, Lotto<PlacementType>> {
	/**
	 * The serial version.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new instance of the PlacementLottos class.
	 */
	public PlacementLottos() {
		this.put(TileType.FOREST, createForestLotto());
	}
	
	/**
	 * Get a placement for the specified tile type.
	 * @param type The tile type to get a placement for.
	 * @param random The rng to use in picking winners.
	 * @return The placement for the specified tile type.
	 */
	public PlacementType getPlacementForTile(TileType type, Random random) {
		// If we have no lotto for this type then simply return no placement as we wont be creating one.
		if (!this.containsKey(type)) {
			return PlacementType.NONE;
		}
		// Get the lotto for this tile type.
		Lotto<PlacementType> lotto = this.get(type);
		// Reset the lotto RNG as we need a consistent result based on the rng.
		lotto.setRNG(random);
		// Return the placement.
		return lotto.draw();
	}

	/**
	 * Create a lotto for the forest tile type.
	 * @return The lotto for the forest tile type.
	 */
	private Lotto<PlacementType> createForestLotto() {
		return new Lotto<PlacementType>()
				.add(PlacementType.TREE)
				.add(PlacementType.NONE, 25);
	}
}
