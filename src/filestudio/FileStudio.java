/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Admin
 */
public class FileStudio extends Application {

    UserSettings uss = new UserSettings();
    FLogger logger = new FLogger();
    Stage istage;

    @Override
    public void start(Stage stage) throws Exception {
        //TO-DO: Read prefs to check if user has selected newUI or just launch old Ui
        //if prefs newui(...try(load newui))else (try...old ui)
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            Scene scene = new Scene(root);
            //stylesheets="@style.css"
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

            //getClass().getResource("style.css").getFile()
            stage.setScene(scene);
            //stage.setIconified(true); //launches the app in minimized state
            //File f = new File(getClass().getResource("FileStudioMain.ico").getFile());
            //.ico files don't work, use .png or .jpg
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

    private static final String REPO_API_URL = "https://api.github.com/repos/abummoja/file-studio/releases/latest";

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

    void checkForUpdates() {
        try {
            // Get the latest release from GitHub
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
                // Download the filestudio.exe
                //downloadFileFromRelease(latestVersion, "filestudio.exe");
            } else {
                logger.Log("launcher - You are using the latest version: " + ver);
                alert("Updater", "Latest version is : " + latestVersion, "You are using the latest version.", Alert.AlertType.INFORMATION);
            }
        } catch (IOException e) {
            logger.Log("launcher - FAILED TO CHECK FOR UPDATES!");
            //e.printStackTrace();
            alert("Network Error", "Failed to check for updates!", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void launchGUI(Stage stage) throws Exception {
        //TO-DO: Read prefs to check if user has selected newUI or just launch old Ui
        //if prefs newui(...try(load newui))else (try...old ui)
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            Scene scene = new Scene(root);
            //stylesheets="@style.css"
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

            //getClass().getResource("style.css").getFile()
            stage.setScene(scene);
            //stage.setIconified(true); //launches the app in minimized state
            //File f = new File(getClass().getResource("FileStudioMain.ico").getFile());
            //.ico files don't work, use .png or .jpg
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

    private void alert(String title, String header, String message, Alert.AlertType type) {
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
                try {
                    launchGUI(istage);
                } catch (Exception ex) {
                    Logger.getLogger(FileStudio.class.getName()).log(Level.CONFIG, null, ex);
                    logger.Log(ex.toString());
                }
                alert.close();
            } else {
                try {
                    //return;
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
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
