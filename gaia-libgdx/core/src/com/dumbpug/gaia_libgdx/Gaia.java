package com.dumbpug.gaia_libgdx;

import java.io.IOException;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gaia.client.networking.ServerJoinRequestRejectedException;
import gaia.client.networking.ServerProxy;
import gaia.world.Direction;
import gaia.world.TileType;

public class Gaia extends ApplicationAdapter {
	private final int TILE_LENGTH = 16;
	
	private SpriteBatch batch;
	private ServerProxy server          = null;
	private TileResources tileResources = null;
	
	@Override
	public void create () {
		batch         = new SpriteBatch();
		tileResources = new TileResources();
		
		try {
			server = ServerProxy.create("localhost", 23445, "nikolas howard");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServerJoinRequestRejectedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render () {
		this.update();
		this.draw();
	}
	
	private void update() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.W)) {
			this.server.getPlayerActions().move(Direction.UP);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			this.server.getPlayerActions().move(Direction.DOWN);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.A)) {
			this.server.getPlayerActions().move(Direction.LEFT);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {
			this.server.getPlayerActions().move(Direction.RIGHT);
		}
		// Refresh our server state.
		server.getServerState().refresh();
	}
	
	private void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// TODO Get real player position!
		int playerX = -20;
		int playerZ = 480;
		
		batch.begin();
		// Draw the tiles around the player!
		for (int x = (playerX - 6); x <= (playerX + 6); x++) {
			for (int z = (playerZ - 6); z <= (playerZ + 6); z++) {
				// Get the tile type at this position.
				TileType tileType = server.getServerState().getTileAt(x, z);
				// Draw the tile!
				batch.draw(this.tileResources.get(tileType), (x - playerX + 6) * TILE_LENGTH, (z - playerZ + 6) * TILE_LENGTH);
			}
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
