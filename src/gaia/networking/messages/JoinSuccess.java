package gaia.networking.messages;

import gaia.networking.IMessage;

/**
 * The message sent to the client on a successful join.
 */
public class JoinSuccess implements IMessage {
	/**
	 * The world seed.
	 */
	private long worldSeed;
	
	/**
	 * Create a new instance of the JoinSuccess class.
	 * @param worldSeed The world seed.
	 */
	public JoinSuccess(long worldSeed) {
		this.worldSeed = worldSeed;
	}
	
	/**
	 * Get the world seed.
	 * @return The world seed.
	 */
	public long getWorldSeed() {
		return this.worldSeed;
	}

	@Override
	public int getTypeId() {
		return 1;
	}
}
