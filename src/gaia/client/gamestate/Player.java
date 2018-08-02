package gaia.client.gamestate;

import gaia.Position;

/**
 * Represents a player.
 */
public class Player {
    /**
     * The player id.
     */
    private String id;
    /**
     * The player position.
     */
    private Position position;

    /**
     * Create an instance of the Player class.
     * @param id The player id.
     * @param position The player position.
     */
    public Player(String id, Position position) {
        this.id      = id;
        this.position = position;
    }

    /**
     * Get the player id.
     * @return The player id.
     */
    public String getPlayerId() {
        return this.id;
    }

    /**
     * Get the player position.
     * @return The player position.
     */
    public Position getPosition() {
        return position;
    }
}
