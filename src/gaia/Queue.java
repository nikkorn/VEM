package gaia;

import java.util.ArrayList;

/**
 * A queue of items.
 * @param <TItem> The item type.
 */
public class Queue<TItem> {
	 /**
     * The list of items.
     */
    private ArrayList<TItem> items = new ArrayList<TItem>();

    /**
     * Get whether the queue contains an item.
     * @return Whether the queue contains an item.
     */
    public boolean hasNext() {
        return this.items.size() > 0;
    }

    /**
     * Gets the next item from the queue.
     * @return The next item from the queue.
     */
    public TItem next() {
        return this.items.remove(0);
    }

    /**
     * Add an item to the end of the queue.
     * @param item The item to add.
     */
    public void add(TItem item) {
        this.items.add(item);
    }
    
    /**
     * Add all items from another queue to the end of the queue.
     * @param queue The queue to pull items from.
     */
    public void add(Queue<TItem> queue) {
    	while (queue.hasNext()) {
			this.add(queue.next());
		}
    }
}
