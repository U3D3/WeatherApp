package com.u3d3.weatherapp.data;

/**
 * Created by U3D3 on 15/4/6.
 */
import java.io.*;
import java.util.*;

public class DataPersister {
    File historyMenuSave;
    ArrayList<String[]> savedJourneys;

    public DataPersister(){
        savedJourneys = new ArrayList<String[]>();
        historyMenuSave = new File("Resources/HistoryMenuSave.txt");

        try{
            if(!historyMenuSave.exists()) historyMenuSave.createNewFile();
        }
        catch (IOException e){
            System.out.println("Error trying to create a new file");
        }
    }

    public void readFromFile(){
        try{
            savedJourneys.clear();
            FileReader fileReader = new FileReader(historyMenuSave);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String[] tempArray = new String[2];
            String temp = "";

            while(temp != null){
                temp = bufferedReader.readLine();

                if (temp == null) break;

                else {
                    tempArray = temp.split(",");
                    savedJourneys.add(tempArray);
                }
            }

            bufferedReader.close();
            fileReader.close();
        }
        catch (IOException e){
            System.out.println("Error occured whilst trying to read from file;");
            System.exit(1);
        }
    }

    public void writeToFile(String origin, String destination){
        try{
            FileWriter fileWriter = new FileWriter(historyMenuSave, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(origin+","+destination);
            bufferedWriter.newLine();

            bufferedWriter.close();
            fileWriter.close();
        }
        catch (IOException e){
            System.out.println("Error occured whilst trying to write to file;");
            System.exit(1);
        }
    }

    public void clearFile(){
        try{
            historyMenuSave.delete();
            historyMenuSave.createNewFile();
        } catch (IOException e){
            System.out.println("Error trying to create a new file");
        }
    }

    public ArrayList<String[]> getSavedJourneys(){
        readFromFile();
        return savedJourneys;
    }
}
