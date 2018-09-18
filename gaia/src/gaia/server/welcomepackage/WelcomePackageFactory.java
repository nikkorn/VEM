package gaia.server.welcomepackage;

import java.util.ArrayList;
import gaia.server.world.World;
import gaia.server.world.players.Player;
import gaia.server.world.players.Players;
import gaia.time.Time;
import gaia.world.players.PositionedPlayer;

/**
 * Factory for creating WelcomePackage instances.
 */
public class WelcomePackageFactory {
	
	/**
	 * Create a WelcomePackage instance, populated with world information that a joining player will need to know about.
	 * @param world The world.
	 * @return A WelcomePackage instance, populated with world information that a joining player will need to know about.
	 */
	public static WelcomePackage create(World world) {
		// Get the world seed.
		long worldSeed = world.getSeed();
		// Get the current world time.
		Time worldTime = world.getClock().getCurrentTime();
		// Get the list of positioned players.
		ArrayList<PositionedPlayer> positionedPlayers = getPositionedPlayers(world.getPlayers());
		// Create and return the welcome package.
		return new WelcomePackage(worldSeed, worldTime, positionedPlayers);
	}

	/**
	 * Get a list of the positioned players in the world.
	 * @param players The players.
	 * @return A list of the positioned players in the world.
	 */
	private static ArrayList<PositionedPlayer> getPositionedPlayers(Players players) {
		// Create a list to hold the positioned players.
		ArrayList<PositionedPlayer> positionedPlayers = new ArrayList<PositionedPlayer>();
		// Populate our list of existing players.
		for (Player player : players.getAllPlayers()) {
			positionedPlayers.add(new PositionedPlayer(player.getId(), player.getPosition(), player.getFacingDirection()));
		}
		// Return the list of positioned players.
		return positionedPlayers;
	}
}
