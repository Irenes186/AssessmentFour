package com.misc;

import com.Kroy;
import com.badlogic.gdx.graphics.Texture;
import com.entities.ETFortress;
import com.entities.Firetruck;
import com.entities.Patrol;
import com.entities.Powerup;
import com.entities.SpeedPowerup;
import com.pathFinding.MapGraph;
import com.screens.GameScreen;
import com.testrunner.GdxTestRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import java.util.ArrayList;
import com.pathFinding.Junction;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Test;

@RunWith(GdxTestRunner.class)
public class DifficultyTest {
    private Powerup testPowerup;
    private Firetruck testTruck;
    private Patrol testPatrol;
    private ETFortress testFortress;
    @Mock
    private Kroy kroyMock;
    @Mock
    private GameScreen gameScreenMock;

    @Before
    public void setUp() {
        initMocks(this);
    }
    
    @After
    public void resetDiff() {
        Constants.getInstance().difficulty = 1f;
    }
    
    @Test
    public void testDifficultyApplies() {
        Texture textureMock = new Texture("garage.jpg");
        ArrayList<Texture> texturesMock = new ArrayList<Texture>();
        texturesMock.add(textureMock);
        
        MapGraph mapGraphMock = new MapGraph();
        Junction junctionMock = new Junction(0, 0, "mock");
        mapGraphMock.addJunction(junctionMock);
        
        
        ////    easy mode
        // Setup
        Constants.getInstance().difficulty = 0.5f;
        testTruck = new Firetruck(texturesMock,
                texturesMock,
                Constants.TruckType.RED,
                null,
                null,
                null,
                true);
        testPowerup = new SpeedPowerup(textureMock, 100);
        testPatrol = new Patrol(texturesMock, mapGraphMock);
        testFortress = new ETFortress(textureMock, textureMock, 1, 1, 0, 0, Constants.FortressType.CLIFFORD, gameScreenMock);
        
        // Generate values
        int easyPowTime = testPowerup.getActiveTime();
        float easyTruckHealth = testTruck.getHealthBar().getMaxAmount();
        float easyTruckPrice = testTruck.getPrice();
        float easyPatrolHealth = testPatrol.getHealthBar().getMaxAmount();
        float easyFortressHealth = testFortress.getHealthBar().getMaxAmount();
        
        
        ////    hard mode
        // Setup
        Constants.getInstance().difficulty = 1.5f;
        testTruck = new Firetruck(texturesMock,
                texturesMock,
                Constants.TruckType.RED,
                null,
                null,
                null,
                true);
        testPowerup = new SpeedPowerup(textureMock, 100);
        testPatrol = new Patrol(texturesMock, mapGraphMock);
        testFortress = new ETFortress(textureMock, textureMock, 1, 1, 0, 0, Constants.FortressType.CLIFFORD, gameScreenMock);
        
        // Generate values
        int hardPowTime = testPowerup.getActiveTime();
        float hardTruckHealth = testTruck.getHealthBar().getMaxAmount();
        float hardTruckPrice = testTruck.getPrice();
        float hardPatrolHealth = testPatrol.getHealthBar().getMaxAmount();
        float hardFortressHealth = testFortress.getHealthBar().getMaxAmount();
        
        assertTrue(easyPowTime > hardPowTime);
        assertTrue(easyTruckHealth > hardTruckHealth);
        assertTrue(easyTruckPrice < hardTruckPrice);
        assertTrue(easyPatrolHealth < hardPatrolHealth);
        assertTrue(easyFortressHealth < hardFortressHealth);
    }
}
