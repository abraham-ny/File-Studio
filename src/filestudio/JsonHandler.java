/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filestudio;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;

/**
 *
 * @author Admin
 */
public class JsonHandler {

    //Gson d = new Gson();
    private static final String FILE_NAME = "fs-workspaces.json";

    // Function to get the file reference
    private File getJsonFile() {
        File dir = new File(Util.home + "\\" + FILE_NAME);
        return new File(dir.getPath());
    }

    // Function to write a list of strings to the JSON file
    public void writeToJson(List<String> names) {
        JsonArray jsonArray = new JsonArray();
        for (String name : names) {
            jsonArray.add(name);
            //System.out.println("ADDED: " + name);
        }

        FileWriter fileWriter = null;
        try {
            File file = getJsonFile();
            fileWriter = new FileWriter(file);
            fileWriter.write(jsonArray.toString());

            //fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    // Function to read the content from the JSON file, if it exists
    public List<String> readFromJson() {
        File file = getJsonFile();
        List<String> names = new ArrayList<>();

        if (file.exists()) {
            try {
                //String content = new String(Files.readAllBytes(file.toPath()));
                FileReader fr = new FileReader(file.getAbsolutePath());
                JsonArray jsonArray = new JsonArray();
                JsonElement jsonel = JsonParser.parseReader(fr);
                if (jsonel.isJsonArray()) {
                    jsonArray = jsonel.getAsJsonArray();
                } else {
                    jsonArray.add("je:empty file");
                }

                for (int i = 0; i < jsonArray.size(); i++) {
                    names.add(jsonArray.get(i).getAsString());
                    System.out.println(jsonArray.get(i).getAsString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // If file doesn't exist, create it with an empty list
            writeToJson(names);
        }

        return names;
    }

    public void deleteData() {
        getJsonFile().delete();
    }
}
