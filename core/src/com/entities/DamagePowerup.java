package com.entities;

import com.badlogic.gdx.graphics.Texture;

public class DamagePowerup extends Powerup {
    private float oldDamage;

    public DamagePowerup (Texture spriteTexture, int activeTime) {
        super(spriteTexture, activeTime);
    }
    
    @Override
    protected void beginPowerup() {
        oldDamage = target.getDamage();
        target.setDamage(oldDamage * 2);
    }

    @Override
    protected boolean doPowerupLogic() {return true;}

    @Override
    protected void endPowerup() {
        target.setDamage(oldDamage);
        oldDamage = 0;
    }

}
