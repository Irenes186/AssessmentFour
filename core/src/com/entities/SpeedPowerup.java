package com.entities;

import com.badlogic.gdx.graphics.Texture;

/**
 * The speed powerup increases the speed of the target firetruck for the duration of the powerup
 */
public class SpeedPowerup extends Powerup {
    private float oldSpeed;
    private float oldAcceleration;

    /**
     * Constructor for the speed powerup, sets the texture for the powerup and saves the time that it should remain active for
     * 
     * @param spriteTexture
     * @param activeTime
     */
    public SpeedPowerup (Texture spriteTexture, int activeTime) {
        super(spriteTexture, activeTime);
        type = "Speed";
    }
    
    /**
     * Sets the target truck's speed to 1.5 times its original speed
     */
    @Override
    protected void beginPowerup() {
        oldSpeed = target.getMaxSpeed();
        target.setMaxSpeed(oldSpeed * 1.5f);
        oldAcceleration = target.getAccelerationRate();
        target.setAccelerationRate(oldAcceleration * 1.5f);
    }

    /*
     * Required by the Powerup base class
     */
    @Override
    protected boolean doPowerupLogic() {return true;}

    /**
     * Sets the target firetruck's speed back to its original value
     */
    @Override
    protected void endPowerup() {
        target.setMaxSpeed(oldSpeed);
        oldSpeed = 0;
        target.setAccelerationRate(oldAcceleration);
        oldAcceleration = 0;
    }
}
