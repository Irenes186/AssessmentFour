package com.entities;

import com.badlogic.gdx.graphics.Texture;

public class InvinciblePowerup extends Powerup {
    private float oldArmour;

    public InvinciblePowerup (Texture spriteTexture, int activeTime) {
        super(spriteTexture, activeTime);
    }
    
    @Override
    protected void beginPowerup() {
        oldArmour = target.getArmour();
        target.setArmour(1);
    }

    @Override
    protected boolean doPowerupLogic() {return true;}

    @Override
    protected void endPowerup() {
        target.setArmour(oldArmour);
        oldArmour = 0;
    }
}
