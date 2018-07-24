package gaia.server.networking;

import gaia.server.ServerConsole;
import gaia.server.world.messaging.IWorldMessageProcessor;
import gaia.server.world.messaging.messages.IWorldMessage;

/**
 * Processes world messages to be forwarded to connected clients.
 */
public class ClientWorldMessageProcessor implements IWorldMessageProcessor {
	/**
	 * The connected client manager.
	 */
	private ClientProxyManager connectedClientManager;
	
	/**
	 * Create a new instance of the ClientWorldMessageProcessor class.
	 * @param connectedClientManager The connected client manager.
	 */
	public ClientWorldMessageProcessor(ClientProxyManager connectedClientManager) {
		this.connectedClientManager = connectedClientManager;
	}

	/**
	 * Process the world message.
	 * @param message The message to process.
	 */
	@Override
	public void process(IWorldMessage message) {
		ServerConsole.writeInfo("We are processing a world message of type " + message.getMessageType() + "!");
	}
}
