/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package filestudio;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import filestudio.modules.AddListController;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 * Class GlobalVars holds common functions that shall be reused across multiple
 * classes. This reduces repetition.
 *
 * @author Abraham Moruri (github: abraham-ny)
 */
public interface GlobalVars {

    FLogger fsLogger = new FLogger();
    /**
     * @since v1.3.1 iPath by default is the dir from where the app was
     * launched, usually ProgramFiles(?x86)/FileStudio unless modified by user
     * on install. The purpose of this string is to hold the current selected
     * dir that will be passed to a module on launch.
     */
    public String iPath = System.getProperty("user.dir");

    default String currPath() {
        return iPath;
    }

    /**
     *
     * pickDir launches a folder picker bound to the owner window. initialD
     * (INITIAL DIR) is optional. The function automatically updates the
     * contents of text field tf to the path of selected dir.
     *
     * @param tf
     * @param title
     * @param initialD
     * @param owner
     */
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

    /**
     * launches a directory chooser bound to the owner window and with the
     * default location as the optional string initialD (INITIAL DIRECTORY)
     *
     * @param title
     * @param initialD
     * @param owner
     * @return
     */
    default String pickFolder(String title, String initialD, Window owner) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        if (initialD != null || !"none".equals(initialD)) {
            dirChooser.setInitialDirectory(new File(initialD));
        }
        dirChooser.setTitle(title);
        File selectedFolder = dirChooser.showDialog(owner);
        if (selectedFolder == null) {
            return null;
        }
        return selectedFolder.getAbsolutePath();
    }

    /**
     * Shows an alert dialog containing a header and message passed as
     * arguments. The alert type is also passed as an argument and the dialog
     * has a single button.
     *
     * @param title
     * @param header
     * @param message
     * @param type
     */
    default void alert(String title, String header, String message, Alert.AlertType type) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(message);
            fsLogger.Log(title, header, message);
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
     * @param folderPath : the path to extract date from.
     * @return uniqueDates : A Set"<"LocalDate">" object containing unique file
     * dates
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
                    //System.out.println(fileDate);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uniqueDates;
    }

    /**
     * creates a mini snack bar at the bottom of window (pane) that can either
     * be red (error) or green with the message passed and a true or false error
     * status
     *
     * @param message
     * @param err
     * @param view
     */
    default void notify(String message, boolean err, Pane view) {
        JFXSnackbar snackbar = new JFXSnackbar(view);
        String style = "-fx-background-color: green;";
        fslog(message);
        if (err) {
            style = "-fx-background-color: red;";
        }
        Label lb = new Label(message);
        lb.styleProperty().set(style);
        Duration d = Duration.seconds(3);
        SnackbarEvent evt = new SnackbarEvent(lb, d);
        snackbar.enqueue(evt);
    }

    default LocalDate localDateConverter(Path file) throws IOException {
        BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
        LocalDate fileDate = attrs.lastModifiedTime()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return fileDate;
    }

    default void browse(String url) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("File Studio: Launch Browser?");
        alert.setHeaderText("You are about to open the browser.\nWe need your confirmation because browsers are sometimes resource intensive.\nIf you wish to proceed, click the \"yes\" button.");
        alert.setContentText("Visit " + url + " ?");
        ButtonType yesBtn = new ButtonType("Yes");
        ButtonType copyBtn = new ButtonType("Copy Link To Clipboard");
        ButtonType noBtn = new ButtonType("No");
        alert.getButtonTypes().setAll(yesBtn, copyBtn, noBtn);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == copyBtn) {
                //copy to clipboard
                StringSelection sel = new StringSelection(url);
                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                cb.setContents(sel, null);
                //showNotification("FileStudio", "Link copied to clipboard!");
            } else if (result.get() == noBtn) {
                //clode dlg
                alert.close();
            } else if (result.get() == yesBtn) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        Desktop.getDesktop().browse(new URI(url));
                    } catch (URISyntaxException ex) {
                        alert("FSProc[URISE]", "Error", ex.getMessage(), Alert.AlertType.ERROR);
                        Logger
                                .getLogger(FXMLDocumentController.class
                                        .getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        alert("FSProc[IOE]", "Error", ex.getMessage(), Alert.AlertType.ERROR);
                        Logger
                                .getLogger(FXMLDocumentController.class
                                        .getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    alert("Unsupported Browser", "We could not find a browser", "Check that you have a browser", Alert.AlertType.ERROR);
                }
            } else {
                //return;
                alert.close();
            }
        }
    }

    default void listManager(String str) {
        try {
            AddListController.mode = str;
            Parent parent = FXMLLoader.load(getClass().getResource("modules/AddList.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            JMetro metro = new JMetro(Style.DARK);
            metro.setScene(scene);
            Image i = new Image(getClass().getResourceAsStream("filestudio.png"));
            stage.getIcons().add(i);
            stage.setTitle(str);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            alert("listman[IOE]", "Failed to load List Manager for " + str, ex.getMessage(), Alert.AlertType.ERROR);
            System.out.println("ABU: Organizer failed to launch addlist controller from GlobalVars because: " + ex.getMessage());
            Logger.getLogger(MetroPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    default void fslog(String... dat) {
        fsLogger.Log(dat);
    }

}
