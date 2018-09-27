package gaia.server.world.placements.builders;

import gaia.world.PlacementType;
import java.util.HashMap;

/**
 * The collection of placements builders.
 */
public class PlacementBuilders {
    /**
     * The map of placement builders, keyed on placement type.
     */
    private static HashMap<PlacementType, IPlacementBuilder> builders;

    static {
    	builders = new HashMap<PlacementType, IPlacementBuilder>() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
                this.put(PlacementType.TILLED_EARTH, new TilledEarthBuilder());
                this.put(PlacementType.TREE, new TreeBuilder());
            }
        };
    }

    /**
     * Get the placement builder for the relevant placement type.
     * @param type The placement type.
     * @return The placement builder for the relevant placement type.
     */
    public static IPlacementBuilder getForType(PlacementType type) {
        return builders.get(type);
    }
}
