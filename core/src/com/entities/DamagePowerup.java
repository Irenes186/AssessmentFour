package com.entities;

import com.badlogic.gdx.graphics.Texture;

/**
 * The damage powerup applies a 
 * damage increase to the fireengine
 * it is assigned to and keeps track 
 * on when the powerup was applied
 */
public class DamagePowerup extends Powerup {
    private float oldDamage;

    /**
     * Constuctor for the damage powerup 
     * gives it a texture to render with 
     * and saves the time that it will 
     * be active for
     * 
     * @param spriteTexture
     * @param activeTime
     */
    public DamagePowerup (Texture spriteTexture, int activeTime) {
        super(spriteTexture, activeTime);
        type = "Damage";
    }

    /**
     * Doubles the damage of the firetruck 
     * that the target is set to
     */
    @Override
    protected void beginPowerup() {
        oldDamage = target.getDamage();
        target.setDamage(oldDamage * 2);
    }

    /**
     * Required by the Powerup base 
     * class for certain powerups
     */
    @Override
    protected boolean doPowerupLogic() {return true;}

    /**
     * Sets the damage of the target 
     * firetruck back to its original 
     * value
     */
    @Override
    protected void endPowerup() {
        target.setDamage(oldDamage);
        oldDamage = 0;
    }


}
