/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio.modules;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import filestudio.GlobalVars;
import filestudio.Util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class AddListController implements Initializable, GlobalVars {

    @FXML
    Button addButton;
    @FXML
    TextField listSearch;
    @FXML
    ListView listView;
    @FXML
    Label titleLabel;
    public static String mode;
    String modeFile = "defs.json";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addButton.setOnAction(value -> {
            String s = pickFolder("Add Folder", Util.home, addButton.getScene().getWindow());
            write(s, modeFile);
            load(modeFile);
        });
        if (mode.equalsIgnoreCase("ignore")) {
            titleLabel.setText("Ignore List");
            load(Util.home + "\\" + "fs-ignore-list.json");
        } else if (mode.equalsIgnoreCase("watch")) {
            titleLabel.setText("Watch List");
            load(Util.home + "\\" + "fs-watch-list.json");
        }
    }

    void load(String str) {
        modeFile = str;
        listView.getItems().clear();
        File datFile = new File(str);
        List<String> items = new ArrayList<>();
        if (datFile.exists()) {
            FileReader mReader = null;
            try {
                mReader = new FileReader(datFile.getAbsolutePath());
                JsonArray jsonArray = new JsonArray();
                JsonElement jsonElement = JsonParser.parseReader(mReader);
                if (jsonElement.isJsonArray()) {
                    jsonArray = jsonElement.getAsJsonArray();
                } else {
                    jsonArray.add("Empty or Lost File");
                }
                for (int i = 0; i < jsonArray.size(); i++) {
                    items.add(jsonArray.get(i).getAsString());
                }
                listView.getItems().addAll(items);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AddListController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    mReader.close();
                } catch (IOException ex) {
                    Logger.getLogger(AddListController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    void write(String toWrite, String path) {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(toWrite);

        FileWriter fileWriter = null;
        try {
            File file = new File(path);
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
}
