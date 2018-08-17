package com.dumbpug.gaia_libgdx;

import java.io.IOException;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gaia.client.gamestate.Placement;
import gaia.client.gamestate.Player;
import gaia.client.networking.ServerJoinRequestRejectedException;
import gaia.client.networking.ServerProxy;
import gaia.world.Direction;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementUnderlay;
import gaia.world.TileType;

public class Gaia extends ApplicationAdapter {
	private final int TILE_LENGTH = 16;
	
	private SpriteBatch batch;
	private ServerProxy server                    = null;
	private TileResources tileResources           = null;
	private PlacementResources placementResources = null;
	
	@Override
	public void create () {
		batch              = new SpriteBatch();
		tileResources      = new TileResources();
		placementResources = new PlacementResources();
		
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
		
		// Get the clients player.
		Player player = server.getServerState().getPlayers().getClientsPlayer();
		
		// Do nothing if we havent heard about our player spawn yet.
		if (player == null) {
			return;
		}
		
		// Get the player position.
		int playerX = player.getPosition().getX();
		int playerZ = player.getPosition().getY();
		
		batch.begin();
		// Draw the tiles and placements around the player!
		for (int x = (playerX - 6); x <= (playerX + 6); x++) {
			for (int z = (playerZ - 6); z <= (playerZ + 6); z++) {
				// Get the tile type at this position.
				TileType tileType = server.getServerState().getTiles().getTileAt(x, z);
				// Do nothing if this tile is OOB.
				if (tileType == null) {
					continue;
				}
				// Draw the tile!
				batch.draw(this.tileResources.get(tileType), (x - playerX + 6) * TILE_LENGTH, (z - playerZ + 6) * TILE_LENGTH);
				
				// Get the placement at this position.
				Placement placement = server.getServerState().getPlacements().getPlacementAt(x, z);
				// Do nothing if there is no placement at this position.
				if (placement != null) {
					// Draw the placement underlay if there is one!
					if (placement.getUnderlay() != PlacementUnderlay.NONE) {
						batch.draw(this.placementResources.getUnderlayTexture(placement.getUnderlay()), (x - playerX + 6) * TILE_LENGTH, (z - playerZ + 6) * TILE_LENGTH);
						System.out.println("TREE: X:" + x + " Z:" + z);
					}
					// Draw the placement overlay if there is one!
					if (placement.getOverlay() != PlacementOverlay.NONE) {
						// TODO Draw overlay!
					}
				}
			}
		}
		// Draw the player!
		batch.draw(PlayerResources.PLAYER_TEXTURE, 6 * TILE_LENGTH, 6 * TILE_LENGTH);
		System.out.println("PLAYER: X:" + playerX + " Z:" + playerZ);
		// Draw the player!
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
