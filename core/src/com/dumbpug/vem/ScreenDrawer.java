package com.dumbpug.vem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.vem.GamePanel.GamePanel;
import com.dumbpug.vem.World.World;
import com.dumbpug.vem.World.WorldCell;
import com.dumbpug.vem.World.WorldObjectType;

public class ScreenDrawer {
	
	/**
	 * Draws everything to the screen
	 * @param batch
	 * @param world
	 * @param gamePanel 
	 * @param vemPosY
	 * @param vemPosX
	 * @param vemOffsetY
	 * @param vemOffsetX
	 */
	public void drawScreen(SpriteBatch batch, World world, GamePanel gamePanel, int vemPosY, int vemPosX, float vemOffsetY, float vemOffsetX) {
		// Get the game texturepack
		TexturePack texturePack = VEM.getTexturePack();
		// Draw ground first..
		for(int y = vemPosY - 10, yRel = 0; y < vemPosY + 10; y++, yRel++) {
			for(int x = vemPosX - 10, xRel = 0; x < vemPosX + 10; x++, xRel++) {
				WorldCell currentCell = world.getWorldCells()[y][x];
				Texture groundTexture = texturePack.GROUND_SEA;
				
				// Draw the actual ground for the cell
				switch(currentCell.getGroundLevel()) {
				case 0:
					groundTexture = texturePack.GROUND_SEA;
					break;
				case 1:
					groundTexture = texturePack.GROUND_SAND_1;
					break;
				case 2:
					groundTexture = texturePack.GROUND_SAND_2;
					break;
				case 3:
				case 4:
					groundTexture = texturePack.GROUND_SAND_3_4;
					break;
				}
				batch.draw(groundTexture, (float) ((xRel*C.CELL_UNIT) - (float) (3*C.CELL_UNIT)) - vemOffsetX, (float) ((yRel*C.CELL_UNIT) - (float) (3*C.CELL_UNIT)) - vemOffsetY, (float) C.CELL_UNIT, (float) C.CELL_UNIT);
				
				// Check to see if this tile meets the sea below, if so draw a shore.
				if(currentCell.getGroundLevel() != 0 && world.getWorldCells()[y - 1][x].getGroundLevel() == 0) {
					batch.draw(texturePack.SHORE, (float) ((xRel*C.CELL_UNIT) - (float) (3*C.CELL_UNIT)) - vemOffsetX, (float) (((yRel-1)*C.CELL_UNIT) - (float) (3*C.CELL_UNIT)) - vemOffsetY, (float) C.CELL_UNIT, (float) C.CELL_UNIT);
				}
			}
		}
		// ...Then draw World objects.
		for(int y = vemPosY + 10, yRel = 20; y > vemPosY - 10; y--, yRel--) {
			for(int x = vemPosX - 10, xRel = 0; x < vemPosX + 10; x++, xRel++) {
				WorldCell currentCell = world.getWorldCells()[y][x];
				// Draw Blemish World Object at this cell (if there is one)
				if(currentCell.getBlemishWorldObject() != null) {
					// Get the type of this blemish world object
					WorldObjectType blemishObjectType = currentCell.getBlemishWorldObject().getType();
					drawWorldObject(blemishObjectType, batch, yRel, xRel, vemOffsetY, vemOffsetX);
				}
				// Draw the world object at this cell (if there is one)
				if(currentCell.getWorldObject() != null) {
					// We may get world objects that are not linked with drawable entities (like rocks and bushes)
					// In these cases we we do the simple task of drawing the world object here.
					if(currentCell.getWorldObject().hasDrawable()) {
						currentCell.getWorldObject().draw(batch, (float) ((yRel*C.CELL_UNIT) - vemOffsetY) - (float) (3*C.CELL_UNIT), (float) ((xRel*C.CELL_UNIT) - vemOffsetX) - (float) (3*C.CELL_UNIT));
					} else {
						// Get the type of this world object
						WorldObjectType objectType = currentCell.getWorldObject().getType();
						drawWorldObject(objectType, batch, yRel, xRel, vemOffsetY, vemOffsetX);
					}
				}
			}
		}
		// Draw our GamePanel.
		gamePanel.draw(batch);
	}
	
	/**
	 * Draws a basic World Object.
	 * @param objectType
	 * @param yRel
	 * @param xRel
	 * @param vemOffsetY
	 * @param vemOffsetX
	 */
	private void drawWorldObject(WorldObjectType objectType, SpriteBatch batch, int yRel, int xRel, float vemOffsetY, float vemOffsetX) {
		TexturePack texturePack = VEM.getTexturePack();
		Texture worldObjectTexture = null;
		// Set the appropriate texture
		switch(objectType) {
		case ROCK_1:
			worldObjectTexture = texturePack.ROCK_1;
			break;
		case ROCK_2:
			worldObjectTexture = texturePack.ROCK_2;
			break;
		case ROCK_3:
			worldObjectTexture = texturePack.ROCK_3;
			break;
		case ROCK_4:
			worldObjectTexture = texturePack.ROCK_4;
			break;
		case ROCK_5:
			worldObjectTexture = texturePack.ROCK_5;
			break;
		case BUSH_1:
			worldObjectTexture = texturePack.BUSH_1;
			break;
		case GEM_STAGE_1:
			worldObjectTexture = texturePack.GEM_STAGE_1;
			break;
		case GEM_STAGE_2:
			worldObjectTexture = texturePack.GEM_STAGE_2;
			break;
		case GEM_STAGE_3:
			worldObjectTexture = texturePack.GEM_STAGE_3;
			break;
		case SCRAP_STAGE_1:
			worldObjectTexture = texturePack.SCRAP_STAGE_1;
			break;
		case SCRAP_STAGE_2:
			worldObjectTexture = texturePack.SCRAP_STAGE_2;
			break;
		case SCRAP_STAGE_3:
			worldObjectTexture = texturePack.SCRAP_STAGE_3;
			break;
		default:
			break;
		}
		// Draw the texture
		batch.draw(worldObjectTexture, (float) ((xRel*C.CELL_UNIT) - (float) (3*C.CELL_UNIT)) - vemOffsetX, (float) ((yRel*C.CELL_UNIT) - (float) (3*C.CELL_UNIT)) - vemOffsetY, (float) C.CELL_UNIT, (float) C.CELL_UNIT);
	}

	/**
	 * Draws a saving screen splash.
	 * @param batch
	 */
	public void drawSavingScreen(SpriteBatch batch) {
		BitmapFont font = FontPack.getFont(FontPack.FontType.GAME_PANEL_FONT);
		TexturePack texturePack = VEM.getTexturePack();
		// Draw a darkened overlay
		batch.draw(texturePack.consoleDarkGreenSquarePlain, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// Draw a saving dialog
		float dialogBoxHeight = Gdx.graphics.getHeight() / 6;
		float dialogBoxWidth = dialogBoxHeight*2;
		batch.draw(texturePack.systemDialogBox, (Gdx.graphics.getWidth()/2) - (dialogBoxWidth/2), (Gdx.graphics.getHeight()/2) - (dialogBoxHeight/2), dialogBoxWidth, dialogBoxHeight);
		font.draw(batch, "SAVING...", (Gdx.graphics.getWidth()/2) - 60, (Gdx.graphics.getHeight()/2) + 5f);
	}
}
