/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio.modules;

import filestudio.GlobalVars;
import filestudio.UserSettings;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
//import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;

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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void createTree(){
        pickDir(dirTreePath, "Create Tree", System.getProperty("user.home"), dirTreePath.getScene().getWindow());
        dirPath = dirTreePath.getText();
        JSONObject jsonTree = getTree(new File(dirPath));
        // Generate a timestamped filename
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String outputFilePath = "directory_snapshot_" + timestamp + ".json";
        dirPath = outputFilePath;
        // Save JSON to a file
        saveJsonToFile(jsonTree, outputFilePath);
        alert("DirTrees", "Folder structure snapshot creation complete!", dirPath, AlertType.INFORMATION);
    }
    
    public JSONObject getTree(File dir){
        JSONObject jsono = new JSONObject();
        jsono.put("name", dir.getName());
        jsono.put("isDir", dir.isDirectory());
        if(dir.isDirectory()){
            JSONArray childArray = new JSONArray();
            File[] files = dir.listFiles();
            if(files!=null){
                for(File file:files){
                    childArray.appendElement(getTree(file));
                }
            }
            jsono.put("children", childArray);
        }
        alert("DevMode-tree", jsono.toString(), "jsono", null);
        return jsono;
    }
    
    private void saveJsonToFile(JSONObject jsonObject, String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(jsonObject.toString(JSONStyle.LT_COMPRESS));
            fileWriter.flush();
        } catch (IOException e) {
            alert("DirTrees::Error", "Failed to complete the tree snapshot procedure", dirPath, AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
}
