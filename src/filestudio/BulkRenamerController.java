/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class BulkRenamerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static String path;
    StringBuilder sBuilder = new StringBuilder();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public static String findMostCommonWord(StringBuilder text) {
        String[] words = text.toString().split("\\W+"); // Split by non-word characters
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
        }

        return mostCommon;
    }
}
