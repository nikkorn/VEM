package com.dumbpug.vem.ScriptInterface;

import com.dumbpug.vem.C;
import com.dumbpug.vem.Entities.Direction;
import com.dumbpug.vem.Entities.Drone;

public class DroneIntermediary implements DroneScriptInterface {
	private Drone entity;
	
	@SuppressWarnings("unused")
	private DroneScript script;
	
	public DroneIntermediary(DroneScript script, Drone droneEntity) {
		this.script = script;
		this.entity = droneEntity;
	}

	@Override
	public String getId() {
		return entity.getId();
	}

	@Override
	public void move(String direction, int numberOfCells) {
		// TODO Auto-generated method stub
		try {
			entity.move(Direction.valueOf(direction), numberOfCells);
		} catch(Exception e) { 
			e.printStackTrace();
		}
	}
	
	@Override
	public void moveAndWait(String direction, int numberOfCells) {
		try {
			entity.move(Direction.valueOf(direction), numberOfCells);
		} catch(Exception e) { 
			e.printStackTrace();
		}
		
		// We need to stop the script thread until this action has been carried out!
		while(isTravelling()) { 
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void say(String info, long milliseconds) {
		// Restrict message length
		if(info.length() > C.MODAL_MAX_LENGTH) {
			entity.speechContents = info.substring(0, C.MODAL_MAX_LENGTH) + "..."; 
		} else {
			entity.speechContents = info;
		}
		entity.speechModalDuration = milliseconds;
		entity.intialSpeechTime = System.currentTimeMillis();
	}

	@Override
	public boolean isStuck() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return entity.isStuck();
	}

	@Override
	public boolean isTravelling() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return entity.isTravelling();
	}

	@Override
	public void stop() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		entity.stop();
	}

	@Override
	public int getPositionX() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return entity.getCellX();
	}

	@Override
	public int getPositionY() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return entity.getCellY();
	}
}
