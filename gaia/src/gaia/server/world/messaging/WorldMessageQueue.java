package gaia.server.world.messaging;

import gaia.server.world.messaging.messages.IWorldMessage;
import gaia.utils.Queue;

/**
 * A message queue populated by a world or a world entity.
 */
public class WorldMessageQueue extends Queue<IWorldMessage> {}
