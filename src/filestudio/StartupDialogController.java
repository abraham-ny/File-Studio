/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class StartupDialogController implements Initializable, GlobalVars {

    @FXML
    TextField dirPathTbx;
    @FXML
    CheckBox showBox;
    @FXML
    AnchorPane anchorPane;
    @FXML
    ListView histList;
    @FXML
    Button withBtn;
    UserSettings uss = new UserSettings();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        anchorPane.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        showBox.setSelected(uss.showStartupScreen);
        histList.getSelectionModel().selectedItemProperty().addListener(listener -> {
            dirPathTbx.setText((String) histList.getSelectionModel().getSelectedItem().toString());
        });
        Platform.runLater(() -> {
            checkHistory();
            withBtn.setDisable(true);
            dirPathTbx.textProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    if (new File(dirPathTbx.getText()).isDirectory()) {
                        addHistory(dirPathTbx.getText());
                        withBtn.setDisable(false);
                    } else {
                        withBtn.setDisable(true);
                    }
                }
            });
            Scene scene = anchorPane.getScene();//new Scene(anchorPane);
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
                        dirPathTbx.setText(firstDir.getAbsolutePath());
                    }
                } else if (dboard.hasString()) {
                    dirPathTbx.setText(dboard.getString());
                } else if (dboard.hasUrl()) {
                    dirPathTbx.setText(dboard.getUrl());
                    alert("DnD Remote - FileStudio", "Experimental Feature", "Remote device support is currently unstable, proceed with caution.", Alert.AlertType.WARNING);
                } else {
                    alert("DnD Feature - FileStudio", "What's this?", "Unsupported item", Alert.AlertType.INFORMATION);
                }
                evt.setDropCompleted(true);
                evt.consume();
            });
        });
    }

    private void addHistory(String activeDir) {
        JsonHandler jh = new JsonHandler();
        List<String> sel = new ArrayList<>();
        sel.add(activeDir);
        for (String sd : jh.readFromJson()) {
            if (!sd.equals(activeDir)) {
                sel.add(sd);
            }
        }
        jh.deleteData();
        jh.writeToJson(sel);
        checkHistory();
    }

    //Method to add history list to listview
    public void checkHistory() {
        histList.getItems().clear();
        try {
            //load json data into listview.
            JsonHandler jh = new JsonHandler();
            List<String> hist = jh.readFromJson();
            if (hist.isEmpty()) {
                histList.getItems().clear();
                histList.getItems().add("Empty List");
                return;
            } else {
                for (String s : hist) {
                    histList.getItems().add(s);
                }
            }
        } catch (Exception ex) {
            histList.getItems().add("Error");
        }
    }

    public void pickNewDir() {
        pickDir(dirPathTbx, "Pick a folder - FileStudio", Util.home, dirPathTbx.getScene().getWindow());
    }

    public void continueWith() {
        try {
            //Launch metro panel without params
            openSelected(dirPathTbx.getText());
        } catch (IOException ex) {
            alert("Open Without Args", "IO Error", ex.getMessage(), Alert.AlertType.ERROR);
            System.out.println("ABU::Err-\n" + ex.getMessage() + System.lineSeparator() + ex.getCause().toString());
            Logger.getLogger(StartupDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void continueWithout() {
        try {
            //Launch metro panel without params
            openSelected("");
        } catch (IOException ex) {
            alert("Open Without Args", "IO Error", ex.getMessage(), Alert.AlertType.ERROR);
            System.out.println("ABU::Err-\n" + ex.getMessage() + System.lineSeparator() + ex.getCause().toString());
            Logger.getLogger(StartupDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void openSelected(String vars) throws IOException {
        //Launch metro panel with string args
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MetroPanel.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        JMetro metro = new JMetro(Style.DARK);
        metro.setScene(scene);
        Image i = new Image(getClass().getResourceAsStream("filestudio.png"));
        stage.getIcons().add(i);
        if (vars == null || vars.equals("")) {
            stage.setTitle("FileStudio v2");
        } else {
            ResourceBundle rb = new ListResourceBundle() {
                @Override
                protected Object[][] getContents() {
                    return new Object[][]{
                        {"dir", vars}
                    };
                }
            };
            loader.setResources(rb);
        }
        stage.setMaximized(true);
        stage.show();
    }

    public void showOrNot() {
        uss.setBool("showstart", showBox.isSelected());
    }

}
