package com.dumbpug.vem.GamePanel;

import com.dumbpug.vem.VEM;
import com.dumbpug.vem.Entities.Drone;

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
			
		// The user has requested drone management.
		case "dm":
			return processDroneManagement(commandTokens);
		
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

	/**
	 * The user wants to do something with drone management
	 * @param commandTokens
	 * @return output
	 */
	private String processDroneManagement(String[] commandTokens) {
		if(commandTokens.length > 1) {
			if(commandTokens[1].toLowerCase().equals("list")) {
				// The user simply wants a list of all existing drones
				String output = "";
				int droneIndex = 1;
				for(Drone drone : VEM.world.getDrones()) {
					output += droneIndex++ + ". " + drone.getId() + "\n";
				}
				return output;
			} else {
				// The second token MUST be a drone id. Check it is valid
				String droneId = commandTokens[1];
				// We can't continue if the drone that the user specified doesn't exist
				if(VEM.world.getDrone(droneId) == null) {
					return "drone '" + droneId + "' does not exist";
				}
				// Process the next command token
				if(commandTokens.length > 2) {
					switch(commandTokens[2].toLowerCase()) {
					case "scripts":
						int snippetLength = 18;
						for(Drone drone : VEM.world.getDrones()) {
							// Is this the target drone?
							if(drone.getId().toLowerCase().equals(droneId.trim().toLowerCase())){
								String output = "";
								for(int position = 0; position < 5; position++) {
									String code = drone.getScriptCode(position);
									String codeSnippet = "--";
									// Construct a code snippet to show user
									if(!code.isEmpty()) {
										if(code.length() > snippetLength) {
											codeSnippet = "'" + code.substring(0, snippetLength) + "'";
										} else {
											codeSnippet = "'" + addPadding(code, snippetLength) + "'";
										}
									}
									output += (position+1) + ". " + codeSnippet + " ";
									// Is this script currently running?
									if(drone.getRunningScriptIndex() == position) {
										output += "(Running)";
									}
									output += "\n";
								}
								return output;
							}
						}
						// We didn't find our drone, let the user know
						return "drone '" + droneId + "' does not exist";
					case "run-script":
						// Run the drone script. check that we have a script index token though
						if(commandTokens.length > 3) {
							return runDroneScript(commandTokens[3], droneId);
						} else {
							return "usage: 'dm " + droneId + " run-script script_number' \n";
						}
					case "stop-script":
						// Stop the currently executing script
						VEM.world.getDrone(droneId).stopScript();
						return "drone '" + droneId + "' was stopped";
					case "state":
						return "This drone is cool!";
					default:
						return "usage: 'dm " + droneId + " scripts' \n"
						 + "       'dm " + droneId + " stop-script' \n"
						 + "       'dm " + droneId + " run-script 1' \n"
						 + "       'dm " + droneId + " state' \n";
					}
				} else {
					return "usage: 'dm " + droneId + " scripts' \n"
							 + "       'dm " + droneId + " stop-script' \n"
							 + "       'dm " + droneId + " run-script 1' \n"
							 + "       'dm " + droneId + " state' \n";
				}
			}
		} else
		{
			return "usage: 'dm list' \n" 
				 + "       'dm drone_name scripts' \n"
				 + "       'dm drone_name stop-script' \n"
				 + "       'dm drone_name run-script 1' \n"
				 + "       'dm drone_name state' \n";
		}
	}
	
	/**
	 * Run a drone script.
	 * @param string
	 * @param droneId
	 * @return
	 */
	private String runDroneScript(String rawScriptIndex, String droneId) {
		int scriptIndex = -1;
		// Attempt to parse the users input as a valid index (int)
		try {
			scriptIndex = Integer.parseInt(rawScriptIndex);
		} catch(NumberFormatException nfe) {
			return "ERROR: '" + rawScriptIndex + "' is not a valid script index. \n" +
					"the index must be between 1 - 5 \n";
		}
		// Make sure that this index is in range 1-5 
		if(!((scriptIndex >= 1) && (scriptIndex <= 5))) {
			return "ERROR: '" + rawScriptIndex + "' is not a valid script index. \n" +
					"the index must be between 1 - 5 \n";
		}
		// Set the script
		VEM.world.getDrone(droneId).setScript(scriptIndex - 1);
		// Run the script
		VEM.world.getDrone(droneId).runScript();
		return "drone '" + droneId + "' began executing script";
	}

	/**
	 * Simply a helper method to add padding to a string
	 * @param input
	 * @param length
	 * @return
	 */
	private String addPadding(String input, int length) {
		if(input.length() >= length) {
			return input;
		} else {
			String padding = "";
			for(int i = 0 ; i < (length - input.length()); i++) {
				padding += " ";
			}
			return input + padding;
		}
	}
}
