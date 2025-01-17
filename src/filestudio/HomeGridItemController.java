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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class HomeGridItemController implements Initializable {

    @FXML
    private ImageView itemImage;
    @FXML
    private Label itemTitle;
    @FXML
    private Label itemDescription;
    @FXML
    AnchorPane homeGridParent;

    // Set data for each item
    public void setData(String image, String title, String description) {
        itemImage.setImage(new Image(getClass().getResourceAsStream(image)));
        itemTitle.setText(title);
        itemDescription.setText(description);
    }

    // Add click event
    public void setOnItemClicked(Runnable action) {
        itemTitle.getParent().setOnMouseClicked(e -> action.run());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //itemTitle.styleProperty().set("-fx-background-color: dodgerblue;");
        homeGridParent.setOnMouseEntered(value -> {
            homeGridParent.styleProperty().set("-fx-background-color: dodgerblue;");
        });
        homeGridParent.setOnMouseExited(value -> {
            homeGridParent.styleProperty().set("-fx-background-color: transparent;");
        });
    }
}
