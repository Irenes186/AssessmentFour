package com.entities;

import com.badlogic.gdx.graphics.Texture;

public class InvinciblePowerup extends Powerup {

	public InvinciblePowerup (Texture spriteTexture, int activeTime) {
		super(spriteTexture, activeTime);
	}
	
	public InvinciblePowerup (Texture spriteTexture) {
		super(spriteTexture);
	}

	@Override
	protected boolean doPowerupLogic() {
		// TODO: Implement
		// maybe set a flag in target? maybe take note of previous health, and then set to -1?
		return false;
	}
}
