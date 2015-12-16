package com.dumbpug.vem.ScriptInterface;

public interface Scriptable {
	public void runScript();
	public void stopScript();
	public boolean isCurrentlyRunningScript();
	public void setScript(String script);
}
