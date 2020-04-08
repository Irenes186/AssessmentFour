package com.entities;

import com.badlogic.gdx.graphics.Texture;

public class SpeedPowerup extends Powerup {
    private float oldSpeed;
    private float oldAcceleration;

    public SpeedPowerup (Texture spriteTexture, int activeTime) {
        super(spriteTexture, activeTime);
        type = "Speed";
    }
    
    @Override
    protected void beginPowerup() {
        oldSpeed = target.getMaxSpeed();
        target.setMaxSpeed(oldSpeed * 1.5f);
        oldAcceleration = target.getAccelerationRate();
        target.setAccelerationRate(oldAcceleration * 1.5f);
    }

    @Override
    protected boolean doPowerupLogic() {return true;}

    @Override
    protected void endPowerup() {
        target.setMaxSpeed(oldSpeed);
        oldSpeed = 0;
        target.setAccelerationRate(oldAcceleration);
        oldAcceleration = 0;
    }
}
