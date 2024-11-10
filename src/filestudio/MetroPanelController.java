/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
    }

}
