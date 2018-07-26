package gaia.server.networking;

import gaia.server.engine.Request;

/**
 * Handles converting messages received from clients to engine requests.
 */
public class ClientMessageConverter {
	
	/**
	 *  Convert a client message into a request to be processed by an engine.
	 * @param message The client message to convert into a request.
	 * @return The request to be processed by an engine.
	 */
	public static Request toEngineRequest(ClientMessage message) {
		return null;
	}
}
