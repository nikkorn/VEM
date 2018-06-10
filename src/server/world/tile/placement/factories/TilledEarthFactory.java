package server.world.tile.placement.factories;

import java.util.Random;
import org.json.JSONObject;
import server.ServerConsole;
import server.items.ItemType;
import server.world.chunk.ChunkFactory;
import server.world.container.Container;
import server.world.messaging.WorldMessageQueue;
import server.world.tile.placement.IPlacementAction;
import server.world.tile.placement.Priority;
import server.world.tile.placement.state.IPlacementState;
import server.world.tile.placement.state.TilledEarthState;
import server.world.time.Time;

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
			public void onServerTick(IPlacementState state, Container container, WorldMessageQueue worldMessageQueue) {
				ServerConsole.writeInfo("Just growing some plants at 100% capacity!");
			}

			@Override
			public void onTimeUpdate(Time time, IPlacementState state, Container container, WorldMessageQueue worldMessageQueue) {
				ServerConsole.writeInfo("Just growing some plants, oh did the time change?");
			}

			@Override
			public ItemType onInteraction(IPlacementState state, Container container, ItemType item, WorldMessageQueue worldMessageQueue) {
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
	public Container getInitialContainer(Random chunkRng) {
		return ChunkFactory.createContainer(3);
	}
}
