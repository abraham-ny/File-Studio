/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package filestudio;

import java.io.File;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

/**
 *
 * @author Admin
 */
public interface GlobalVars {

    default void pickDir(TextField tf, String title, String initialD, Window owner) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        if (initialD != null || !"none".equals(initialD)) {
            dirChooser.setInitialDirectory(new File(initialD));
        }
        dirChooser.setTitle(title);
        File selectedFolder = dirChooser.showDialog(owner);
        if (selectedFolder != null && selectedFolder.exists()) {
            tf.setText(selectedFolder.getPath());
        }
    }

    default void alert(String title, String header, String message, Alert.AlertType type) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(message);
            ButtonType yesBtn = new ButtonType("Ok");
            //ButtonType noBtn = new ButtonType("Close");
            alert.getButtonTypes().setAll(yesBtn);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == yesBtn) {
                    alert.close();
                } else {
                    //return;
                    alert.close();
                }
            }
        });
    }
}
