package gaia.networking;

import java.io.IOException;

/**
 * Reads messages from a MessageInputStream into a queue.
 */
public class QueuedMessageReader implements Runnable {
	/**
	 * The message input stream to read messages from.
	 */
	private MessageInputStream messageInputStream;
	/**
	 * The message queue.
	 */
	private MessageQueue messageQueue = new MessageQueue();
	/**
	 * Flag defining whether the input stream is still connected to the client. 
	 */
	private boolean isConnected = true;
	
	/**
	 * Create a new instance of the QueuedMessageReader class.
	 * @param messageInputStream The message input stream to read messages from.
	 */
	public QueuedMessageReader(MessageInputStream messageInputStream) {
		this.messageInputStream = messageInputStream;
	}
	
	/**
	 * Get whether the reader is still connected.
	 * @return Whether the reader is still connected.
	 */
	public boolean isConnected() {
		return this.isConnected;
	}
	
	/**
	 * Get whether there are any queued messages.
	 * @return Whether there are any queued messages.
	 */
	public boolean hasQueuedMessages() {
		// Checking the message queue has to be done in a thread-safe way.
		synchronized (messageQueue) {
			return this.messageQueue.hasNext();
		}
	}
	
	/**
	 * Get any queued messages in a thread-safe way.
	 * @return Any queued messages.
	 */
	public MessageQueue getQueuedMessages() {
		// Create a new queue to hold the queued messages.
		MessageQueue newMessageQueue = new MessageQueue();
		// Modifying the message queue has to be done in a thread-safe way.
		synchronized (messageQueue) {
			newMessageQueue.add(this.messageQueue);
		}
		// Return the queued messages.
		return newMessageQueue;
	}
	
	@Override
	public void run() {
		// Wait for new messages forever.
		while (true) {
			try {
				// Read the next message from the message input stream.
				IMessage received = this.messageInputStream.readMessage();
				// We received a message! Add it to out message queue.
				synchronized (messageQueue) {
					messageQueue.add(received);
				}
			} catch (IOException e) {
				// Our connection failed! We should stop waiting for new messages.
				this.isConnected = false;
				break;
			}
		}
	}
}
