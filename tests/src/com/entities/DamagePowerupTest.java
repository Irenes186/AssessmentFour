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
public class DamagePowerupTest {
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
    public void testDamagePowerupApply() {
        DamagePowerup damagePow = new DamagePowerup(dummyTexture, 30);
        float oldDamage = gameScreenDummy.getFirestation().getActiveFireTruck().getDamage();
        damagePow.queuePowerup(gameScreenDummy.getFirestation().getActiveFireTruck());
        damagePow.applyPowerup();
        
        assertTrue(gameScreenDummy.getFirestation().getActiveFireTruck().getDamage() > oldDamage);
    }
    
    @Test
    public void testDamagePowerupReset() {
        DamagePowerup damagePow = new DamagePowerup(dummyTexture, 30);
        float oldDamage = gameScreenDummy.getFirestation().getActiveFireTruck().getDamage();
        damagePow.queuePowerup(gameScreenDummy.getFirestation().getActiveFireTruck());
        damagePow.applyPowerup();
        
        assertTrue(gameScreenDummy.getFirestation().getActiveFireTruck().getDamage() > oldDamage);
        damagePow.dequeuePowerup();
        assertTrue(gameScreenDummy.getFirestation().getActiveFireTruck().getDamage() == oldDamage);
    }
}