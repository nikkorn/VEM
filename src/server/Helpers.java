package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Various helpers.
 */
public class Helpers {
	
	/**
	 * Read a JSON object from file.
	 * @param file The file to read from.
	 * @return JSONObject The JSON object.
	 */
	public static JSONObject readJSONObjectFromFile(File file) {
		String rawJsonObject = Helpers.readStringFromFile(file);
		// Return our constructed JSON object.
		return new JSONObject(rawJsonObject);
	}
	
	/**
	 * Read a JSON array from file.
	 * @param file The file to read from.
	 * @return JSONArray The JSON array.
	 */
	public static JSONArray readJSONArrayFromFile(File file) {
		String rawJsonObject = Helpers.readStringFromFile(file);
		// Return our constructed JSON object.
		return new JSONArray(rawJsonObject);
	}
	
	/**
	 * Read entire file into a string.
	 * @param file The file to read from.
	 * @return content The content of the file as a string.
	 */
	public static String readStringFromFile(File file) {
		String rawString = "";
		if(file.exists()) {
			Scanner scanner = null;
			// Set up our scanner.
			try {
				scanner = new Scanner(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			while(scanner.hasNextLine()) {
				rawString += scanner.nextLine();
			}
		} 
		return rawString;
	}
}