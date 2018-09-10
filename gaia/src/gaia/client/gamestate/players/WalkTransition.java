package gaia.client.gamestate.players;

import gaia.Constants;
import gaia.world.IPositionDetails;
import gaia.world.Position;

/**
 * Represents a player walking transition between two tiles.
 */
public class WalkTransition {
	/**
	 * The time in millis at the beginning of the transtion.
	 */
	private long began;
	/**
	 * The origin from where the transition started.
	 */
	private Position origin;
	
	/**
	 * Create a new instance of the WalkTransition class.
	 * @param origin The origin at which the transition started.
	 */
	private WalkTransition(Position origin) {
		this.began  = System.currentTimeMillis();
		this.origin = origin;
	}
	
	/**
	 * Create a WalkTransition instance. 
	 * @param originX The x position at which the transition started.
	 * @param originY The y position at which the transition started.
	 * @return A WalkTransition instance. 
	 */
	public static WalkTransition begin(int originX, int originY) { 
		return new WalkTransition(new Position(originX, originY));
	}
	
	/**
	 * Get the origin of the walk transition.
	 * @return The origin of the walk transition.
	 */
	public IPositionDetails getOrigin() {
		return this.origin;
	}
	
	/**
	 * Get the progress of the transition between 0 and 1.
	 * @return The progress of the transition between 0 and 1.
	 */
	public float getProgress() {
		// Is the transition complete?
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
