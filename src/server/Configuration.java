package server;

import java.io.File;
import org.json.JSONObject;

/**
 * Represents the server configuration defined by server.config.json
 */
public class Configuration {
	/**
	 * The server port.
	 */
	private int port;
	/**
	 * The world save interval.
	 * This is the time in milliseconds between saves of world state to disk.
	 */
	private long worldSaveInterval;
	
	/**
	 * Create a new instance of the Configuration class.
	 * @param port The server port.
	 * @param worldSaveInterval The world save interval.
	 */
	private Configuration(int port, long worldSaveInterval) {
		this.port              = port;
		this.worldSaveInterval = worldSaveInterval;
	}

	/**
	 * Get the server port.
	 * @return The server port.
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Get the world save interval.
	 * This is the time in milliseconds between saves of world state to disk.
	 * @return The world save interval.
	 */
	public long getWorldSaveInterval() {
		return worldSaveInterval;
	}
	
	/**
	 * Get server settings defined in the server configuration file.
	 * @return The server configuration.
	 */
	public static Configuration loadFromDisk() {
		// Grab the 'server.config.json' file.
		File serverConfigFile = new File("server.config.json");
		// Check that the configuration file even exists.
		if (!serverConfigFile.exists()) {
			throw new RuntimeException("Missing 'server.config.json' file!");
		}
		// Convert the raw configuration JSON into a JSONObject instance.
		JSONObject rawConfigJSON = Helpers.readJSONObjectFromFile(serverConfigFile);
		// Parse the configuration options from the JSON.
		int port               = rawConfigJSON.getInt("port");
		long worldSaveInterval = rawConfigJSON.getLong("save_interval");
		// Print the configuration to the console.
		System.out.println("loading server config from 'server.config.json'...");
		System.out.println("port:              " + port);
		System.out.println("worldSaveInterval: " + worldSaveInterval);
		// Return the configuration instance.
		return new Configuration(port, worldSaveInterval);
	}
}
