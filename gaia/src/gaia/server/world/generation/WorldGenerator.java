package gaia.server.world.generation;

import gaia.Constants;
import gaia.world.TileType;
import gaia.server.world.placements.Placement;
import gaia.server.world.placements.Placements;
import gaia.server.world.placements.builders.IPlacementBuilder;
import gaia.server.world.placements.builders.PlacementBuilders;
import gaia.world.PlacementType;
import gaia.world.generation.TileGenerator;

import java.util.Random;

/**
 * Generates world tiles and placements based on noise and RNG.
 */
public class WorldGenerator extends TileGenerator {
    /**
     * The world placements lottos.
     * These lottos are used in determining whether any placmements should be placed at a tile position.
     */
    private PlacementLottos placementLottos = new PlacementLottos();

    /**
     * Creates a new instance of the WorldGenerator class.
     * @param seed The world seed.
     */
    public WorldGenerator(long seed) {
        super(seed);
    }

    /**
     * Create the collection that will hold the placements for the chunk.
     * @param tiles The static world tiles for the chunk.
     * @param x The x position of the chunk.
     * @param y The y position of the chunk.
     * @return The collection holding the placements for the chunk.
     */
    public Placements getChunkPlacements(TileType[][] tiles, int x, int y) {
        // Create the collection which will hold any generated placements.
        Placements placements = new Placements();
        // Create the rng to use in creating all the placements for this chunk.
        Random chunkRng = new Random(this.getPositionSeed(x, y));
        // For each tile in this chunk we will try to create a placements, based on the tile type.
        for (short tileX = 0; tileX < Constants.WORLD_CHUNK_SIZE; tileX++) {
            for (short tileY = 0; tileY < Constants.WORLD_CHUNK_SIZE; tileY++) {
                // Try to generate a placements for this tile position, this could be null if we didn't generate one.
                PlacementType placementType = this.placementLottos.getPlacementForTile(tiles[tileX][tileY], chunkRng);
                // Create the placements if we generated one.
                if (placementType != PlacementType.NONE) {
                    // Create and add our newly generated placements.
                    // Passing the RNG means that we can generate consistent placements state!
                    placements.add(createPlacement(placementType, tileX, tileY, chunkRng));
                }
            }
        }
        // Return the generated placements.
        return placements;
    }

    /**
     * Create a placements of the specified type in its default state.
     * @param type The placements type.
     * @param x The placements x position within its parent chunk.
     * @param y The placements y position within its parent chunk.
     * @param chunkRng The rng to use in creating all the placements for a chunk.
     * @return The placements.
     */
    private static Placement createPlacement(PlacementType type, short x, short y, Random chunkRng) {
        // Create the new placements.
        Placement placement = new Placement(type, x, y);
        // Get the relevant placements factory.
        IPlacementBuilder placementFactory = PlacementBuilders.getForType(type);
        // Create the placements state.
        placement.setState(placementFactory.createState(chunkRng));
        // Create the action for this placements.
        placement.setActions(placementFactory.getActions());
        // Set the initial priority for this placements.
        placement.setPriority(placementFactory.getInitialPriority());
        // Set the initial underlay for this placements.
        placement.setUnderlay(placementFactory.getInitialUnderlay());
        // Set the initial overlay for this placements.
        placement.setOverlay(placementFactory.getInitialOverlay());
        // Set the container for this placements.
        placement.setContainer(placementFactory.getContainer(chunkRng));
        // Return the new placements.
        return placement;
    }
}
