package com.dumbpug.vem.ScriptInterface;

public interface DroneScriptInterface {
	public String getId();
	public boolean isStuck();
	public boolean isTravelling();
	public void move(String direction, int numberOfCells);
	public int getPositionX();
	public int getPositionY();
	public void stop();
	public void say(String info, long milliseconds);
}
