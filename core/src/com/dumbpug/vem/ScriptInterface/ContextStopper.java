package com.dumbpug.vem.ScriptInterface;

/**
 * 
 * @author Nikolas Howard
 *
 */
public class ContextStopper {
	private boolean pendingStop = false;
	
	public void stop() {
		pendingStop = true;
	}
	
	public boolean isStopPending() {
		return pendingStop;
	}
}
