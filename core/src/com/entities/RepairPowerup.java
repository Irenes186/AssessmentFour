package com.entities;

import com.badlogic.gdx.graphics.Texture;

public class RepairPowerup extends Powerup {

    public RepairPowerup(Texture spriteTexture) {
        super(spriteTexture);
    }

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
