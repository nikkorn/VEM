package com.dumbpug.vem.ScriptInterface;

public interface Scriptable {
	public void runScript();
	public boolean isCurrentlyRunningScript();
	public void setScript(String script);
}
