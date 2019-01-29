package gaia.server.networking;

import gaia.networking.IMessage;
import gaia.networking.messages.MessageIdentifier;
import gaia.networking.messages.MovePlayer;
import gaia.networking.messages.SwapContainerAndInventoryItem;
import gaia.networking.messages.UseInventoryItem;
import gaia.server.engine.Request;
import gaia.server.world.players.requests.MoveRequest;
import gaia.server.world.players.requests.SwapContainerAndInventoryItemRequest;
import gaia.server.world.players.requests.UseItemRequest;

/**
 * Represents a message sent from a client that includes the player id of the client.
 */
public class ClientMessage {
	/**
	 *  The player id of the client.
	 */
	private String playerId;
	/**
	 * The message sent by the client.
	 */
	private IMessage message;
	
	/**
	 * Create a new instance of the ClientMessage class.
	 * @param playerId The player id of the client.
	 * @param message The message sent by the client.
	 */
	public ClientMessage(String playerId, IMessage message) {
		this.playerId = playerId;
		this.message  = message;
	}
	
	/**
	 *  Get the player id of the client.
	 * @return The player id of the client.
	 */
	public String getPlayerId() {
		return this.playerId;
	}
	
	/**
	 * Get the message sent by the client.
	 * @return The message sent by the client.
	 */
	public IMessage getMessage() {
		return this.message;
	}
	
	/**
	 * Convert a client message into a request to be processed by an engine.
	 * @param message The client message to convert into a request.
	 * @return The request to be processed by an engine.
	 */
	public static Request toEngineRequest(ClientMessage message) {
		switch (message.getMessage().getTypeId()) {

			case MessageIdentifier.MOVE_PLAYER:
				return new MoveRequest(message.getPlayerId(), ((MovePlayer)message.getMessage()).getDirection());

			case MessageIdentifier.USE_INVENTORY_ITEM:
				return new UseItemRequest(message.getPlayerId(), ((UseInventoryItem)message.getMessage()).getSlotIndex(), ((UseInventoryItem)message.getMessage()).getExpectedItemType());
			
			case MessageIdentifier.SWAP_CONTAINER_AND_INVENTORY_ITEM:
				SwapContainerAndInventoryItem swapMessage = (SwapContainerAndInventoryItem) message.getMessage(); 
				return new SwapContainerAndInventoryItemRequest(message.getPlayerId(), swapMessage.getContainerSlotIndex(), swapMessage.getExpectedContainerItem(), swapMessage.getInventorySlotIndex(), swapMessage.getExpectedInventoryItem());

			default:
				throw new RuntimeException("error: cannot convert client message with id '" + message.getMessage().getTypeId() + "' to engine request.");
		}
	}
}
