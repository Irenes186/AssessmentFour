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
    
    @Test
    public void testSaveTrucksUnlocked() {
        try {
            GameScreen gameScreenMock = new GameScreen(new ArrayList<String>(), true);
            for (Firetruck truck: gameScreenMock.getFirestation().getParkedFireTrucks()) {
                if (truck.getType() == Constants.TruckType.BLUE) {
                    truck.buy();
                }
            }
            
            saveGame(gameScreenMock.save("testSave.txt"), "testSave.txt");
            gameScreenMock = new GameScreen(loadSave("testSave.txt"), true);
            
            for (Firetruck truck: gameScreenMock.getFirestation().getParkedFireTrucks()) {
                if (truck.getType() == Constants.TruckType.BLUE) {
                    assertTrue(truck.isBought());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("exception occurred");
        }
    }

}
