package com.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.sprites.SimpleSprite;

public abstract class Powerup extends SimpleSprite {
    // Time in frames for a powerup to be active
    // TODO: Change to seconds
	protected int activeTime;
	protected int currentActiveTime;
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
			endPowerup();
			dequeuePowerup();
			return false;
		}
	}
	
	// TODO: FIX PERSPECTIVE ON GRAPHICS RENDER (firetrucks rotate images for 3D effect)
	public void update(Batch batch) {
	    if (target != null) {
	        super.update(batch);
	        setCenter(target.getCentre());
	    }
    }
	
    protected void endPowerup() {}

	protected abstract boolean doPowerupLogic();

}
