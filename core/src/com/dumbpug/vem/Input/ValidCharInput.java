package com.dumbpug.vem.Input;

/**
 * Determines whether the character picked up by the InputProcessor
 * is valid input for the GamePanel Shell/Editor
 * @author Nikolas Howard
 *
 */
public class ValidCharInput {
	private static int[] validASCII = new int[] {
			8,   //BACKSPACE
			9,   //TAB
			13,  //ENTER
			32,  //SPACE
			127  //DELETE
	};
			
	public static boolean isValid(int charCode) {
		// If the code is between 33 and 126 then return true as this portion of the ASCII table are all valid for input.
		if((charCode >= 33) && (charCode <= 126)) {
			return true;
		}
		for(int i = 0; i < validASCII.length; i++) {
			if(validASCII[i] == charCode) {
				return true;
			}
		}
		return false;
	}
	
	public static char getCorrespondingCharacter(int charCode) {
		return (char) charCode;
	}
}
