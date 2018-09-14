package gaia.server.welcomepackage;

import java.util.ArrayList;
import gaia.time.Time;
import gaia.world.players.PositionedPlayer;

/**
 * Contains details to be sent to joining players.
 * This holds world information that a joining player will need to know about.
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
	 * The positioned players in the world at the time of joining.
	 */
	private ArrayList<PositionedPlayer> positionedPlayers;
	
	/**
	 * Create a new instance of the WelcomePackage class.
	 * @param worldSeed The world seed.
	 * @param time The current world time.
	 * @param positionedPlayers The positioned players in the world at the time of joining.
	 */
	public WelcomePackage(long worldSeed, Time time, ArrayList<PositionedPlayer> positionedPlayers) {
		this.worldSeed         = worldSeed;
		this.time              = time;
		this.positionedPlayers = positionedPlayers;
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
		return this.positionedPlayers;
	}
}
