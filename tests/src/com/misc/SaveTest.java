package com.misc;

import com.Kroy;
import com.badlogic.gdx.graphics.Texture;
import com.entities.ETFortress;
import com.entities.Firetruck;
import com.entities.Powerup;
import com.entities.RepairPowerup;
import com.entities.SpeedPowerup;
import com.screens.GameScreen;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import java.util.ArrayList;
import java.util.HashMap;

import com.testrunner.GdxTestRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

public class SaveTest {
    GameScreen gameScreenDummy;
    static Texture dummyTexture;
    
    public ArrayList<String> loadSave(String fileName) {
        String fileString;
        File file = new File("assets/saves/" + fileName);
        ArrayList<String> fileContents = new ArrayList<String>();

        try {

          BufferedReader reader = new BufferedReader (new FileReader (file));


          while ((fileString = reader.readLine()) != null) {
            fileContents.add(fileString);

           }
        } catch (IOException e) {
          e.printStackTrace();
        }

        try {
            JSONParser parser = new JSONParser();
            JSONObject gameData = (JSONObject) parser.parse(fileContents.get(fileContents.size() - 1));
            com.misc.Constants.getInstance().difficulty = (float) ((double) gameData.get("Difficulty"));
        } catch (ParseException pe) {
            System.out.println (pe.toString());
        }
        
        return fileContents;
    }
    
