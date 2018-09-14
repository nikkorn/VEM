package gaia.client.gamestate.players;

import gaia.world.Direction;
import gaia.world.Position;
import gaia.world.items.Inventory;
import gaia.world.items.ItemType;

/**
 * Represents a player.
 */
public class Player implements IPlayerDetails {
    /**
     * The player id.
     */
    private String id;
    /**
     * The player position.
     */
    private Position position;
    /**
     * The direction in which the player is facing.
     */
    private Direction facingDirection;
    /**
     * The walking transition for the player.
     */
    private WalkTransition walkTransition = null;
    /**
     * The player inventory.
     */
    private Inventory inventory = new Inventory();

    /**
     * Create an instance of the Player class.
     * @param id The player id.
     * @param position The player position.
     * @param facingDirection The facing direction of the player.
     */
    public Player(String id, Position position, Direction facingDirection) {
        this.id              = id;
        this.position        = position;
        this.facingDirection = facingDirection;
    }

    /**
     * Get the player id.
     * @return The player id.
     */
    public String getPlayerId() {
        return this.id;
    }

    /**
     * Get the player position.
     * @return The player position.
     */
    public Position getPosition() {
        return position;
    }
    
    /**
	 * Get the x position of the player.
	 * @return The x position of the player.
	 */
    @Override
	public int getX() {
		return position.getX();
	}

    /**
	 * Get the y position of the player.
	 * @return The y position of the player.
	 */
	@Override
	public int getY() {
		return position.getY();
	}
    
    /**
     * Get the direction that the player is facing.
     * @return The direction that the player is facing.
     */
    @Override
    public Direction getFacingDirection() {
    	return this.facingDirection;
    }
    
    /**
     * Set the direction that the player is facing.
     * @param direction The direction that the player is facing.
     */
    public void setFacingDirection(Direction direction) {
    	this.facingDirection = direction;
    }
    
    /**
	 * Get the walking transition of the player, or null if the player is not currently walking.
	 * @return The walking transition of the player, or null if the player is not currently walking.
	 */
    @Override
    public WalkTransition getWalkingTransition() {
    	return this.walkTransition;
    }
    
    /**
     * Get the player inventory.
     * @return The player inventory.
     */
    public Inventory getInventory() {
    	return this.inventory;
    }
    
    /**
	 * Get the item at the specified slot index of the player's inventory. 
	 * @param slotIndex the slot index.
	 * @return The item at the specified slot index of the player's inventory. 
	 */
    @Override
	public ItemType getInventorySlot(int slotIndex) {
		return Inventory.isValidSlotIndex(slotIndex) ? this.inventory.get(slotIndex) : ItemType.NONE;
	}
    
    /**
     * Get whether the player is currently walking.
     * @return Whether the player is currently walking.
     */
    @Override
    public boolean isWalking() {
    	// The player is currently walking if they are in the middle of a walk transition.
    	return this.walkTransition != null && !this.walkTransition.isComplete();
    }
    
    /**
     * Move to a neighbouring tile in the specified direction.
     * @param direction The direction in which the player is walking.
     */
    public void move(Direction direction) {
    	// There is nothing to do if we are already walking.
    	if (this.isWalking()) {
    		return;
    	}
    	// Update the player's facing direction to reflect the movement they are about to make.
    	this.setFacingDirection(direction);
    	// Find the target position based on the direction we are moving and our current position.
    	int targetX = position.getX();
    	int targetY = position.getY();
    	switch (direction) {
	    	case UP:
	    		targetY += 1;
				break;
			case DOWN:
				targetY -= 1;
				break;
			case LEFT:
				targetX -= 1;
				break;
			case RIGHT:
				targetX += 1;
				break;
			default:
				throw new RuntimeException("unexpected direction: " + direction);
    	}
    	// Begin the walk transition for the player, passing their original position.
    	this.walkTransition = WalkTransition.begin(position.getX(), position.getY(), targetX, targetY);
    	// Update the player's actual position.
    	this.position.setX((short)targetX);
    	this.position.setY((short)targetY);
    }
    
    /**
     * Move the player to the tile at the x/y position.
     * @param x The x position to move the player to.
     * @param y The y position to move the player to.
     * @param direction The direction should face after moving the the position.
     */
    public void moveTo(int x, int y, Direction direction) {
    	// We are being moved to a tile and should not be walking.
    	this.walkTransition = null;
    	// Update the player's facing direction.
    	this.setFacingDirection(direction);
    	// Update the player's actual position.
    	this.position.setX((short)x);
    	this.position.setY((short)y);
    }
}
