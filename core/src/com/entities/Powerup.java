package com.entities;

import com.badlogic.gdx.graphics.Texture;
import com.sprites.SimpleSprite;

public abstract class Powerup extends SimpleSprite {
	private int activeTime;
	private int currentActiveTime;
	// The Firetruck that picked up the powerup
	protected Firetruck target;
	// Whether or not powerup has been picked up
	protected boolean pickedUp;

	// Basic constructor with no powerup timeout
	public Powerup (Texture spriteTexture) {
		super(spriteTexture);
		activeTime = -1;
		create();
	}
	
	public Powerup (Texture spriteTexture, int activeTime) {
		super(spriteTexture);
		this.activeTime = activeTime;
		create();
	}
	
	private void create() {
		currentActiveTime = 0;
		target = null;
		pickedUp = false;
	}
	
	public void queuePowerup (Firetruck target) {
		target.activatePowerup(this, activeTime);
		this.target = target;
	}
	
	public void dequeuePowerup () {
		target.deactivatePowerup(this);
		this.target = null;
		this.pickedUp = false;
	}
	
	/* Apply the powerup's effects to the specified fireengine
	 * 
	 * return true if the powerup was applied successfuly, false otherwise
	 */
	public boolean applyPowerup () {
		if (doPowerupTimeout()) {
			return doPowerupLogic();
		} else {
			return false;
		}
	}
	
	private boolean doPowerupTimeout() {
		if (activeTime == -1) {
			return true;
		} else if (currentActiveTime < activeTime) {
			currentActiveTime++;
			return true;
		} else {
			currentActiveTime = 0;
			dequeuePowerup();
			return false;
		}
	}

	protected abstract boolean doPowerupLogic();

}
