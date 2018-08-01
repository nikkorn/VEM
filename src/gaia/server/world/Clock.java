package gaia.server.world;

import gaia.server.Constants;
import gaia.time.Season;
import gaia.time.Time;

/**
 * Represents game world clock.
 */
public class Clock {
	/**
	 * The current time.
	 */
	private Time time;
	/**
	 * The number of server ticks since the last time change.
	 */
	private long ticksSinceLastChange = 0;
	
	/**
	 * Create a new instance of the Clock class.
	 * @param time The current time.
	 */
	public Clock(Time time) {
		this.time = time;
	}
	
	/**
	 * Get the current time.
	 * @return The current time.
	 */
	public synchronized Time getCurrentTime() {
		return new Time(time.getSeason(), time.getDay(), time.getDay(), time.getMinute());
	}
	
	/**
	 * Update the time.
	 * @return Whether the time has changed.
	 */
	public synchronized boolean update() {
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
		int nextMinute = this.time.getMinute() + 1;
		// Have we moved into the next hour?
		if (nextMinute > 59) {
			this.time.setMinute(0);
			tickHour();
		} else {
			this.time.setMinute(nextMinute);
		}
	}
	
	/**
	 * Tick the time to the next hour.
	 */
	private void tickHour() {
		int nextHour = this.time.getHour() + 1;
		// Have we moved into the next day?
		if (nextHour > 23) {
			this.time.setHour(0);
			tickDay();
		} else {
			this.time.setHour(nextHour);
		}
	}
	
	/**
	 * Tick the time to the next day.
	 */
	private void tickDay() {
		int nextDay = this.time.getDay() + 1;
		// Have we moved into the next season?
		if (nextDay > 30) {
			this.time.setDay(0);
			tickSeason();
		} else {
			this.time.setDay(nextDay);
		}
	}
	
	/**
	 * Tick the time to the next season.
	 */
	private void tickSeason() {
		switch(this.time.getSeason()) {
			case SPRING:
				this.time.setSeason(Season.SUMMER);
				break;
			case SUMMER:
				this.time.setSeason(Season.AUTUMN);
				break;
			case AUTUMN:
				this.time.setSeason(Season.WINTER);
				break;
			case WINTER:
				this.time.setSeason(Season.SPRING);
				break;
		}
	}
}
