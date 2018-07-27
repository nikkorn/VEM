package gaia.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Stack;
import gaia.Constants;
import gaia.Direction;
import gaia.client.networking.ServerJoinRequestRejectedException;
import gaia.client.networking.ServerProxy;
import gaia.networking.MessageQueue;
import gaia.networking.messages.MovePlayer;

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
			ServerProxy server = ServerProxy.create("localhost", 23445, playerId);
			
			// Listen for messages from the server on another thread and spit them out to the console.
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						// Get any received messages.
						MessageQueue received = server.getReceivedMessageQueue();
						// Write out details of the message to the console.
						while (received.hasNext()) {
							System.out.println("-> id:" + received.next().getTypeId());
						}
					}
				}
			}).start();
			
			// We managed to connect to the server.
			System.out.println("connected!");
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
				server.sendMessage(new MovePlayer(Direction.UP));
				break;
			case "down":
				server.sendMessage(new MovePlayer(Direction.DOWN));
				break;
			case "left":
				server.sendMessage(new MovePlayer(Direction.LEFT));
				break;
			case "right":
				server.sendMessage(new MovePlayer(Direction.RIGHT));
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