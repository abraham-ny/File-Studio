package filestudio;

import com.google.gson.JsonObject;
import static filestudio.FXMLDocumentController.ver;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 * Entry point/main launcher class responsible for receiving command line
 * arguments(Files and other items or instructions) via main(String[]
 * parameters)
 *
 * @author Abraham Moruri
 */
public class FileStudio extends Application {

    UserSettings uss = new UserSettings();
    FLogger logger = new FLogger();
    Stage istage;

    /**
     * Starts the JavaFX application.
     * Sets up the main stage with the appropriate UI based on user settings.
     *
     * @param stage The primary stage for this application.
     * @throws Exception If loading the FXML or other initialization fails.
     */
    @Override
    public void start(Stage stage) throws Exception {
        if (uss.useMetro.equals("yes")) {
            Parent root = FXMLLoader.load(getClass().getResource("MetroPanel.fxml"));
            Scene scene = new Scene(root);
            Platform.runLater(() -> {
                scene.setOnDragOver(evt -> {
                    if (evt.getDragboard().hasFiles() || evt.getDragboard().hasString() || evt.getDragboard().hasUrl()) {
                        evt.acceptTransferModes(TransferMode.COPY);
                    }
                    evt.consume();
                });
                scene.setOnDragDropped(evt -> {
                    Dragboard dboard = evt.getDragboard();
                    if (dboard.hasFiles()) {
                        File firstDir = new File(dboard.getFiles().get(0).getPath());
                        if (new File(dboard.getFiles().get(0).getPath()).isDirectory()) {
                            MetroPanelController.updatePath(firstDir.getAbsolutePath());
                        }
                    } else if (dboard.hasString()) {
                        MetroPanelController.updatePath(dboard.getString());
                    } else if (dboard.hasUrl()) {
                        MetroPanelController.updatePath(dboard.getUrl());
                    }
                    evt.setDropCompleted(true);
                    evt.consume();
                });
            });
            stage.setScene(scene);
            JMetro metro = new JMetro(Style.DARK);
            metro.setScene(scene);
            Image i = new Image(getClass().getResourceAsStream("filestudio.png"));
            stage.getIcons().add(i);
            stage.setTitle("FileStudio v2");
            stage.resizableProperty().addListener(listener -> {
                logger.Log("RESIZING -width " + stage.getWidth() + " -height " + stage.getHeight());
            });
            stage.setMaximized(true);
            stage.show();

        } else {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Scene scene = new Scene(root);
                switch (uss.theme) {
                    case "dark":
                        scene.getStylesheets().add("filestudio/style.css");
                        break;
                    case "light":
                        scene.getStylesheets().add("filestudio/light.css");
                        break;
                    default:
                        scene.getStylesheets().add("filestudio/style.css");
                }
                stage.setScene(scene);
                Image i = new Image(getClass().getResourceAsStream("FileStudioMainIcon.png"));
                stage.getIcons().add(i);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setResizable(false);
                stage.maximizedProperty().addListener((obs, oldv, newv) -> {
                    if (newv) {
                        stage.setMaximized(false);
                    }
                });
                stage.show();
            } catch (IOException e) {
                System.out.println("File-Studio init Fxml err Abu, " + e.getMessage() + e.getCause().toString());
                alert("File-Studio init Fxml err Abu - " + e.getMessage(), e.getMessage() + System.lineSeparator() + e.getLocalizedMessage(), e.getCause().toString(), Alert.AlertType.ERROR);
            }
        }
    }

    private static final String REPO_API_URL = "https://api.github.com/repos/abraham-ny/file-studio/releases/latest";

    /**
     * Retrieves the latest release tag from the GitHub repository.
     *
     * @return The latest release tag as a String.
     * @throws IOException If an I/O error occurs during the HTTP request.
     */
    private static String getLatestReleaseTag() throws IOException {
        URL url = new URL(REPO_API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

        Scanner scanner = new Scanner(connection.getInputStream());
        StringBuilder response = new StringBuilder();

        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.getAsJsonObject(response.toString());
        return jsonResponse.get("tag_name").toString();
    }

    String url = "https://sourceforge.net/projects/filestudio/";

    /**
     * Checks for updates by comparing the current version with the latest release.
     * Displays alerts to the user about update availability.
     */
    void checkForUpdates() {
        try {
            String latestVersion = getLatestReleaseTag();
            logger.Log("Checking for updates on : " + ver);
            if (!ver.equals(latestVersion)) {
                logger.Log("New version available: " + latestVersion);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update " + latestVersion);
                alert.setHeaderText("A new version of FileStudio is available!");
                alert.setContentText("Bug Fixes, Improvements, New Features and more...");
                ButtonType yesBtn = new ButtonType("Update");
                ButtonType noBtn = new ButtonType("Close");
                alert.getButtonTypes().setAll(yesBtn, noBtn);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent()) {
                    if (result.get() == yesBtn) {
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                            try {
                                Desktop.getDesktop().browse(new URI(url));
                            } catch (URISyntaxException ex) {
                                alert("Browser Error", ex.getMessage(), ex.getReason(), Alert.AlertType.ERROR);
                                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                alert("Browser Error", ex.getMessage(), "IOException", Alert.AlertType.ERROR);
                                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            alert("Browser Error", "Failed to launch browser", url, Alert.AlertType.ERROR);
                        }
                    } else if (result.get() == noBtn) {
                        try {
                            launchGUI(istage);
                        } catch (Exception ex) {
                            Logger.getLogger(FileStudio.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } else {
                logger.Log("launcher - You are using the latest version: " + ver);
                alert("Updater", "Latest version is : " + latestVersion, "You are using the latest version.", Alert.AlertType.INFORMATION);
            }
        } catch (IOException e) {
            logger.Log("launcher - FAILED TO CHECK FOR UPDATES!");
            alert("Network Error", "Failed to check for updates!", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Launches the main GUI stage.
     *
     * @param stage The stage to launch.
     * @throws Exception If loading the FXML or other initialization fails.
     */
    public void launchGUI(Stage stage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            Scene scene = new Scene(root);
            switch (uss.theme) {
                case "dark":
                    scene.getStylesheets().add("filestudio/style.css");
                    break;
                case "light":
                    scene.getStylesheets().add("filestudio/light.css");
                    break;
                default:
                    scene.getStylesheets().add("filestudio/style.css");
            }
            stage.setScene(scene);
            Image i = new Image(getClass().getResourceAsStream("FileStudioMainIcon.png"));
            stage.getIcons().add(i);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.maximizedProperty().addListener((obs, oldv, newv) -> {
                if (newv) {
                    stage.setMaximized(false);
                }
            });
            stage.show();
        } catch (IOException e) {
            System.out.println("Fxml err Abu, " + e.getMessage() + e.getCause().toString());
        }
    }

    /**
     * Displays an alert dialog to the user.
     *
     * @param title The title of the alert.
     * @param header The header text of the alert.
     * @param message The content message of the alert.
     * @param type The type of the alert.
     */
    private void alert(String title, String header, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        ButtonType yesBtn = new ButtonType("Ok");
        alert.getButtonTypes().setAll(yesBtn);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == yesBtn) {
                try {
                    launchGUI(istage);
                } catch (Exception ex) {
                    Logger.getLogger(FileStudio.class.getName()).log(Level.CONFIG, null, ex);
                    logger.Log(ex.toString());
                }
                alert.close();
            } else {
                try {
                    launchGUI(istage);
                } catch (Exception ex) {
                    Logger.getLogger(FileStudio.class.getName()).log(Level.WARNING, null, ex);
                    logger.Log(ex.toString());
                }
                alert.close();
            }
        }
    }

    /**
     * The entry point that receives commands entered via cmd passed to
     * filestudio.exe as in {filestudio upscaler} or {filestudio organizer} or
     * {filestudio C://File/Path/Dir}
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
