package gaia.networking.messages;

import gaia.networking.IMessage;
import gaia.time.Time;

/**
 * The message sent to the client on a successful join.
 */
public class JoinSuccess implements IMessage {
	/**
	 * The world seed.
	 */
	private long worldSeed;
	/**
	 * The world time.
	 */
	private Time time;
	
	/**
	 * Create a new instance of the JoinSuccess class.
	 * @param worldSeed The world seed.
	 * @param time The current world time.
	 */
	public JoinSuccess(long worldSeed, Time time) {
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

	@Override
	public int getTypeId() {
		return MessageIdentifier.JOIN_SUCCESS;
	}
}
