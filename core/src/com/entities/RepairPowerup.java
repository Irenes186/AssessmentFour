package com.entities;

import com.badlogic.gdx.graphics.Texture;

/**
 * The repair powerup incrementaly heals the firetruck each frame.
 */
public class RepairPowerup extends Powerup {

    /**
     * Constructor for the repair powerup it gives the powerup a texture
     * 
     * @param spriteTexture
     */
    public RepairPowerup(Texture spriteTexture) {
        super(spriteTexture);
        type = "Repair";
    }

    /**
     * Heals the truck if needed and removes itself once the truck reaches full health
     * 
     * @return boolean false if the target truck is at full health true otherwise
     */
    @Override
    protected boolean doPowerupLogic() {
        if (target.getHealthBar().isFull()) {
            dequeuePowerup();
            return false;
        } else {
            target.getHealthBar().addResourceAmount(1);
            return true;
        }
    }

}
