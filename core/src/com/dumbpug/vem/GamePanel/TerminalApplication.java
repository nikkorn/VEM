package com.dumbpug.vem.GamePanel;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface TerminalApplication {
	public void processASCIIChar(int decCharValue);
	public void processArrowKeyPress(int key);
	public void enterKeyPress();
	public void draw(SpriteBatch batch);
	public void act();
}
