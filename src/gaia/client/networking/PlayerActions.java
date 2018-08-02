package gaia.client.networking;

import java.io.IOException;

import gaia.networking.IMessage;
import gaia.networking.MessageOutputStream;
import gaia.networking.messages.MovePlayer;
import gaia.world.Direction;

/**
 * Player actions.
 */
public class PlayerActions {
    /**
     * The message output stream.
     */
    private MessageOutputStream messageOutputStream;

    /**
     * Create a new instance of the PlayerActions class.
     * @param messageOutputStream The message output stream.
     */
    public PlayerActions(MessageOutputStream messageOutputStream) {
        this.messageOutputStream = messageOutputStream;
    }

    /**
     * Move in the specified direction.
     * @param direction The direction to move in.
     */
    public void move(Direction direction) {
        send(new MovePlayer(direction));
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
