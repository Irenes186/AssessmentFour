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
public class SpeedPowerupTest {
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
    public void testSpeedPowerup() {
        SpeedPowerup speedPow = new SpeedPowerup(dummyTexture, 30);
        speedPow.queuePowerup(gameScreenDummy.getFirestation().getActiveFireTruck());
        float oldMaxSpeed = gameScreenDummy.getFirestation().getActiveFireTruck().getMaxSpeed();
        speedPow.applyPowerup();
        
        assertTrue(gameScreenDummy.getFirestation().getActiveFireTruck().getMaxSpeed() > oldMaxSpeed);
    }
}
