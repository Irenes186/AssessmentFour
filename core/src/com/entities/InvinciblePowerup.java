package com.entities;

import com.badlogic.gdx.graphics.Texture;

public class InvinciblePowerup extends Powerup {
    private float oldArmour;
    private boolean oldArmourSaved;

    public InvinciblePowerup (Texture spriteTexture, int activeTime) {
        super(spriteTexture, activeTime);
        oldArmourSaved = false;
    }
	
    public InvinciblePowerup (Texture spriteTexture) {
        super(spriteTexture);
        oldArmourSaved = false;
    }

    @Override
    protected boolean doPowerupLogic() {
        if (!oldArmourSaved) {
            oldArmour = target.getArmour();
            oldArmourSaved = true;
            target.setArmour(1);
        }
        return true;
    }

    @Override
    protected void endPowerup() {
        target.setArmour(oldArmour);
        oldArmour = 0;
        oldArmourSaved = false;
    }
}
