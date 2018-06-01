package server.world.generation;

import java.util.HashMap;
import java.util.Random;
import lotto.Lotto;
import server.world.TileType;

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
	 * @return The lotto for he plains tile type.
	 */
	private Lotto<TileType> createPlainsLotto() {
		return new Lotto<TileType>()
			.add(TileType.PLAINS_GRASS_FLOWER, 2)
			.add(TileType.PLAINS_GRASS_TUFT, 4)
			.add(TileType.PLAINS, 15);
	}
}
