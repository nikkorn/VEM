package gaia.networking;

import java.util.ArrayList;

/**
 * A message queue.
 */
public class MessageQueue {
	 /**
     * The list of messages.
     */
    private ArrayList<IMessage> messages = new ArrayList<IMessage>();

    /**
     * Get whether the message queue contains a request.
     * @return Whether the message queue contains a message.
     */
    public boolean hasNext() {
        return this.messages.size() > 0;
    }

    /**
     * Gets the next message from the queue.
     * @return The next message from the queue.
     */
    public IMessage next() {
        return this.messages.remove(0);
    }

    /**
     * Add a message to the end of the queue.
     * @param message The message to add.
     */
    public void add(IMessage message) {
        this.messages.add(message);
    }
    
    /**
     * Add all messages from another queue to the end of the queue.
     * @param queue The message queue to pull messages from.
     */
    public void add(MessageQueue queue) {
    	while (queue.hasNext()) {
			this.add(queue.next());
		}
    }
}
