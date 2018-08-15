package gaia.server.engine;

import gaia.time.Time;

/**
 * Contains details to be sent to joining players.
 */
public class WelcomePackage {
	/**
	 * The world seed.
	 */
	private long worldSeed;
	/**
	 * The world time.
	 */
	private Time time;
	
	/**
	 * Create a new instance of the WelcomePackage class.
	 * @param worldSeed The world seed.
	 * @param time The current world time.
	 */
	public WelcomePackage(long worldSeed, Time time) {
		this.worldSeed = worldSeed;
		this.time      = time;
	}
	
	/**
	 * Get the world seed.
	 * @return the world seed.
	 */
	public long getWorldSeed() {
		return this.worldSeed;
	}
	
	/**
	 * Get the world time.
	 * @return The world time.
	 */
	public Time getWorldTime() {
		return this.time;
	}
}
