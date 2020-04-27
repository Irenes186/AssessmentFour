package com.entities;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.graphics.Texture;
import com.screens.GameScreen;
import com.testrunner.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class InvinciblePowerupTest {
    GameScreen gameScreenDummy;
    static Texture dummyTexture;
    
    @BeforeClass
    public static void setUpClass() {
        dummyTexture = new Texture("garage.jpg");
    }
    
    @Before
    public void setUp() {
        gameScreenDummy = new GameScreen(new ArrayList<String>(), true);
    }

    @Test
    public void testInvincibilePowerupApply() {
        InvinciblePowerup invinciblePow = new InvinciblePowerup(dummyTexture, 30);
        float oldHealth = gameScreenDummy.getFirestation().getActiveFireTruck().getHealthBar().getCurrentAmount();
        invinciblePow.queuePowerup(gameScreenDummy.getFirestation().getActiveFireTruck());
        invinciblePow.applyPowerup();
        
        gameScreenDummy.getFirestation().getActiveFireTruck().takeDamage(100);
        assertTrue(gameScreenDummy.getFirestation().getActiveFireTruck().getHealthBar().getCurrentAmount() == oldHealth);
    }
    
    @Test
    public void testInvincibilePowerupReset() {
        InvinciblePowerup invinciblePow = new InvinciblePowerup(dummyTexture, 30);
        float oldHealth = gameScreenDummy.getFirestation().getActiveFireTruck().getHealthBar().getCurrentAmount();
        invinciblePow.queuePowerup(gameScreenDummy.getFirestation().getActiveFireTruck());
        invinciblePow.applyPowerup();
        
        gameScreenDummy.getFirestation().getActiveFireTruck().takeDamage(100);
        assertTrue(gameScreenDummy.getFirestation().getActiveFireTruck().getHealthBar().getCurrentAmount() == oldHealth);
        invinciblePow.dequeuePowerup();
        gameScreenDummy.getFirestation().getActiveFireTruck().takeDamage(100);
        assertTrue(gameScreenDummy.getFirestation().getActiveFireTruck().getHealthBar().getCurrentAmount() == oldHealth - 100);
    }
}