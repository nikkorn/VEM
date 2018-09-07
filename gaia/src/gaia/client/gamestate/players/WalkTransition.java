package gaia.client.gamestate.players;

import gaia.Constants;

/**
 * Represents a player walking transition between two tiles.
 */
public class WalkTransition {
	/**
	 * The time in millis at the beginning of the transtion.
	 */
	private long began;
	
	/**
	 * Create a new instance of the WalkTransition class.
	 */
	private WalkTransition() {
		this.began = System.currentTimeMillis();
	}
	
	/**
	 * Create a WalkTransition instance. 
	 * @return A WalkTransition instance. 
	 */
	public static WalkTransition begin() { 
		return new WalkTransition();
	}
	
	/**
	 * Get the progress of the transition between 0 and 1.
	 * @return The progress of the transition between 0 and 1.
	 */
	public float getProgress() {
		// Is the transition complete?.
		if (isComplete()) {
			return 1f;
		}
		// Return how much progress we have made.
		return (System.currentTimeMillis() - began) / Constants.PLAYER_MOVE_DURATION;
	}
	
	/**
	 * Get whether this transition is complete.
	 * @return Whether this transition is complete.
	 */
	public boolean isComplete() {
		return System.currentTimeMillis() >= (began + Constants.PLAYER_MOVE_DURATION);
	}
}
