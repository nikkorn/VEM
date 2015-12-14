package com.dumbpug.vem.GamePanel;

/**
 * 
 * @author Nikolas Howard
 *
 */
public class CommandProcessor {
	
	/**
	 * Called by Shell when the user types in a command and presses ENTER.
	 * @param command
	 * @return any output from the command
	 */
	public String process(String rawCommand) {
		// Break the command up into tokens.
		String[] commandTokens = rawCommand.split(" ");
		switch(commandTokens[0].toLowerCase()) {
		// The user has requested the version number of our character.
		case "version":
			return "VEM (Versatile Engineering Module) \n" +
				"Constructed 3012 - WRP Corp. \n" +
				"ser. H56L098RR3 \n" +
				"ver. 7.007.897";
		// The user has requested help.
		case "help":
			// Do we only have one token? (as in has the user only typed 'help')
			if(commandTokens.length > 1) {
				// Which topic does the user need help with?
				switch(commandTokens[1].toLowerCase()) {
				case "drones":
					return "Drones are great!";
				case "ship":
					return "Your ship is the WRP StarJet model 34-BH \n" +
					"Fitted by standard are: \n" +
					"   - WRP-Core System Console v1.034 \n" +
					"   - Hyper-Metal Resource Canister \n" +
					"   - AeroTek TransWave Scanner m.34.4 \n" +
					"   - Prototype WRP-HyperConstructor v0.01 \n";
				case "controls":
					return "PRINT CONTROLS!";
				case "rom":
					break;
				}
			} else {
				return "========================================= \n" +
				   "========    VEM TERMINAL HELP   ========= \n" +
			       "========================================= \n" +
			       "= To get help simply add a sub-topic    = \n" +
			       "= modifier to the 'help' keyword.       = \n" +
			       "= (e.g. 'help drones')                  = \n" +
			       "=                                       = \n" +
			       "=  drones                               = \n" +
			       "=  ship                                 = \n" +
			       "=  controls                             = \n" +
			       "=  rom                                  = \n" +
			       "=                                       = \n" +
			       "========================================= \n";
			}
		}
		// We were't able to handle this command, simply error
		return "ERROR: command '" + rawCommand + "' is invalid!";
	}
}
