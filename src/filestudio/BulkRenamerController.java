/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Bulk Renamer Controller class
 *
 * @author Abraham Moruri : abummoja3@gmail.com
 */
public class BulkRenamerController implements Initializable, GlobalVars {
//browseBtn, autoWordBtn, newNameBtn

    @FXML
    TextField dirPathTbx;
    @FXML
    TextField oldWordsTbx;
    @FXML
    TextField newWordTbx;
    @FXML
    Button browseBtn;
    @FXML
    Button autoWordBtn;
    @FXML
    Button newNameBtn;
    public static String path;
    StringBuilder sBuilder = new StringBuilder();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dirPathTbx.setText(path);
        autoWordBtn.setOnMouseClicked(value -> {
            if (dirPathTbx.getText() == null) {
                alert("Empty Path", "Enter path to folder", "Folder path is null", AlertType.ERROR);
            }
            if (new File(path).isDirectory()) {
                for (File f : new File(path).listFiles()) {
                    sBuilder.append(f.getName()).append(" ");
                }
            }
            oldWordsTbx.setText(findMostCommonWord(sBuilder));
        });
        browseBtn.setOnMouseClicked(val -> {
            pickDir(dirPathTbx, "Bulk Rename - Pick Folder", Util.home, dirPathTbx.getScene().getWindow());
        });
        newNameBtn.setOnMouseClicked(value -> {
            newWordTbx.setText(new File(dirPathTbx.getText() != null ? dirPathTbx.getText() : Util.home).getName());
        });
    }

    public static String findMostCommonWord(StringBuilder text) {
        String[] words = text.toString().split(" (?=[^\\w])"); // Split non space if followed by a non word char
        Map<String, Integer> wordCount = new HashMap<>();

        // Count word frequency
        for (String word : words) {
            word = word.toLowerCase(); // Optional: To make it case-insensitive
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }

        // Find the most common word
        String mostCommon = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostCommon = entry.getKey();
                maxCount = entry.getValue();
            }
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        return mostCommon;
    }
}
