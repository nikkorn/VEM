package gaia.world.generation;

import java.util.HashMap;
import java.util.Random;
import gaia.lotto.Lotto;
import gaia.world.TileType;

/**
 * Represents a map of tile type lottos keyed on tile type.
 */
public class TileLottos extends HashMap<TileType, Lotto<TileType>> {
	/**
	 * The serial version.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new instance of the TileLottos class.
	 */
	public TileLottos() {
		// Populate this map with tile substitution lottos.
		this.put(TileType.PLAINS, createPlainsLotto());
		this.put(TileType.GRASS, createGrassLotto());
	}
	
	/**
	 * Get a substitution for the specified tile type.
	 * @param type The tile type to substitute.
	 * @param positionSeed The seed of the tile position.
	 * @return The substitution for the specified tile type.
	 */
	public TileType getSubstitutionForTile(TileType type, long positionSeed) {
		// If we have no lotto for this type then simply return it.
		if (!this.containsKey(type)) {
			return type;
		}
		// Get the lotto for this tile type.
		Lotto<TileType> lotto = this.get(type);
		// Reset the lotto RNG as we need a consistent substitution based on the position seed.
		lotto.setRNG(new Random(positionSeed));
		// Return the tile substitution.
		return lotto.draw();
	}

	/**
	 * Create a lotto for the plains tile type.
	 * @return The lotto for the plains tile type.
	 */
	private Lotto<TileType> createPlainsLotto() {
		return new Lotto<TileType>()
			.add(TileType.PLAINS_GRASS_FLOWER, 2)
			.add(TileType.PLAINS_GRASS_TUFT, 4)
			.add(TileType.PLAINS, 30);
	}
	
	/**
	 * Create a lotto for the grass tile type.
	 * @return The lotto for the grass tile type.
	 */
	private Lotto<TileType> createGrassLotto() {
		return new Lotto<TileType>()
			.add(TileType.GRASS_FLOWERS_1, 2)
			.add(TileType.GRASS_FLOWERS_2, 2)
			.add(TileType.GRASS_TUFT_1, 4)
			.add(TileType.GRASS_TUFT_2, 4)
			.add(TileType.GRASS, 35);
	}
}
