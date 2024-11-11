/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class MetroPanelController implements Initializable {

    @FXML
    AnchorPane metroAnchor;
    @FXML
    GridPane homeGrid;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        metroAnchor.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        //homeGrid.set
        String[] titles = {"Bulk Renamer", "Duplicate Finder", "File Organizer", "Archive Handler", "Media Upscale"};
        String[] descriptions = {"Rename multiple files based on custom or default criteria", "Find and optionally delete duplicate files occupying useful space on your storage device",
            "Organize files based on custom or default criteria (move files of a certain type such as videos into a common folder etc.), helps to find files faster",
            "Compress/Archive and Extract/Unarchive files and folders into all archive formats, supports ultra compression",
            "Improve video/image quality using advanced image processing technology and OpenUpscaler (improve videos from 360p to 1080p or 4K)"};
        String[] imagePaths = {"FileStudioMainIcon.png", "FileStudioMainIcon.png", "FileStudioMainIcon.png", "FileStudioMainIcon.png", "FileStudioMainIcon.png"};

        try {
            for (int i = 0; i < titles.length; i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeGridItem.fxml"));
                AnchorPane itemBox = loader.load();

                HomeGridItemController controller = loader.getController();
                controller.setData(imagePaths[i], titles[i], descriptions[i]);

                // Add click event to print the name
                String itemName = titles[i];
                controller.setOnItemClicked(() -> System.out.println(itemName));

                // Place the item in the GridPane at the correct position
                homeGrid.add(itemBox, i % 3, i / 3); // adjust columns and rows as needed
                JMetro gItem = new JMetro();
                homeGrid.getStyleClass().add(JMetroStyleClass.ALTERNATING_ROW_COLORS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
