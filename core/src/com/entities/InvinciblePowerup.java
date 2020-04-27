package com.entities;

import com.badlogic.gdx.graphics.Texture;

/**
 * The invincible powerup prevents the firetruck 
 * it is applied to from taking damage and keeps 
 * track on when the powerup was applied
 */
public class InvinciblePowerup extends Powerup {
    private float oldArmour;

    /**
     * Constuctor for the invincibility powerup 
     * gives it a texture to render with 
     * and saves the time that it will 
     * be active for
     * 
     * @param spriteTexture
     * @param activeTime
     */
    public InvinciblePowerup (Texture spriteTexture, int activeTime) {
        super(spriteTexture, activeTime);
        type = "Invicible";
    }
    
    /**
     * Activates the assigned truck's armour 
     * to prevent damage
     */
    @Override
    protected void beginPowerup() {
        oldArmour = target.getArmour();
        target.setArmour(1);
    }

    /*
     * Required by the Powerup base class
     */
    @Override
    protected boolean doPowerupLogic() {return true;}

    /**
     * Removes the assigned fire truck's armour
     */
    @Override
    protected void endPowerup() {
        target.setArmour(oldArmour);
        oldArmour = 0;
    }
}
