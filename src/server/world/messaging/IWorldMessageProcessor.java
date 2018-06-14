package server.world.messaging;

import server.world.messaging.messages.IWorldMessage;

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