    /**
     * Overwrites the requested file
     * 
     * @param saveText
     * @param newFileName
     */
    public void saveGame(String saveText, String newFileName) {
        File folder = new File("assets/saves");

        if (!folder.exists()) {
            folder.mkdir();
        }
        File[] listOfFiles = folder.listFiles();

        newFileName = "assets/saves/" + newFileName;

        for (File file : listOfFiles) {
           if (file.getName() == newFileName)  {
               file.delete();
           }

        }

        try {
            FileWriter writer = new FileWriter(newFileName); 
            writer.write (saveText);
            writer.close ();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @BeforeClass
    public static void setUpClass() {
        dummyTexture = new Texture("garage.jpg");
    }
    
    @Before
    public void setUp() {
        gameScreenDummy = new GameScreen(new ArrayList<String>(), true);
    }
    
    @Test
    public void testSaveTrucksUnlocked() {
        for (Firetruck truck: gameScreenDummy.getFirestation().getParkedFireTrucks()) {
            if (truck.getType() == Constants.TruckType.BLUE) {
                truck.buy();
            }
        }
        
        saveGame(gameScreenDummy.save("testSave.txt"), "testSave.txt");
        gameScreenDummy = new GameScreen(loadSave("testSave.txt"), true);
        
        for (Firetruck truck: gameScreenDummy.getFirestation().getParkedFireTrucks()) {
            if (truck.getType() == Constants.TruckType.BLUE) {
                assertTrue(truck.isBought());
            }
        }
    }
    
    @Test
    public void testSaveTruckPos() {
        gameScreenDummy.getFirestation().getActiveFireTruck().setPosition(100, 100);
        
        saveGame(gameScreenDummy.save("testSave.txt"), "testSave.txt");
        gameScreenDummy = new GameScreen(loadSave("testSave.txt"), true);
        
        assertTrue(gameScreenDummy.getFirestation().getActiveFireTruck().getX() == 100);
        assertTrue(gameScreenDummy.getFirestation().getActiveFireTruck().getY() == 100);
    }
    
    @Test
    public void testSaveTruckRotation() {
        gameScreenDummy.getFirestation().getActiveFireTruck().resetRotation(90);
        
        saveGame(gameScreenDummy.save("testSave.txt"), "testSave.txt");
        gameScreenDummy = new GameScreen(loadSave("testSave.txt"), true);
        
        assertTrue(gameScreenDummy.getFirestation().getActiveFireTruck().getRotation() == 90);
    }
    
    @Test
    public void testSaveActiveTruckHealth() {
        float redTruckMaxHealth;
        redTruckMaxHealth = gameScreenDummy.getFirestation().getActiveFireTruck().getHealthBar().getMaxAmount();
        gameScreenDummy.getFirestation().getActiveFireTruck().getHealthBar().setCurrentAmount((int) redTruckMaxHealth / 2);
        
        saveGame(gameScreenDummy.save("testSave.txt"), "testSave.txt");
        gameScreenDummy = new GameScreen(loadSave("testSave.txt"), true);
        
        assertTrue(gameScreenDummy.getFirestation().getActiveFireTruck().getHealthBar().getCurrentAmount() == (int) redTruckMaxHealth / 2);
    }
    
    @Test
    public void testSaveParkedTruckHealth() {
        float blueTruckMaxHealth = -1;
        for (Firetruck truck: gameScreenDummy.getFirestation().getParkedFireTrucks()) {
            if (truck.getType() == Constants.TruckType.BLUE) {
                blueTruckMaxHealth = truck.getHealthBar().getMaxAmount();
                truck.getHealthBar().setCurrentAmount((int) blueTruckMaxHealth / 2);
            }
        }
        if (blueTruckMaxHealth == -1) {
            throw new RuntimeException("blue fire truck not found!");
        }
        
        saveGame(gameScreenDummy.save("testSave.txt"), "testSave.txt");
        gameScreenDummy = new GameScreen(loadSave("testSave.txt"), true);
        
        for (Firetruck truck: gameScreenDummy.getFirestation().getParkedFireTrucks()) {
            if (truck.getType() == Constants.TruckType.BLUE) {
                assertTrue(truck.getHealthBar().getCurrentAmount() == (int) blueTruckMaxHealth / 2);
            }
        }
    }
    
    @Test
    public void testSaveActiveTruckWater() {
        float redTruckMaxWater;
        redTruckMaxWater = gameScreenDummy.getFirestation().getActiveFireTruck().getWaterBar().getMaxAmount();
        gameScreenDummy.getFirestation().getActiveFireTruck().getWaterBar().setCurrentAmount((int) redTruckMaxWater / 2);
        
        saveGame(gameScreenDummy.save("testSave.txt"), "testSave.txt");
        gameScreenDummy = new GameScreen(loadSave("testSave.txt"), true);
        
        assertTrue(gameScreenDummy.getFirestation().getActiveFireTruck().getWaterBar().getCurrentAmount() == (int) redTruckMaxWater / 2);
    }
    
    @Test
    public void testSaveParkedTruckWater() {
        float blueTruckMaxWater = -1;
        for (Firetruck truck: gameScreenDummy.getFirestation().getParkedFireTrucks()) {
            if (truck.getType() == Constants.TruckType.BLUE) {
                blueTruckMaxWater = truck.getWaterBar().getMaxAmount();
                truck.getWaterBar().setCurrentAmount((int) blueTruckMaxWater / 2);
            }
        }
        if (blueTruckMaxWater == -1) {
            throw new RuntimeException("blue fire truck not found!");
        }
        
        saveGame(gameScreenDummy.save("testSave.txt"), "testSave.txt");
        gameScreenDummy = new GameScreen(loadSave("testSave.txt"), true);
        
        for (Firetruck truck: gameScreenDummy.getFirestation().getParkedFireTrucks()) {
            if (truck.getType() == Constants.TruckType.BLUE) {
                assertTrue(truck.getWaterBar().getCurrentAmount() == (int) blueTruckMaxWater / 2);
            }
        }
    }
    
    @Test
    public void testSaveScore() {
        gameScreenDummy.setScore(100);
        
        saveGame(gameScreenDummy.save("testSave.txt"), "testSave.txt");
        gameScreenDummy = new GameScreen(loadSave("testSave.txt"), true);
        
        assertTrue(gameScreenDummy.getScore() == 100);
    }
    
    @Test
    public void testSaveTimeLeft() {
        gameScreenDummy.setTime(60);
        
        saveGame(gameScreenDummy.save("testSave.txt"), "testSave.txt");
        gameScreenDummy = new GameScreen(loadSave("testSave.txt"), true);
        
        assertTrue(gameScreenDummy.getTime() == 60);
    }
    
    @Test
    public void testSaveFirestationHealth() {
        float firestationMaxHealth = gameScreenDummy.getFirestation().getHealthBar().getCurrentAmount();
        gameScreenDummy.getFirestation().getHealthBar().setCurrentAmount((int) firestationMaxHealth / 2);
        
        saveGame(gameScreenDummy.save("testSave.txt"), "testSave.txt");
        gameScreenDummy = new GameScreen(loadSave("testSave.txt"), true);
        
        assertTrue(gameScreenDummy.getFirestation().getHealthBar().getCurrentAmount() == (int) firestationMaxHealth / 2);
    }
    
    @Test
    public void testSaveFirestationDestroyed() {
        gameScreenDummy.setTime(0);
        gameScreenDummy.getFirestation().getHealthBar().setCurrentAmount(0);
        gameScreenDummy.getFirestation().destroyOnNoHealth();
        
        saveGame(gameScreenDummy.save("testSave.txt"), "testSave.txt");
        gameScreenDummy = new GameScreen(loadSave("testSave.txt"), true);
        
        assertTrue(gameScreenDummy.getFirestation().isDestroyed());
    }
    
    @Test
    public void testSaveActiveTruck() {
        for (int i = 0; i < gameScreenDummy.getFirestation().getParkedFireTrucks().size(); i++) {
            Firetruck truck = gameScreenDummy.getFirestation().getParkedFireTrucks().get(i);
            if (truck.getType() == Constants.TruckType.BLUE) {
                truck.buy();
                gameScreenDummy.getFirestation().changeFiretruck(i);
            }
        }
        
        saveGame(gameScreenDummy.save("testSave.txt"), "testSave.txt");
        gameScreenDummy = new GameScreen(loadSave("testSave.txt"), true);
        
        assertTrue(gameScreenDummy.getFirestation().getActiveFireTruck().getType() == Constants.TruckType.BLUE);
    }
    
    @Test
    public void testSaveFortressHealth() {
        for (ETFortress fortress: gameScreenDummy.getETFortresses()) {
            switch (fortress.getType()) {
                case CLIFFORD:
                    fortress.getHealthBar().setCurrentAmount(1);
                    break;
                case MINSTER:
                    fortress.getHealthBar().setCurrentAmount(2);
                    break;
                case RAIL:
                    fortress.getHealthBar().setCurrentAmount(3);
                    break;
                case CASTLE1:
                    fortress.getHealthBar().setCurrentAmount(4);
                    break;
                case CASTLE2:
                    fortress.getHealthBar().setCurrentAmount(5);
                    break;
                case MOSSY:
                    fortress.getHealthBar().setCurrentAmount(6);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown ETFortress type: " + fortress.getType().name());
            }
        }
        
        saveGame(gameScreenDummy.save("testSave.txt"), "testSave.txt");
        gameScreenDummy = new GameScreen(loadSave("testSave.txt"), true);
        
        for (ETFortress fortress: gameScreenDummy.getETFortresses()) {
            switch (fortress.getType()) {
                case CLIFFORD:
                    assertTrue(fortress.getHealthBar().getCurrentAmount() == 1);
                    break;
                case MINSTER:
                    assertTrue(fortress.getHealthBar().getCurrentAmount() == 2);
                    break;
                case RAIL:
                    assertTrue(fortress.getHealthBar().getCurrentAmount() == 3);
                    break;
                case CASTLE1:
                    assertTrue(fortress.getHealthBar().getCurrentAmount() == 4);
                    break;
                case CASTLE2:
                    assertTrue(fortress.getHealthBar().getCurrentAmount() == 5);
                    break;
                case MOSSY:
                    assertTrue(fortress.getHealthBar().getCurrentAmount() == 6);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown ETFortress type: " + fortress.getType().name());
            }
        }
    }
    
    @Test
    public void testSaveFortressesDestroyed() {
        for (ETFortress fortress: gameScreenDummy.getETFortresses()) {
            switch (fortress.getType()) {
                case CLIFFORD:
                case RAIL:
                case CASTLE1:
                    fortress.getHealthBar().setCurrentAmount(0);
                    fortress.flood();
                    break;
                default:
            }
        }
        
        saveGame(gameScreenDummy.save("testSave.txt"), "testSave.txt");
        gameScreenDummy = new GameScreen(loadSave("testSave.txt"), true);
        
        for (ETFortress fortress: gameScreenDummy.getETFortresses()) {
            switch (fortress.getType()) {
                case CLIFFORD:
                case RAIL:
                case CASTLE1:
                    fortress.flood();
                    assertTrue(fortress.isFlooded());
                    break;
                default:
            }
        }
    }
    
    @Test
    public void testSavePowerups() {
        SpeedPowerup speedPow = new SpeedPowerup(dummyTexture, 30);
        speedPow.queuePowerup(gameScreenDummy.getFirestation().getActiveFireTruck());
        RepairPowerup repairPow = new RepairPowerup(dummyTexture);
        repairPow.queuePowerup(gameScreenDummy.getFirestation().getActiveFireTruck());
        
        saveGame(gameScreenDummy.save("testSave.txt"), "testSave.txt");
        gameScreenDummy = new GameScreen(loadSave("testSave.txt"), true);
        
        boolean speedFound = false;
        boolean repairFound = false;
        for (Powerup pow: gameScreenDummy.getFirestation().getActiveFireTruck().getActivePowerups().keySet()) {
            if (!speedFound && pow.equals(speedPow)) {
                speedFound = true;
            } else if (!repairFound && pow.equals(repairPow)) {
                repairFound = true;
            } else {
                fail("excess powerup found: " + pow + ": " + pow.getActiveTime());
            }
        }
        
        assertTrue(speedFound);
        assertTrue(repairFound);
    }
    
    @Test
    public void testSaveDifficulty() {
        Constants.getInstance().difficulty = 0.5f;
        gameScreenDummy = new GameScreen(new ArrayList<String>(), true);
        
        saveGame(gameScreenDummy.save("testSave.txt"), "testSave.txt");
        Constants.getInstance().difficulty = 1f;
        gameScreenDummy = new GameScreen(loadSave("testSave.txt"), true);
        
        assertTrue(Constants.getInstance().difficulty == 0.5f);
        Constants.getInstance().difficulty = 1f;
    }
}
