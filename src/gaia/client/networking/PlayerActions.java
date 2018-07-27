package gaia.client.networking;

import gaia.Direction;
import gaia.networking.messages.MovePlayer;

/**
 * Player actions.
 */
public class PlayerActions {
    /**
     * The message sender.
     */
    private IMessageSender messageSender;

    /**
     * Create a new instance of the PlayerActions class.
     * @param messageSender The message sender.
     */
    public PlayerActions(IMessageSender messageSender) {
        this.messageSender = messageSender;
    }

    /**
     * Move in the specified direction.
     * @param direction The direction to move in.
     */
    public void move(Direction direction) {
        this.messageSender.send(new MovePlayer(direction));
    }
}
