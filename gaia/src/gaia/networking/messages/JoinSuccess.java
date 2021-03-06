package gaia.networking.messages;

import java.util.ArrayList;
import gaia.networking.IMessage;
import gaia.time.Time;
import gaia.world.players.PositionedPlayer;

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
	 * The positioned players in the world at the time of joining.
	 */
	private ArrayList<PositionedPlayer> players;
	
	/**
	 * Create a new instance of the JoinSuccess class.
	 * @param worldSeed The world seed.
	 * @param time The current world time.
	 * @param players The positioned players in the world at the time of joining.
	 */
	public JoinSuccess(long worldSeed, Time time, ArrayList<PositionedPlayer> players) {
		this.worldSeed = worldSeed;
		this.time      = time;
		this.players   = players;
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
	
	/**
	 * Get the positioned players in the world at the time of joining.
	 * @return The positioned players in the world at the time of joining.
	 */
	public ArrayList<PositionedPlayer> getPositionedPlayers() {
		return this.players;
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.JOIN_SUCCESS;
	}
}
