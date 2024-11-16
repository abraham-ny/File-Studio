/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio;

import com.jfoenix.controls.JFXSnackbar;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import jfxtras.styles.jmetro.JMetro;
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
    @FXML
    GridPane diskGrid;
    @FXML
    TabPane tabHolder;
    @FXML
    static TextField topBarPath;
    @FXML
    Accordion homeAccordion;

    public static String activeDir;

    String[] titles = {"Bulk Renamer", "Duplicate Finder", "File Organizer", "Archive Handler", "Media Upscale", "Onedrive", "Dropbox", "Google Drive"};
    String[] descriptions = {"Rename multiple files based on custom or default criteria", "Find and optionally delete duplicate files occupying useful space on your storage device",
        "Organize files based on custom or default criteria (move files of a certain type such as videos into a common folder etc.), helps to find files faster",
        "Compress/Archive and Extract/Unarchive files and folders into all archive formats, supports ultra compression",
        "Improve video/image quality using advanced image processing technology and OpenUpscaler (improve videos from 360p to 1080p or 4K)",
        "Link to one drive",
        "Connect with a drop box account",
        "Connect to GDDR"};
    String[] imagePaths = {"renamer.png", "duplicate.png", "organizer.png", "task_archiver.png", "upscaler.png", "onedrive.png", "Dropbox250.png", "gddr.png"};

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        metroAnchor.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        homeGrid.getStyleClass().add(JMetroStyleClass.ALTERNATING_ROW_COLORS);
        homeAccordion.expandedPaneProperty().setValue(homeAccordion.getPanes().get(0));
        notify("process \"File-Studio\" started", false);
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu tasksMenu = new Menu("Tools");//add shortcuts to stuff like create arch, bulk delete and other tasks
        Menu windowMenu = new Menu("Window");
        Menu helpMenu = new Menu("Help");
        SeparatorMenuItem sm = new SeparatorMenuItem();
        //file menu
        MenuItem openDirMenu = new MenuItem("Open Folder");
        MenuItem createArchMenu = new MenuItem("Create Archive (Compress folder)");
        MenuItem extractArchMenu = new MenuItem("Extract Archive");
        MenuItem exitMenu = new MenuItem("Exit");
        fileMenu.getItems().addAll(openDirMenu, createArchMenu, extractArchMenu, exitMenu);
        //window menu
        MenuItem maximizeMenu = new MenuItem("Maximize");
        MenuItem restoreMenu = new MenuItem("Restore");
        MenuItem prefMenu = new MenuItem("Settings");
        windowMenu.getItems().addAll(maximizeMenu, restoreMenu, prefMenu);
        //help menu [how to, source, check updates, about]
        MenuItem howMenu = new MenuItem("How To");
        MenuItem srcMenu = new MenuItem("View Source");
        MenuItem updatesMenu = new MenuItem("Check for Updates");
        MenuItem donateMenu = new MenuItem("Donate");
        MenuItem aboutMenu = new MenuItem("About");
        helpMenu.getItems().addAll(howMenu, srcMenu, updatesMenu, donateMenu, aboutMenu);
        //menu bar
        menuBar.getMenus().addAll(fileMenu, tasksMenu, windowMenu, helpMenu);
        AnchorPane.setTopAnchor(menuBar, 0.0);
        AnchorPane.setLeftAnchor(menuBar, 0.0);
        AnchorPane.setRightAnchor(menuBar, 0.0);
        metroAnchor.getChildren().add(menuBar);
        //files of type combobox, use array of regex alongside array of combo items
        try {
            loadDisks();
            notify("Done loading disks", false);
        } catch (IOException ex) {
            notify("Disk load error : " + ex.getMessage(), true);
        }
        try {
            for (int i = 0; i < titles.length; i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeGridItem.fxml"));
                AnchorPane itemBox = loader.load();

                HomeGridItemController controller = loader.getController();
                controller.setData(imagePaths[i], titles[i], descriptions[i]);

                // Add click event to print the name
                String itemName = titles[i];
                String desc = descriptions[i];
                controller.setOnItemClicked(() -> {
                    try {
                        addTab(itemName, desc);
                        notify(String.format("Opened %s : %s", itemName, desc), false);
                    } catch (IOException ex) {
                        notify(ex.getMessage(), true);
                        Logger.getLogger(MetroPanelController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

                // Place the item in the GridPane at the correct position
                homeGrid.add(itemBox, i % 3, i / 3); // adjust columns and rows as needed
            }
        } catch (IOException e) {
            notify(e.getMessage(), true);
            e.printStackTrace();
        }
    }

    void addTab(String name, String desc) throws IOException {
        Tab nT = new Tab(name);
        switch (name) {
            case "Bulk Renamer":
                BulkRenamerController.path = "";
                AnchorPane renamerParent = FXMLLoader.load(getClass().getResource("BulkRenamer.fxml"));
                nT.setContent(renamerParent);
                break;
            //"Onedrive", "Dropbox", "Google Drive"
            case "Onedrive":
            case "Dropbox":
            case "Google Drive":
                AnchorPane serviceParent = FXMLLoader.load(getClass().getResource("../modules/LoadingService.fxml"));
                nT.setContent(serviceParent);
                break;
        }
        //after switch
        Tooltip toolTip = new Tooltip(desc);
        nT.setTooltip(toolTip);
        // nT.setGraphic(itm.getGraphic());
        tabHolder.getTabs().add(nT);
    }

    public void notify(String message, boolean err) {
        JFXSnackbar snackbar = new JFXSnackbar(metroAnchor);
        String style = "-fx-background-color: green;";
        if (err) {
            style = "-fx-background-color: red;";
        }
        Label lb = new Label(message);
        lb.styleProperty().set(style);

        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(lb));
    }

    public void notifyWithNode(Node node) {
        JFXSnackbar snackbar = new JFXSnackbar(metroAnchor);
        Node clone = node;
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(clone));
    }

    void loadDisks() throws IOException {
        File[] drives = File.listRoots();
        int i = 0;
        for (File drive : drives) {
            if (drive.getPath().equalsIgnoreCase(null) || drive.canRead() == false) {
                return;
            }
            i++;
            DiskInfo diskInfo = new DiskInfo(drive.getPath());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DiskItem.fxml"));
            AnchorPane itemBox = loader.load();

            DiskItemController controller = loader.getController();
            double np = Math.round((diskInfo.getFreeSpace() / 1024 / 1024) * 100) / 100; //ree space in hundreds (GB)
            String desc = diskInfo.path + "\n" + diskInfo.getName() + "\n" + np + " GB free";
            controller.setData("hdd.png", diskInfo.getDescription(), desc, np / 1000);
            controller.setOnItemClicked(() -> {
                notify(desc, false);
            });
            diskGrid.add(itemBox, i % 3, i / 3);
        }
    }

    public static void updatePath(String newPath) {
        topBarPath.setText(newPath);
        new MetroPanelController().notify(newPath, false);
    }
}
