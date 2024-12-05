/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
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
    @FXML
    ListView listView;
    @FXML
    ComboBox fileDateCbx;
    @FXML
    ComboBox fileSizeCbx;

    List<File> wordRemoverFileList = new ArrayList<>();

    public static String path;
    StringBuilder sBuilder = new StringBuilder();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dirPathTbx.setText(path);
        fileSizeCbx.getItems().addAll("Small (<1 MB)", "Medium (1-100 MB)", "Large (>100 MB)");
        if (dirPathTbx.getText() != null && new File(dirPathTbx.getText()).isDirectory()) {
            updateFilters();
        }
        autoWordBtn.setOnMouseClicked(value -> {
            if (dirPathTbx.getText() == null) {
                this.alert("Empty Path", "Enter path to folder", "Folder path is null", AlertType.ERROR);
            }
            if (new File(dirPathTbx.getText()).isDirectory()) {
                for (File f : new File(dirPathTbx.getText()).listFiles()) {
                    sBuilder.append(f.getName());
                }
            }
            oldWordsTbx.setText(findMostCommonWord(sBuilder));
        });
        browseBtn.setOnMouseClicked(val -> {
            pickDir(dirPathTbx, "Bulk Rename - Pick Folder", Util.home, dirPathTbx.getScene().getWindow());
            updateFilters();
        });
        newNameBtn.setOnMouseClicked(value -> {
            newWordTbx.setText(new File(dirPathTbx.getText() != null ? dirPathTbx.getText() : Util.home).getName());
        });
    }

    public static String findMostCommonWord(StringBuilder text) {
        String[] words = text.toString().split("\\W+");
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
            //System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        return mostCommon;
    }

    private void updateFilters() {
        Path folderPath = Paths.get(dirPathTbx.getText());
        // Extract unique dates from files
        Set<LocalDate> uniqueDates = getUniqueFileDates(folderPath);
        fileDateCbx.getItems().addAll(uniqueDates.stream().sorted().collect(Collectors.toList()));
    }

    public void removeWord() {
        try {
            FileRenamer renamer = new FileRenamer();
            String status = renamer.removeWordFromList(wordRemoverFileList, newWordTbx.getText(), oldWordsTbx.getText());
            //successOrNotRenamerLabel.setText(status);
            listView.getItems().clear();
        } catch (Exception f) {
        }
    }

    public void search() {
        //check if the String containing path to active dir is empty or null
        String activeDir = dirPathTbx.getText();
        if (activeDir != null && !activeDir.equals(" ")) {
            String keyWord = newWordTbx.getText();
            File directory = new File(activeDir);
            File[] directoryToSearch = directory.listFiles();
            //clear the list and listview as user types
            wordRemoverFileList.clear();
            listView.getItems().clear();
            //then update the list and listview with dir contents matching search criteria
            for (File file : directoryToSearch) {
                if (file.getName().contains(keyWord)) {
                    wordRemoverFileList.add(file);
                    listView.getItems().add(file.getName());
                }
            }
        }
    }
}
