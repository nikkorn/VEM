package com.dumbpug.vem.GamePanel;

import java.io.File;

import com.badlogic.gdx.Gdx;
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
public class Shell implements TerminalApplication {
	Stage shellStage;
	TextArea shellTextArea;
	CommandProcessor commandProcessor = null;
	// This string contains text we can't edit (eg previously executed commands/command output)
	String outputGarbage = "";
	// This string is the current line in the shell, this text we CAN edit
	String currentInput = "";
	
	public Shell() {
		// Initialise our command processor
		commandProcessor = new CommandProcessor();
		// Create our custom scene2D skin.
		Skin skin = new Skin(new FileHandle(new File("bin\\uistyle\\uiskin.json")));
		TextFieldStyle shellTextFieldStyle = skin.get(TextFieldStyle.class);
		shellTextFieldStyle.font = FontPack.getFont(FontType.SHELL_FONT);
		skin.add("default", shellTextFieldStyle);
		shellStage = new Stage();
		shellTextArea = new TextArea("",skin);
		shellTextArea.setBounds((C.WIDTH_PX - C.CONSOLE_VIEWPORT_WIDTH_PX) + C.CONSOLE_MAP_INSET_X_PX, C.CONSOLE_MAP_INSET_Y_PX, C.CONSOLE_VIEWPORT_WIDTH_PX-50, 553 - C.CONSOLE_MAP_INSET_Y_PX);
		shellTextArea.setFocusTraversal(true);
		shellStage.addActor(shellTextArea);
	}

	@Override
	public void processASCIIChar(int decCharValue) {
		//----------------------------------------------------------------------------------------------------
		// TODO This needs to be fixed eventually! We must have the input at the position of the cursor.
		//----------------------------------------------------------------------------------------------------
		switch(decCharValue) {
		case 8: //BACKSPACE
			// TODO Fix this to remove at cursor position!!!
			if(currentInput.length() > 0) {
				currentInput = currentInput.substring(0, currentInput.length() - 1);
			}
			break;
		case 9: //TAB
			currentInput += "   ";
			break;
		case 13: //ENTER
			// DO NOTHING! A press of the ENTER key while in the shell is a green light for executing a command. Let this be handled by 'enterKeyPress()'
			break;
		case 32: //SPACE
			currentInput += " ";
			break;
		case 127: //DELETE
			break;
		default:
			//We must be left with a printable character. append it to either the editor or the shell (whichever is active)
			currentInput += ValidCharInput.getCorrespondingCharacter(decCharValue);
		}
	}

	@Override
	public void processArrowKeyPress(int key) {
		//----------------------------------------------------------------------------------------------------
		// TODO Revisit recently executed commands
		//----------------------------------------------------------------------------------------------------
	}

	@Override
	public void enterKeyPress() {
		// Execute current command (if there is one)
		if(currentInput != "") {
			// Process the input as a command
			String commandOutput = commandProcessor.process(currentInput);
			// Add the current command to the garbage pile with a newline
			outputGarbage += "> " + currentInput + "\n";
			// Append any output from the command to the garbage pile
			if(commandOutput != null) {
				outputGarbage += commandOutput + "\n";
			}
			// If we have a 'clear' command then we can handle that here. clear the terminal
			if(currentInput.trim().toLowerCase().equals("clear")) {
				outputGarbage = "";
			}
			// Reset the current input
			currentInput = "";
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		shellStage.draw();
	}

	@Override
	public void act() {
		// The contents of the shell is a combination of old output and current input.
		shellTextArea.setText("");
		// We must use 'appendText' to force focus to bottom of terminal
		shellTextArea.appendText(outputGarbage + ">> " + currentInput);
		shellTextArea.act(Gdx.graphics.getDeltaTime());
	}
}
