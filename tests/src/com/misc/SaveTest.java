package com.misc;

import com.Kroy;
import com.entities.Firetruck;
import com.screens.GameScreen;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import java.util.ArrayList;
import com.testrunner.GdxTestRunner;
import org.junit.Before;
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
    public void testSaveDiscovered() {
        gameScreenDummy.setTime(0);
        gameScreenDummy.getFirestation().getHealthBar().setCurrentAmount(0);
        assertTrue(gameScreenDummy.getFirestation().destroy());
        
        saveGame(gameScreenDummy.save("testSave.txt"), "testSave.txt");
        gameScreenDummy = new GameScreen(loadSave("testSave.txt"), true);
        
        assertTrue(gameScreenDummy.getFirestation().isDestroyed());
    }
}
