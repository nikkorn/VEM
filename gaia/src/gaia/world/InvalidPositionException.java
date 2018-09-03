package gaia.world;

/**
 * An exception thrown in when using an invalid world position.
 */
public class InvalidPositionException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new instance of the InvalidPositionException class.
	 * @param x The x position.
	 * @param y The y position.
	 */
	public InvalidPositionException(int x, int y) {
		super("Invalid world position: x:" + x + " y:" + y);
	}
}
