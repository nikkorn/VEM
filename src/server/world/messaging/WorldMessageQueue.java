package server.world.messaging;

import java.util.ArrayList;

/**
 * A message queue populated by a world or a world entity.
 */
public class WorldMessageQueue {
    /**
     * The list of world messages.
     */
    private ArrayList<WorldMessage> messages = new ArrayList<WorldMessage>();

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
    public WorldMessage next() {
        return this.messages.remove(0);
    }

    /**
     * Add a message to the end of the queue.
     * @param message The message to add.
     */
    public void add(WorldMessage message) {
        this.messages.add(message);
    }
}
