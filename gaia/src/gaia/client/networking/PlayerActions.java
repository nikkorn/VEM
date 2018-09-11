package gaia.client.networking;

import java.io.IOException;
import gaia.client.gamestate.ServerState;
import gaia.client.gamestate.players.Player;
import gaia.networking.IMessage;
import gaia.networking.MessageOutputStream;
import gaia.networking.messages.MovePlayer;
import gaia.networking.messages.UseInventoryItem;
import gaia.world.Direction;
import gaia.world.items.Inventory;
import gaia.world.items.ItemTarget;
import gaia.world.items.ItemType;

/**
 * Player actions.
 */
public class PlayerActions {
    /**
     * The message output stream.
     */
    private MessageOutputStream messageOutputStream;
    /**
     * The server state.
     */
    private ServerState serverState;

    /**
     * Create a new instance of the PlayerActions class.
     * @param messageOutputStream The message output stream.
     * @param serverState The server state.
     */
    public PlayerActions(MessageOutputStream messageOutputStream, ServerState serverState) {
        this.messageOutputStream = messageOutputStream;
        this.serverState         = serverState;
    }

    /**
     * Move the player in the specified direction.
     * The player will not be able to move to this position if it is blocked.
     * @param direction The direction to move in.
     */
    public void move(Direction direction) {
    	// Refresh the server state to ensure we have the latest placement info.
    	this.serverState.refresh();
    	// Get the client's player.
    	Player player = serverState.getPlayers().getClientsPlayer();
    	// TODO Do nothing if there is a non-walkable placement in the way of the player.
    	// TODO Do nothing if there is another player in the way of the player.
    	// There is nothing to do if the player is already moving.
    	if (player.isWalking()) {
    		return;
    	}
    	// Start our player moving right away.
    	player.move(direction);
    	// Make a request to the server to move in the same direction. Hopefully the server will
    	// respond with a 'PlayerMoved' message with the details of the position we are moving to.
    	// However, the server may also return a 'PlayerMoved' with details of a positon that is not
    	// the position we were targeting. This is OK though, we will just move the player there instead. 
    	send(new MovePlayer(direction));
    }
    
    /**
     * Use the inventory item at the specified slot.
     * @param slot The inventory slot index.
     */
    public void useItem(int slot) {
    	// Do nothing if the slot index is invalid.
    	if (!Inventory.isValidSlotIndex(slot)) {
    		throw new RuntimeException("Invalid inventory slot index: " + slot);
    	}
    	// Refresh the server state to ensure we have the latest inventory slots.
    	this.serverState.refresh();
    	// Get the item at the specified index.
    	ItemType itemType = serverState.getPlayers().getClientsPlayer().getInventory().get(slot);
    	// We do not want to do anything if there is no target of use for the item in the slot.
    	if (itemType.getTarget() == ItemTarget.NONE) {
    		return;
    	}
    	// Send a message to the server asking to use the item in the specified slot.
    	send(new UseInventoryItem(slot, itemType));
    }
    
    /**
     * Send a message to the server.
     * @param message The message to send
     */
    private void send(IMessage message) {
		try {
			messageOutputStream.writeMessage(message);
		} catch (IOException e) {
			// TODO Should we do this?
			throw new NotConnectedException();
		}
	}
}
