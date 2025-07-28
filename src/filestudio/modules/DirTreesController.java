/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import filestudio.GlobalVars;
import filestudio.UserSettings;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
//import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class DirTreesController implements Initializable, GlobalVars {

    /**
     * Initializes the controller class.
     */
    String dirPath;
    UserSettings uss;
    @FXML TextField dirTreePath;
    @FXML CheckBox formatCheck;
    @FXML ProgressBar dirTreesProgress;
    @FXML Label progressText;
    @FXML TreeView dirTreeView;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dirTreesProgress.progressProperty().bind(buildTreeTask.progressProperty());
        buildTreeTask.messageProperty().addListener((obs,old,newVal)->{
            progressText.setText(newVal);
        });
        buildTreeTask.setOnSucceeded(e->{
            JsonObject res = buildTreeTask.getValue();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String outputFilePath = dirPath+File.separator+ "directory_snapshot_" + timestamp + ".json";
            dirPath = outputFilePath;
            // Save JSON to a file
            saveJsonToFile(res, outputFilePath);
            count = 0;
            alert("DirTrees", "Folder structure snapshot creation complete!", dirPath, AlertType.INFORMATION);
            progressText.setText("Done creating tree snapshot");
            dirTreesProgress.setProgress(1.0);
            readTree(outputFilePath);
        });
        
    }
    
    //creates a dirTree for save
    public void createTree(){
        dirTreePath.setText(pickFolder("Create Tree", System.getProperty("user.home"), dirTreePath.getScene().getWindow()));
        dirPath = dirTreePath.getText();
        new Thread(buildTreeTask).start();
    }
    
    //opens a dirTree file for viewing
    public void readTree(){
        dirTreePath.setText(pickFolder("Open Tree", System.getProperty("user.home"), dirTreePath.getScene().getWindow()));
        dirPath = dirTreePath.getText();
    }
    //read treeFiles to treeView
    public void readTree(String path){
        JsonObject jsono = new JsonObject();
        
        if(!new File(path).exists()||!new File(path).isFile()){
            return;
        }
        File file = new File(path);
        TreeItem<String> root = new TreeItem<>(file.getName());
        root.setExpanded(true);
        dirTreeView.setRoot(root);
        dirTreeView.setShowRoot(true);
        try {
            FileReader fr = new FileReader(file.getAbsolutePath());
            JsonElement jsonel = JsonParser.parseReader(fr);
            JsonArray jsonArray = new JsonArray();
            
            if (jsonel.isJsonArray()) {
                    jsonArray = jsonel.getAsJsonArray();
            } else {
                    jsonArray.add("je:empty file");
            }
            jsonArray.forEach(action->{
                if(action.isJsonObject()){
                    JsonObject obj = new JsonObject();
                    obj = action.getAsJsonObject();
                    root.getChildren().add(new TreeItem<String>(obj.get("name").getAsString()));
                }else if(action.isJsonArray()){
                    JsonArray arr = new JsonArray();
                    arr = action.getAsJsonArray();
                    
                }
            });
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DirTreesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public JsonObject getTree(File dir){
        JsonObject jsono = new JsonObject();
        jsono.addProperty("name", dir.getName());
        jsono.addProperty("isDir", dir.isDirectory());
        
        print(jsono.toString());
        if(dir.isDirectory()){
            JsonArray childArray = new JsonArray();
            File[] files = dir.listFiles();
            if(files!=null){
                for(File file:files){
                    childArray.add(getTree(file));
                }
            }
            jsono.add("children", childArray);
        }
        //alert("DevMode-tree", jsono.toString(), "jsono", null);
        return jsono;
    }
    
    private void saveJsonToFile(JsonObject jsonObject, String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            if(!formatCheck.isSelected()){
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(jsonObject, fileWriter);
            }else{
                fileWriter.write(jsonObject.toString());
            }
            fileWriter.flush();
        } catch (IOException e) {
            alert("DirTrees::Error", "Failed to complete the tree snapshot procedure", dirPath, AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    int count = 0;
    Task<JsonObject> buildTreeTask = new Task<JsonObject>(){
        @Override
        protected JsonObject call() throws Exception {
            File root = new File(dirPath);
            int totalNodes = countHeads(root);
            int[] currentCount = {0};//mutable
            //return progressTree(root, totalNodes, currentCount);
            return progressTree(root, totalNodes, currentCount);
        }
        
        private int countHeads(File root) {
            count++;
            File[] files = root.listFiles();
            if(files!=null){
                for(File f:files){
                    if(f.isDirectory()){
                        count+=countHeads(f);
                    }else{
                        count++;
                        updateMessage("Scanning "+count+" items");
                    }
                }
            }
            return count;
        }
        
        private JsonObject progressTree(File dir, int totalNodes, int[] currentCount) {
            
            JsonObject jsono = new JsonObject();
            jsono.addProperty("name", dir.getName());
            jsono.addProperty("isDir", dir.isDirectory());
            currentCount[0]++;
            updateProgress(currentCount[0], totalNodes);
            updateMessage("Procesing "+currentCount[0]+" of "+totalNodes);
            if(dir.isDirectory()){
                JsonArray childArray = new JsonArray();
                File[] files = dir.listFiles();
                if(files!=null){
                    for(File file:files){
                        childArray.add(progressTree(file, totalNodes, currentCount));
                    }
                }
                jsono.add("children", childArray);
            }
            return jsono;
        }
    };
    
    //read
    
    
}
