package com.dumbpug.vem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * 
 * @author Nikolas Howard
 *
 */
public class FontPack {
	private static BitmapFont SPEECH_BOX_FONT;
	private static BitmapFont SHELL_FONT;
	private static BitmapFont GAME_PANEL_FONT;
	
	public static void loadFonts() {
		SPEECH_BOX_FONT = new BitmapFont(Gdx.files.internal("fonts/speech.fnt"), Gdx.files.internal("fonts/speech.png"), false);
		SHELL_FONT = new BitmapFont(Gdx.files.internal("fonts/shellfont.fnt"), Gdx.files.internal("fonts/shellfont.png"), false);
		GAME_PANEL_FONT = new BitmapFont(Gdx.files.internal("fonts/gamepanel.fnt"), Gdx.files.internal("fonts/gamepanel.png"), false);
	}
	
	public static BitmapFont getFont(FontType type) {
		switch(type) {
		case GAME_PANEL_FONT:
			return GAME_PANEL_FONT;
		case SHELL_FONT:
			return SHELL_FONT;
		case SPEECH_BOX_FONT:
			return SPEECH_BOX_FONT;
		}
		return null;
	}
	
	public enum FontType {
		SPEECH_BOX_FONT,
		SHELL_FONT,
		GAME_PANEL_FONT
	}
}
