package com.entities;

import com.badlogic.gdx.graphics.Texture;

/**
 * The refill powerup incrementaly fills the firetruck each frame.
 */
public class RefillPowerup extends Powerup {

    /**
     * Constructor for the refill powerup it gives the powerup a texture
     * 
     * @param spriteTexture
     */
    public RefillPowerup(Texture spriteTexture) {
        super(spriteTexture);
        type = "Refill";
    }
    

    /** 
     * Fills the truck if needed and removes itself once the truck reaches full water
     * 
     * @return boolean false if the target truck is at full water true otherwise
     */
    @Override
    protected boolean doPowerupLogic() {
        if (target.getWaterBar().isFull()) {
            dequeuePowerup();
            return false;
        } else {
            target.getWaterBar().addResourceAmount(10);
            return true;
        }
    }

}
