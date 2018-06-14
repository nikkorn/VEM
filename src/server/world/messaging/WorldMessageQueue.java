package server.world.messaging;

import java.util.ArrayList;
import server.world.messaging.messages.IWorldMessage;

/**
 * A message queue populated by a world or a world entity.
 */
public class WorldMessageQueue {
    /**
     * The list of world messages.
     */
    private ArrayList<IWorldMessage> messages = new ArrayList<IWorldMessage>();

    /**
     * Get whether the message queue is empty.
     * @return Whether the message queue is empty.
     */
    public boolean isEmpty() {
        return this.messages.isEmpty();
    }

    /**
     * Gets the next message from the queue.
     * @return The next message from the queue.
     */
    public IWorldMessage next() {
        return this.messages.remove(0);
    }

    /**
     * Add a message to the end of the queue.
     * @param message The message to add.
     */
    public void add(IWorldMessage message) {
        this.messages.add(message);
    }
}
