package com.dumbpug.vem.World;
/**
 * 
 * @author Nikolas Howard
 * !!! Non-obstructive types must be added to World.positionIsClear()
 */
public enum WorldObjectType {
	NULL,
	ROCK_1,
	ROCK_2,
	ROCK_3,
	ROCK_4,
	ROCK_5,
	BUSH_1,
	GEM_STAGE_1,
	GEM_STAGE_2,
	GEM_STAGE_3,
	SCRAP_STAGE_1,
	SCRAP_STAGE_2,
	SCRAP_STAGE_3,
	
	// SPECIAL SLOTS (These 20 slots are reserved for world objects that are special and wont be randomly picked by the world map generator)
	VEM,
	DRONE,
	SPECIAL_SLOT_3,
	SPECIAL_SLOT_4,
	SPECIAL_SLOT_5,
	SPECIAL_SLOT_6,
	SPECIAL_SLOT_7,
	SPECIAL_SLOT_8,
	SPECIAL_SLOT_9,
	SPECIAL_SLOT_10,
	SPECIAL_SLOT_11,
	SPECIAL_SLOT_12,
	SPECIAL_SLOT_13,
	SPECIAL_SLOT_14,
	SPECIAL_SLOT_15,
	SPECIAL_SLOT_16,
	SPECIAL_SLOT_17,
	SPECIAL_SLOT_18,
	SPECIAL_SLOT_19,
	SPECIAL_SLOT_20, 
}
