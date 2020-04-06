package com.entities;

import com.badlogic.gdx.graphics.Texture;

public class RefillPowerup extends Powerup {

    public RefillPowerup(Texture spriteTexture) {
        super(spriteTexture);
    }
    
//    @Override
//    protected void beginPowerup() {
////        target.getWaterBar().resetResourceAmount();
//    }

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
