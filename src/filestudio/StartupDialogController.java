/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import jfxtras.styles.jmetro.JMetroStyleClass;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class StartupDialogController implements Initializable, GlobalVars {

    /**
     * Initializes the controller class.
     */
    @FXML
    TextField dirPathTbx;
    @FXML
    CheckBox showBox;
    @FXML
    AnchorPane anchorPane;
    UserSettings uss = new UserSettings();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        anchorPane.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        showBox.setSelected(uss.showStartupScreen);
    }

    public static void updatePath(String str) {
        //dirPathTbx.setText(str);
    }

    public void pickNewDir() {
        pickDir(dirPathTbx, "Pick a folder - FileStudio", null, dirPathTbx.getScene().getWindow());
    }

    public void continueWithout() {
        //Launch metro panel without params
    }

    public void openSelected() {
        //Launch metro panel with string args
    }

    public void showOrNot() {
        uss.setBool("showstart", showBox.isSelected());
    }

}
