package gaia.server.world.generation;

import gaia.server.world.placements.factories.IPlacementFactory;
import gaia.server.world.placements.factories.TilledEarthFactory;
import gaia.server.world.placements.factories.TreeFactory;
import gaia.world.PlacementType;
import java.util.HashMap;

/**
 * The collection of placements factories.
 */
public class PlacementFactories {
    /**
     * The map of placements factories, keyed on placements type.
     */
    private static HashMap<PlacementType, IPlacementFactory> factories;

    static {
        factories = new HashMap<PlacementType, IPlacementFactory>()
        {
            {
                this.put(PlacementType.TILLED_EARTH, new TilledEarthFactory());
                this.put(PlacementType.TREE, new TreeFactory());
            }
        };
    }

    /**
     * Get the placements factory for the relevant placements type.
     * @param type The placements type.
     * @return The placements factory for the relevant placements type.
     */
    public static IPlacementFactory getForType(PlacementType type) {
        return factories.get(type);
    }
}
