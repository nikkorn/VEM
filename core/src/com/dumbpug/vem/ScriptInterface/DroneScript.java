package com.dumbpug.vem.ScriptInterface;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;

/**
 * 
 * @author Nikolas Howard
 *
 */
public class DroneScript {
	private String rawScript = "";
	private boolean running = false;
	private org.mozilla.javascript.Scriptable scriptable = null;
	private ObservingDebugger observingDebugger;
	
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
					Context cx = Context.enter();
					observingDebugger = new ObservingDebugger();
					cx.setDebugger(observingDebugger, new Integer(0));
					cx.setGeneratingDebug(true);
					cx.setOptimizationLevel(-1);
					Script script = cx.compileString(rawScript, "drone_script", 1, null);
					
					try {
						cx.executeScriptWithContinuations(script, scriptable);
					} catch (RuntimeException re) {
						// TODO Determine if this was the debugger calling a stop, or bad code
					}
					
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
			observingDebugger.setDisconnected(true);
		}
	}
}