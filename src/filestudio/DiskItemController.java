/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filestudio;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class DiskItemController implements Initializable {

    @FXML
    private ImageView diskIcon;
    @FXML
    private Label diskNameLabel;
    @FXML
    private Label diskDescriptionLabel;
    @FXML
    AnchorPane diskAnchorPane;
    @FXML
    ProgressBar diskProgressBar;

    // Set data for each item
    public void setData(String image, String title, String description, double np) {
        diskIcon.setImage(new Image(getClass().getResourceAsStream(image)));
        diskNameLabel.setText(title);
        diskDescriptionLabel.setText(description);
        diskProgressBar.setProgress(np);
    }

    // Add click event
    public void setOnItemClicked(Runnable action) {
        diskNameLabel.getParent().setOnMouseClicked(e -> action.run());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //itemTitle.styleProperty().set("-fx-background-color: dodgerblue;");
        diskAnchorPane.setOnMouseEntered(value -> {
            diskAnchorPane.styleProperty().set("-fx-background-color: dodgerblue;");
        });
        diskAnchorPane.setOnMouseExited(value -> {
            diskAnchorPane.styleProperty().set("-fx-background-color: transparent;");
        });
    }
}
