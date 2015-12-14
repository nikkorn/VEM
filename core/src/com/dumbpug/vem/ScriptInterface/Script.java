package com.dumbpug.vem.ScriptInterface;

import javax.script.ScriptContext;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Script {
	private String script = "";
	private boolean running = false;
	private javax.script.ScriptEngine scripEngine = null;
	
	public Script() {
		javax.script.ScriptEngineManager mgr = new ScriptEngineManager();
		scripEngine = mgr.getEngineByExtension("js");
	}
	
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	
	public void run() {
		if(!running) {
			new Thread(new Runnable() {
				public void run() {
					running = true;
					try {
						scripEngine.eval(script);
					} catch (ScriptException e) {
						running = false;
						// A LOT OF ERROR HANDLING WILL HAVE TO GO HERE!!
						e.printStackTrace();
					}
					running = false;
				}
			}).start();
		}
	}
	
	public ScriptContext getContext() {
		return scripEngine.getContext();
	}
	
	public boolean isRunning() {
		return running;
	}
}