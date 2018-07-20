package gaia.server.world.tile.placement.factories;

import java.util.Random;
import org.json.JSONObject;

import gaia.server.ServerConsole;
import gaia.server.items.ItemType;
import gaia.server.world.chunk.ChunkFactory;
import gaia.server.world.container.Container;
import gaia.server.world.tile.placement.IModifiablePlacement;
import gaia.server.world.tile.placement.IPlacementAction;
import gaia.server.world.tile.placement.PlacementOverlay;
import gaia.server.world.tile.placement.PlacementUnderlay;
import gaia.server.world.tile.placement.Priority;
import gaia.server.world.tile.placement.state.IPlacementState;
import gaia.server.world.tile.placement.state.TilledEarthState;
import gaia.server.world.time.Time;

/**
 * Factory for creating a tilled earth placement.
 */
public class TilledEarthFactory implements IPlacementFactory {

	@Override
	public IPlacementState createState(Random chunkRng) {
		return new TilledEarthState();
	}

	@Override
	public IPlacementState createState(JSONObject stateJSON) {
		// TODO Create state based on JSON!
		return new TilledEarthState();
	}
	
	@Override
	public IPlacementAction getAction() {
		return new IPlacementAction() {
			@Override
			public void onServerTick(IModifiablePlacement placement) {
				ServerConsole.writeInfo("Just growing some plants at 100% capacity!");
			}

			@Override
			public void onTimeUpdate(IModifiablePlacement placement, Time time) {
				ServerConsole.writeInfo("Just growing some plants, oh did the time change?");
			}

			@Override
			public ItemType onInteraction(IModifiablePlacement placement, ItemType item) {
				ServerConsole.writeInfo("Just growing some plants, did you use that item on me? I will keep it!");
				return ItemType.NONE;
			}
		};
	}

	@Override
	public Priority getInitialPriority() {
		return Priority.HIGH;
	}
	
	@Override
	public PlacementUnderlay getInitialUnderlay() {
		return PlacementUnderlay.TILLED_EARTH;
	}

	@Override
	public PlacementOverlay getInitialOverlay() {
		return PlacementOverlay.NONE;
	}

	@Override
	public Container getContainer(Random chunkRng) {
		return ChunkFactory.createContainer(3);
	}
}
