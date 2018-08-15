package gaia.server.world.messaging;

import gaia.server.world.messaging.messages.IWorldMessage;

/**
 * Represents a world message processor.
 */
public interface IWorldMessageProcessor {

	/**
	 * Process the world message.
	 * @param message The message to process.
	 */
	void process(IWorldMessage message);
}
