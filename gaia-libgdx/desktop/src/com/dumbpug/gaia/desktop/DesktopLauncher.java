package com.dumbpug.gaia.desktop;

import javax.swing.JOptionPane;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dumbpug.gaia_libgdx.Gaia;

public class DesktopLauncher {
	public static void main (String[] args) {
		String address  = null;
		int port        = 0;
		String playerId = null;
		// The server address, port and player id will either be passed in as command line arguments or we will ask for them.
		if (args.length == 3) {
			address  = args[0];
			port     = Integer.parseInt(args[1]);
			playerId = args[2];
		} else {
			address  = JOptionPane.showInputDialog("Server Address: ", "localhost");
			port     = Integer.parseInt(JOptionPane.showInputDialog("Server Port: ", "23445"));
			playerId = JOptionPane.showInputDialog("Player Name: ", "niko");
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width  = 208;
		config.height = 208;
		new LwjglApplication(new Gaia(address, port, playerId), config);
	}
}
