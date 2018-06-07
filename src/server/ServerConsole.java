package server;

/**
 * Represents the server console.
 */
public class ServerConsole {
	/**
	 * Whether to write debug output to the server console.
	 */
	private static boolean outputDebugInfo = false;
	
	/**
	 * Set whether to write debug output to the console.
	 * @param debugToConsole Whether to write debug output to the console.
	 */
	public static void setDebugToConsole(boolean debugToConsole) {
		ServerConsole.outputDebugInfo = debugToConsole;
	}
	
	/**
	 * Write the value to the server console.
	 * @param value The value to write to the server console.
	 */
	public static void writeInfo(String value) {
		System.out.println("INFO  : " + value);
	}
	
	/**
	 * Write the debug value to the server console.
	 * @param value The debug value to write to the server console.
	 */
	public static void writeDebug(String value) {
		// Do nothing if we have specified to not output debug info to the console.
		if (outputDebugInfo) {
			System.out.println("DEBUG : " + value);
		}
	}
}
