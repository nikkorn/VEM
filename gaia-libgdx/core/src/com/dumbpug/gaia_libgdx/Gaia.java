package com.dumbpug.gaia_libgdx;

import java.io.IOException;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gaia.Constants;
import gaia.client.gamestate.IPlacementDetails;
import gaia.client.gamestate.players.IPlayerDetails;
import gaia.client.networking.IServerProxy;
import gaia.client.networking.ServerJoinRequestRejectedException;
import gaia.client.networking.ServerProxy;
import gaia.world.Direction;
import gaia.world.PlacementOverlay;
import gaia.world.PlacementUnderlay;
import gaia.world.TileType;
import gaia.world.items.ItemType;

/**
 * Visual Gaia client.
 */
public class Gaia extends ApplicationAdapter {
	/**
	 * The resources to draw the scene with.
	 */
	private SpriteBatch batch;
	private IServerProxy server                   = null;
	private TileResources tileResources           = null;
	private PlacementResources placementResources = null;
	private ItemResources itemResources           = null;
	
	/**
	 * The current active inventory slot index.
	 */
	private int activeInventorySlot = 0;
	
	@Override
	public void create() {
		batch              = new SpriteBatch();
		tileResources      = new TileResources();
		placementResources = new PlacementResources();
		itemResources      = new ItemResources();
		
		try {
			server = ServerProxy.create("localhost", 23445, "niko");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServerJoinRequestRejectedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render() {
		this.update();
		this.draw();
	}
	
	private void update() {
		// Do nothing if we are not connected.
		if (!server.isConnected()) {
			return;
		}
		// Check whether the player wants to move.
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			this.server.getPlayerActions().move(Direction.UP);
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			this.server.getPlayerActions().move(Direction.DOWN);
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			this.server.getPlayerActions().move(Direction.LEFT);
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			this.server.getPlayerActions().move(Direction.RIGHT);
		}
		// Check whether the player wants to change their active inventory slot.
		if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			// We are going left throught the inventory!
			activeInventorySlot = (activeInventorySlot == 0) ? (Constants.PLAYER_INVENTORY_SIZE - 1) : (activeInventorySlot - 1);
		} else if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
			// We are going right throught the inventory!
			activeInventorySlot = (activeInventorySlot == (Constants.PLAYER_INVENTORY_SIZE - 1)) ? 0 : (activeInventorySlot + 1);
		}
		// Check whether to use the active inventory item.
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			this.server.getPlayerActions().useItem(activeInventorySlot);
		}
		// Refresh our server state.
		server.getServerState().refresh();
	}
	
	private void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Get the clients player.
		IPlayerDetails player = server.getServerState().getPlayersDetails().getClientsPlayerDetails();
		// Do nothing if we havent heard about our player spawn yet.
		if (player == null) {
			return;
		}
		// Get the player position.
		int playerX = player.getX();
		int playerZ = player.getY();
		// Get the world tiles offset, defined by the offset of the player when they are moving.
		float tileOffsetX = 0;
		float tileOffsetY = 0;
		if (player.isWalking()) {
			switch (player.getFacingDirection()) {
				case UP:
					tileOffsetY += 16f - (player.getWalkingTransition().getProgress() * 16f);
					break;
				case DOWN:
					tileOffsetY -= player.getWalkingTransition().getProgress() * 16f;
					break;
				case LEFT:
					tileOffsetX -= player.getWalkingTransition().getProgress() * 16f;
					break;
				case RIGHT:
					tileOffsetX += 16f - (player.getWalkingTransition().getProgress() * 16f);
					break;
			}
		}
		// Begin drawing the scene.
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
				batch.draw(this.tileResources.get(tileType), ((x - playerX + 6) * Constants.WORLD_CHUNK_SIZE) + tileOffsetX, ((z - playerZ + 6) * Constants.WORLD_CHUNK_SIZE) + tileOffsetY);
				// Get the placement at this position.
				IPlacementDetails placement = server.getServerState().getPlacements().getPlacementDetails(x, z);
				// Do nothing if there is no placement at this position.
				if (placement != null) {
					// Draw the placement underlay if there is one!
					if (placement.getUnderlay() != PlacementUnderlay.NONE) {
						batch.draw(this.placementResources.getUnderlayTexture(placement.getUnderlay()), (x - playerX + 6) * Constants.WORLD_CHUNK_SIZE, (z - playerZ + 6) * Constants.WORLD_CHUNK_SIZE);
					}
					// Draw the placement overlay if there is one!
					if (placement.getOverlay() != PlacementOverlay.NONE) {
						// TODO Draw overlay!
					}
				}
			}
		}
		// Draw the player!
		batch.draw(PlayerResources.PLAYER_TEXTURE, 6 * Constants.WORLD_CHUNK_SIZE, 6 * Constants.WORLD_CHUNK_SIZE);
		// Draw the Inventory bar!
		batch.draw(HUDResources.INVENTORY_BAR, 0, 0);
		// Draw the Inventory bar active slot!
		batch.draw(HUDResources.INVENTORY_BAR_ACTIVE_SLOT, activeInventorySlot * 16, 0);
		// Draw the items in the inventory bar!
		for (int slotIndex = 0; slotIndex < Constants.PLAYER_INVENTORY_SIZE; slotIndex++) {
			// Get the item type at the current player slot.
			ItemType item = server.getServerState().getPlayersDetails().getClientsPlayerDetails().getInventorySlot(slotIndex);
			// Draw the item in this slot.
			batch.draw(itemResources.getItemTexture(item), slotIndex * 16, 0);
		}
		// Draw the disconnect overlay if we are disconnected.
		if (!server.isConnected()) {
			batch.draw(HUDResources.DISCONNECT, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}
}
