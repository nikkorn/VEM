package gaia.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Stack;
import gaia.Constants;
import gaia.client.networking.ServerJoinRequestRejectedException;
import gaia.client.networking.ServerProxy;
import gaia.world.Direction;
import gaia.world.TileType;

/**
 * A command line based client to test server functionality.
 */
public class CLIClient {
	
	/**
	 * Entry point.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		// Print the splash.
		System.out.println("#################### Gaia CLI Client #####################");
		System.out.println("##########################################################");
		System.out.println("version: " + Constants.VERSION);
		System.out.println("##########################################################");
		// Create a scanner to read player input.
		Scanner inputScanner = new Scanner(System.in);
		// Ask the user for a player id.
		System.out.print("enter player id: ");
		// Get the first line of input (the player id).
		String playerId = inputScanner.nextLine();
		try {
			System.out.print("attempting to connect... ");
			// Create a connection with the server.
			final ServerProxy server = ServerProxy.create("localhost", 23445, playerId);
			// We managed to connect to the server.
			System.out.println("connected!");
			System.out.println("world seed: " + server.getServerState().getWorldSeed());
			System.out.println("Type 'help' for the command list.");
			// Read the first command.
			String commandString = inputScanner.nextLine();
			// Read input until the user exits.
			while (!commandString.toLowerCase().equals("exit")) {
				// Process this command.
				Stack<String> command = createCommandStack(commandString.toLowerCase().split(" "));
				// If the command is empty then offer some help.
				if (command.isEmpty()) {
					System.out.println("unknown command: " + commandString);
				} else {
					processCommand(command, server);
				}
				// Get the next line of input.
				commandString = inputScanner.nextLine();
			}
		} catch (UnknownHostException e) {
			System.out.println("error: unknown host");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("error: failed to connect, is the server running?");
		} catch (ServerJoinRequestRejectedException e) {
			System.out.print("error: server rejected join request: " + e.getMessage());
		}
		inputScanner.close();
	}

	/**
	 * Process a command.
	 * @param command The command to process.
	 * @param server The server.
	 */
	private static void processCommand(Stack<String> command, ServerProxy server) {
		// Get the first token of the command.
		String initialCommandToken = command.pop();
		// Act based on the initial token.
		switch (initialCommandToken) {
			case "help":
				printCommandList();
				break;
			case "move":
				// We are expecting a direction to move in.
				if (command.isEmpty()) {
					System.out.println("expected a direction to move in");
				} else {
					try {
						movePlayer(command.pop(), server);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				break;
			case "tile":
				// We are expecting a x and z position.
				if (command.size() != 2) {
					System.out.println("expected a x and z position.");
				} else {
					// Parse the tile position from the command.
					int x         = Integer.parseInt(command.pop());
					int z         = Integer.parseInt(command.pop());
					TileType type = server.getServerState().getTiles().getTileAt(x, z);
					// Print the tile type to the console.
					if (type == null) {
						System.out.println("not a valid world position");
					} else {
						System.out.println("tile type: " + type.toString());
					}
				}
				break;
			case "refresh":
				// We are refreshing the server state.
				server.getServerState().refresh();
				break;
			default:
				System.out.println(initialCommandToken);
		}
	}
	
	/**
	 * Print the command list to the console.
	 */
	private static void printCommandList() {
		System.out.println("##########################################################");
		System.out.println("commands:");
		System.out.println("move [up|down|left|right]     Move in a direction");
		System.out.println("tile [x] [z]                  Get the type of tile at the x/z position");
		System.out.println("refresh                       Refresh the server state");
		System.out.println("help                          Print the command list");
		System.out.println("exit                          Close the client");
		System.out.println("##########################################################");
	}

	/**
	 * Move the player.
	 * @param direction The direction to move in.
	 * @param server the server.
	 * @throws IOException 
	 */
	private static void movePlayer(String direction, ServerProxy server) throws IOException {
		switch (direction) {
			case "up":
				server.getPlayerActions().move(Direction.UP);
				break;
			case "down":
				server.getPlayerActions().move(Direction.DOWN);
				break;
			case "left":
				server.getPlayerActions().move(Direction.LEFT);
				break;
			case "right":
				server.getPlayerActions().move(Direction.RIGHT);
				break;
			default:
				System.out.println("unknown direction: " + direction);
		}
	}
	
	/**
	 * Create a command stack.
	 * @param command The command.
	 * @return The command stack.
	 */
	private static Stack<String> createCommandStack(String[] command) {
		Stack<String> commandStack = new Stack<String>();
		for (int i = command.length - 1; i >= 0; i--) {
			commandStack.add(command[i]);
		}
		return commandStack;
	}
}
