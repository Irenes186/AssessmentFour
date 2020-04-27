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
public class RefillPowerupTest {
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
    public void testRefillPowerupApply() {
        RefillPowerup refillPowerup = new RefillPowerup(dummyTexture);
        refillPowerup.queuePowerup(gameScreenDummy.getFirestation().getActiveFireTruck());
        gameScreenDummy.getFirestation().getActiveFireTruck().getWaterBar().setCurrentAmount(1);
        refillPowerup.applyPowerup();
        
        assertTrue(gameScreenDummy.getFirestation().getActiveFireTruck().getWaterBar().getCurrentAmount() > 1);
    }
}