package gaia.client.networking;

import gaia.networking.IMessage;

/**
 * A sender of messages to the server.
 */
public interface IMessageSender {

    /**
     * Send a message to the server.
     * @param message The message to send.
     */
    void send(IMessage message);
}
