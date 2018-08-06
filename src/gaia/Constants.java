package gaia;

/**
 * Constants shared by both server and client.
 */
public class Constants {
	
	/** The application version. */
	public static final String VERSION = "0.0.1";
	
	/** The size of a chunk (tiles). */
	public static short WORLD_CHUNK_SIZE = 32;
	
	/** The size of the world (tiles). */
	public static short WORLD_SIZE = 1024;
	
	/** The number of chunks per axis. */
	public static short WORLD_CHUNKS_PER_AXIS = (short) (WORLD_SIZE / WORLD_CHUNK_SIZE);
	
	/** The distance (in chunks) from a chunk that is regarded as being in the chunks vicinity. */
	public static short WORLD_CHUNK_VICINITY_RANGE = 1;
	
	/** The server clock rate (ms). */
	public static long SERVER_CLOCK_RATE = 125l;
	
	/** The number of server ticks per minute in game. */
	public static int SERVER_TIME_MINUTE_TICKS = 16;
}
