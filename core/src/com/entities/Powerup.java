package com.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.sprites.SimpleSprite;

/**
 * Abstract class that is used to create the classes for specific powerups
 * and holds logic for assigning powerups and rendering them
 */
public abstract class Powerup extends SimpleSprite {
    // Time in frames for a powerup to be active
    // TODO: Change to seconds
	protected int activeTime;
	protected int currentActiveTime;
	// The Firetruck that picked up the powerup
	protected Firetruck target;
	// Whether or not powerup has been picked up
	protected boolean pickedUp;
	// Is this the first frame the powerup has been applied?
	protected boolean firstApply;

  protected String type;

	/**
	 * Basic constructor with no powerup timeout 
	 * 
	 * @param spriteTexture
	 */
	public Powerup (Texture spriteTexture) {
		super(spriteTexture, null);
		activeTime = -1;
		create();
	}
	
	/**
	 * Constructor for when the powerup has an active time
	 * 
	 * @param spriteTexture
	 * @param activeTime
	 */
	public Powerup (Texture spriteTexture, int activeTime) {
		super(spriteTexture, null);
		this.activeTime = (int)(activeTime * (1 / com.misc.Constants.getInstance().difficulty));
		create();
	}
	
	/**
	 * Initialises basic variables to default values
	 */
	private void create() {
		currentActiveTime = 0;
		target = null;
		pickedUp = false;
		firstApply = true;
	}
	
	/**
	 * Add this powerup to the target fireTruck
	 */
	public void queuePowerup (Firetruck target) {
		target.activatePowerup(this, activeTime);
		this.target = target;
	}
	
	/**
	 * Remove this powerup from the target firetruck
	 */
	public void dequeuePowerup () {
		target.deactivatePowerup(this);
		this.target = null;
		this.pickedUp = false;
	}
	
	/** Apply the powerup's effects to the specified fireengine
	 * 
	 * @return true if the powerup was applied successfully, false otherwise
	 */
	public boolean applyPowerup () {
	    if (firstApply) {
	        beginPowerup();
	        firstApply = false;
	    }
		if (doPowerupTimeout()) {
			return doPowerupLogic();
		} else {
			return false;
		}
	}

	/**
	 * Checks if the powerup timer has run out and deactivates the powerup if needed
	 * 
	 * @return boolean true if there is still time left otherwise false
	 */
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

	/**
	 * Renders the powerup sprite if the powerup has a firetruck
	 * 
	 * @param batch
	 */
	public void update(Batch batch) {
	    if (target != null) {
	        batch.draw(texture, target.getCentreX(), target.getCentreY(), texture.getWidth() * 3, texture.getHeight() * 3);
	    }
    }

	protected void beginPowerup() {}

	protected abstract boolean doPowerupLogic();

	protected void endPowerup() {}
	
	public int getActiveTime() {return activeTime;}
	
	@Override
	public boolean equals(Object o) {return o instanceof Powerup &&
	                                    ((Powerup) o).type == type &&
	                                    ((Powerup) o).activeTime == activeTime &&
                                        ((Powerup) o).target.getType() == target.getType();}

  public String toString() {
        return type;
  }
}
