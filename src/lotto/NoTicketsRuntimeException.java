package lotto;

/**
 * Thrown when we attempt to carry out a draw and there are no tickets.
 */
public class NoTicketsRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a new instance of the NoTicketsRuntimeException class.
	 */
	public NoTicketsRuntimeException() {
        super("cannot draw when there are no tickets");
    }
}
