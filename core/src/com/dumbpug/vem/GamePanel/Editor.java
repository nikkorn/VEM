package com.dumbpug.vem.GamePanel;

import java.io.File;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.dumbpug.vem.C;
import com.dumbpug.vem.FontPack;
import com.dumbpug.vem.FontPack.FontType;
import com.dumbpug.vem.Input.ValidCharInput;

/**
 * 
 * @author Nikolas Howard
 *
 */
public class Editor implements TerminalApplication {
	Stage editorStage;
	TextArea editorTextArea;
	
	public Editor() {
		// Create our custom scene2D skin.
		Skin skin = new Skin(new FileHandle(new File("bin\\uistyle\\uiskin.json")));
		TextFieldStyle shellTextFieldStyle = skin.get(TextFieldStyle.class);
		shellTextFieldStyle.font = FontPack.getFont(FontType.SHELL_FONT);
		skin.add("default", shellTextFieldStyle);
		editorStage = new Stage();
		editorTextArea = new TextArea("",skin);
		editorTextArea.setBounds((C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX) + C.CONSOLE_MAP_INSET_X_PX, C.CONSOLE_MAP_INSET_Y_PX, C.CONSOLE_VIEWPORT_WIDTH_PX-50, 553 - C.CONSOLE_MAP_INSET_Y_PX);
		editorTextArea.setFocusTraversal(true);
		editorStage.addActor(editorTextArea);
	}

	@Override
	public void processASCIIChar(int decCharValue) {
		//----------------------------------------------------------------------------------------------------
		// TODO This needs to be fixed eventually! We must have the input at the position of the cursor.
		//----------------------------------------------------------------------------------------------------
		switch(decCharValue) {
		case 8: //BACKSPACE
			break;
		case 9: //TAB
			editorTextArea.appendText("   ");
			break;
		case 13: //ENTER
			editorTextArea.appendText("\n");
			break;
		case 32: //SPACE
			editorTextArea.appendText(" ");
			break;
		case 127: //DELETE
			break;
		default:
			//We must be left with a printable character. append it to either the editor or the shell (whichever is active)
			editorTextArea.appendText("" + ValidCharInput.getCorrespondingCharacter(decCharValue));
		}
	}

	@Override
	public void processArrowKeyPress(int key) {
		// In the editor the arrow keys simple move the cursor
		switch(key) {
		case Input.Keys.UP:
			if(editorTextArea.getCursorLine() > 0) {
				editorTextArea.moveCursorLine(editorTextArea.getCursorLine() - 1);
			}
			break;
		case Input.Keys.DOWN:
			if(editorTextArea.getCursorLine() < editorTextArea.getLines()) {
				editorTextArea.moveCursorLine(editorTextArea.getCursorLine() + 1);
			}
			break;
		}
	}

	@Override
	public void enterKeyPress() {
		// TODO Auto-generated method stub
		
	}

	public void saveCurrentFile() {
		// TODO Auto-generated method stub
		
	}

	public void exitEditor() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		editorStage.draw();
	}

	@Override
	public void act() {
		editorTextArea.act(Gdx.graphics.getDeltaTime());
	}
}
