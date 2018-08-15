package com.dumbpug.gaia_libgdx;

import java.io.IOException;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gaia.client.networking.ServerJoinRequestRejectedException;
import gaia.client.networking.ServerProxy;
import gaia.world.Direction;

public class Gaia extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	ServerProxy server = null;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		
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
		batch.begin();
		// batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
