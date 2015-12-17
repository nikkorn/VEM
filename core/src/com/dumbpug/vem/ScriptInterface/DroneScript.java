package com.dumbpug.vem.ScriptInterface;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;

import com.dumbpug.vem.ScriptInterface.MyFactory.StoppableContext;

/**
 * 
 * @author Nikolas Howard
 *
 */
public class DroneScript {
	private String rawScript = "";
	private boolean running = false;
	private org.mozilla.javascript.Scriptable scriptable = null;
	private ContextStopper contextStopper = null;
	
	public DroneScript() {
		Context cx = Context.enter();
		scriptable = cx.initStandardObjects();
	}
	
	public String getScript() {
		return rawScript;
	}
	
	public void setScript(String script) {
		this.rawScript = script;
	}
	
	public void run() {
		if(!running) {
			running = true;
			// Run our Rhino script in a new thread
			new Thread(new Runnable() {
				public void run() {
					StoppableContext cx = (StoppableContext) Context.enter();
					contextStopper = new ContextStopper();
					cx.setContextStopper(contextStopper);
					Script script = cx.compileString(rawScript, "drone_script", 1, null);
					
					try {
						cx.executeScriptWithContinuations(script, scriptable);
					} catch (RuntimeException re) {
						// TODO Determine if this was the debugger calling a stop, or bad code
					}
					
					// Reset the ContextStopper
					contextStopper = null;
					// This script is no longer running
					running = false;
				}
			}).start();
		}
	}
	
	public org.mozilla.javascript.Scriptable getGlobalScope() {
		return scriptable;
	}
	
	/**
	 * Check whether the script is running
	 * @return
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Stop the currently running script.
	 */
	public void stop() {
		if(running) {
			contextStopper.stop();
		}
	}
}