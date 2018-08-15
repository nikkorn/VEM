package gaia.server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents the server console.
 */
public class ServerConsole {
	/**
	 * Whether to write debug output to the server console.
	 */
	private static boolean outputDebugInfo = false;
	/**
	 * The date format to use for console date stamps.
	 */
	private static final DateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
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
		writeDateStamp();
		System.out.println("INFO     : " + value);
	}
	
	/**
	 * Write the warning to the server console.
	 * @param value The warning to write to the server console.
	 */
	public static void writeWarning(String value) {
		writeDateStamp();
		System.out.println("WARNING  : " + value);
	}

	/**
	 * Write the error to the server console.
	 * @param value The error to write to the server console.
	 */
	public static void writeError(String value) {
		writeDateStamp();
		System.out.println("ERROR    : " + value);
	}
	
	/**
	 * Write the debug value to the server console.
	 * @param value The debug value to write to the server console.
	 */
	public static void writeDebug(String value) {
		writeDateStamp();
		// Do nothing if we have specified to not output debug info to the console.
		if (outputDebugInfo) {
			System.out.println("DEBUG    : " + value);
		}
	}

	/**
	 * Write the date stamp as a console message prefix.
	 */
	private static void writeDateStamp() {
		// Get the current date.
		Date now = new Date();
		// Write the date to the console.
		System.out.print(simpleDateFormat.format(now) + ": ");
	}
}
