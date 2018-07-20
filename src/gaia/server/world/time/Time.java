package gaia.server.world.time;

import org.json.JSONObject;

import gaia.server.Constants;

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
	 * The number of server ticks since the last time change.
	 */
	private long ticksSinceLastChange = 0;
	
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
	 * Get the season.
	 * @return The season.
	 */
	public Season getSeason() {
		return this.season;
	}
	
	/**
	 * Get the day.
	 * @return The day.
	 */
	public int getDay() {
		return this.day;
	}
	
	/**
	 * Get the hour.
	 * @return The hour.
	 */
	public int getHour() {
		return this.hour;
	}
	
	/**
	 * Get the minute.
	 * @return The minute.
	 */
	public int getMinute() {
		return this.minute;
	}
	
	/**
	 * Get the formatted time.
	 * @return The formatted time.
	 */
	public String getFormattedTime() {
		return "SEASON: " + season.toString() + " DAY: " + this.day  + " HOUR: " + this.hour + " MINUTE: " + this.minute;
	}
	
	/**
	 * Update the time.
	 * @return Whether the time has changed.
	 */
	public boolean update() {
		ticksSinceLastChange += 1;
		// Have we had enough server ticks to update the time?
		if (ticksSinceLastChange == Constants.SERVER_TIME_MINUTE_TICKS) {
			// Reset our server tick counter.
			ticksSinceLastChange = 0;
			// We have moved into the next minute.
			tickMinute();
			// The time did change.
			return true;
		}
		// The time did not change.
		return false;
	}
	
	/**
	 * Tick the time to the next minute.
	 */
	private void tickMinute() {
		minute += 1;
		if (minute > 59) {
			minute = 0;
			tickHour();
		}
	}
	
	/**
	 * Tick the time to the next hour.
	 */
	private void tickHour() {
		hour += 1;
		if (hour > 23) {
			hour = 0;
			tickDay();
		}
	}
	
	/**
	 * Tick the time to the next day.
	 */
	private void tickDay() {
		day += 1;
		if (day > 30) {
			day = 1;
			tickSeason();
		}
	}
	
	/**
	 * Tick the time to the next season.
	 */
	private void tickSeason() {
		switch(this.season) {
			case SPRING:
				this.season = Season.SUMMER;
				break;
			case SUMMER:
				this.season = Season.AUTUMN;
				break;
			case AUTUMN:
				this.season = Season.WINTER;
				break;
			case WINTER:
				this.season = Season.SPRING;
				break;
		}
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
	
	/**
	 * Create a new instance of time based on existing state.
	 * @param state The existing time state as JSON.
	 * @return The time as defined by the existing state.
	 */
	public static Time fromState(JSONObject state) {
		Season season = Season.values()[state.getInt("season")];
		int day       = state.getInt("day");
		int hour      = state.getInt("hour");
		int minute    = state.getInt("minute");
		return new Time(season, day, hour, minute);
	}
}
