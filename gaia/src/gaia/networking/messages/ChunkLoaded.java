package gaia.networking.messages;

import java.util.ArrayList;
import gaia.networking.IMessage;

/**
 * A message sent to a client notifying them of a chunk load.
 */
public class ChunkLoaded implements IMessage {
	/**
	 * The x/y position of the chunk.
	 */
	private short x, y;
	/**
	 * The list of positioned and packed placements within the loaded chunk.
	 */
	private ArrayList<PackedPlacement> placements;
	
	/**
	 * Create a new instance of the ChunkLoaded class.
	 * @param x The x position of the chunk.
	 * @param y The y position of the chunk.
	 * @param placements The list of positioned and packed placements within the loaded chunk.
	 */
	public ChunkLoaded(short x, short y, ArrayList<PackedPlacement> placements) {
		this.x          = x;
		this.y          = y;
		this.placements = placements;
	}
	
	/**
	 * Get the x position of the placements relative to the parent chunk.
	 * @return The x position of the placements relative to the parent chunk.
	 */
	public short getX() {
		return this.x;
	}
	
	/**
	 * Get the y position of the placements relative to the parent chunk.
	 * @return The y position of the placements relative to the parent chunk.
	 */
	public short getY() {
		return this.y;
	}
	
	/**
	 * Get the list of positioned and packed placements within the loaded chunk.
	 * @return The list of positioned and packed placements within the loaded chunk.
	 */
	public ArrayList<PackedPlacement> getPackedPlacements() {
		return this.placements;
	}

	@Override
	public int getTypeId() {
		return MessageIdentifier.CHUNK_LOADED;
	}
}
