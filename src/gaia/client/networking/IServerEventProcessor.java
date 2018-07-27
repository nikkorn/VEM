package gaia.client.networking;

import gaia.Position;
import gaia.client.players.Player;

/**
 * Processor for server events.
 */
public interface IServerEventProcessor {

    /**
     * Called in response to a player spawn.
     * @param player The spawning player.
     */
    void onPlayerSpawn(Player player);

    /**
     * Called in response to a player despawn.
     * @param player The despawning player.
     */
    void onPlayerDespawn(Player player);

    /**
     * Called in response to a player moving.
     * @param player The player that has moved.
     * @param previous The position of the player before moving.
     */
    void onPlayerMove(Player player, Position previous);
}
