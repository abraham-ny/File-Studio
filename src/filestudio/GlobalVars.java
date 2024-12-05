/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package filestudio;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import javafx.util.Duration;

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

    default String pickFolder(String title, String initialD, Window owner) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        if (initialD != null || !"none".equals(initialD)) {
            dirChooser.setInitialDirectory(new File(initialD));
        }
        dirChooser.setTitle(title);
        File selectedFolder = dirChooser.showDialog(owner);
        if (selectedFolder == null && !selectedFolder.exists()) {
            return null;
        }
        return selectedFolder.getAbsolutePath();
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

    /**
     * Extracts unique file dates from the given folder path.
     *
     * @param folderPath : the path to extract date from
     */
    default Set<LocalDate> getUniqueFileDates(Path folderPath) {
        Set<LocalDate> uniqueDates = new TreeSet<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath)) {
            for (Path file : stream) {
                if (Files.isRegularFile(file)) {
                    // Get last modified time of the file
                    BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
                    LocalDate fileDate = attrs.lastModifiedTime()
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    // Add to the set to ensure uniqueness
                    uniqueDates.add(fileDate);
                    System.out.println(fileDate);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uniqueDates;
    }

    default void notify(String message, boolean err, Pane view) {
        JFXSnackbar snackbar = new JFXSnackbar(view);
        String style = "-fx-background-color: green;";
        if (err) {
            style = "-fx-background-color: red;";
        }
        Label lb = new Label(message);
        lb.styleProperty().set(style);
        Duration d = Duration.seconds(3);
        SnackbarEvent evt = new SnackbarEvent(lb, d);
        snackbar.enqueue(evt);
    }
}
