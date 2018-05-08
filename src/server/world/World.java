package server.world;

/**
 * Represents the entire game world.
 */
public class World
{
	/**
	 * The world seed.
	 */
	private long seed;
	
	/**
	 * Creates an instance of the World class.
	 * @param seed The world seed.
	 */
	public World(long seed)
	{
		this.seed = seed;
	}
	
	/**
	 * Gets the world seed.
	 * @return The world seed.
	 */
	public long getSeed()
	{
		return this.seed;
	}
}
