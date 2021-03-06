package gaia.server.world.placements.builders;

import java.util.Random;
import org.json.JSONObject;
import gaia.server.world.placements.IModifiablePlacement;
import gaia.server.world.placements.IPlacementActions;
import gaia.server.world.placements.Priority;
import gaia.server.world.placements.state.IPlacementState;
import gaia.server.world.placements.state.TilledEarthState;
import gaia.time.Time;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementUnderlay;
import gaia.world.items.ItemType;
import gaia.world.items.container.Container;
import gaia.world.items.container.ContainerFactory;

/**
 * Builder for creating a tilled earth placements.
 */
public class TilledEarthBuilder implements IPlacementBuilder {

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
	public IPlacementActions getActions() {
		return new IPlacementActions() {
			@Override
			public void onServerTick(IModifiablePlacement placement) {
				// ServerConsole.writeInfo("Just growing some plants at 100% capacity!");
			}

			@Override
			public void onTimeUpdate(IModifiablePlacement placement, Time time) {
				// ServerConsole.writeInfo("Just growing some plants, oh did the time change?");
			}

			@Override
			public ItemType onInteraction(IModifiablePlacement placement, ItemType item) {
				// ServerConsole.writeInfo("Just growing some plants, did you use that item on me? I will keep it!");
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
		return ContainerFactory.create(3);
	}
}
