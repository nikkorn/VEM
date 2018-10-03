package gaia.server.world.placements.factories;

import gaia.world.PlacementType;
import java.util.HashMap;

/**
 * The collection of placements builders.
 */
public class PlacementFactories {
    /**
     * The map of placement factories, keyed on placement type.
     */
    private static HashMap<PlacementType, IPlacementFactory> factories;

    static {
    	factories = new HashMap<PlacementType, IPlacementFactory>() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
                this.put(PlacementType.TILLED_EARTH, new TilledEarthFactory());
                this.put(PlacementType.TREE, new TreeFactory());
            }
        };
    }

    /**
     * Get the placement factory for the relevant placement type.
     * @param type The placement type.
     * @return The placement factory for the relevant placement type.
     */
    public static IPlacementFactory getFactoryForType(PlacementType type) {
        return factories.get(type);
    }
}
