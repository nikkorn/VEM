package server.world.time;

import org.json.JSONObject;

/**
 * Represents game time.
 */
public class Time {
	/**
	 * The season.
	 */
	private Season season;
	/**
	 * The day. (1-30)
	 */
	private int day;
	/**
	 * The hour. (0-23)
	 */
	private int hour;
	/**
	 * The minute. (0-59)
	 */
	private int minute;
	
	/**
	 * Create a new instance of the Time class.
	 * @param season The season.
	 * @param day The day.
	 * @param hour The hour.
	 * @param minute The minute.
	 */
	public Time(Season season, int day, int hour, int minute) {
		this.season = season;
		this.day    = day;
		this.hour   = hour;
		this.minute = minute;
	}
	
	/**
	 * Get the time state as JSON.
	 * @return The time state as JSON.
	 */
	public JSONObject getState() {
		// Create the JSON object that will represent this time.
		JSONObject timeState = new JSONObject();
		timeState.put("season", this.season.ordinal());
		timeState.put("day", this.day);
		timeState.put("hour", this.hour);
		timeState.put("minute", this.minute);
		// Return the item as a JSON object.
		return timeState;
	}
}
