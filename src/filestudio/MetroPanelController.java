/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio;

import com.jfoenix.controls.JFXSnackbar;
import filestudio.modules.AddListController;
import filestudio.modules.DuplicateFinderController;
import filestudio.modules.OrganizerController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class MetroPanelController implements Initializable, GlobalVars {

    @FXML
    AnchorPane metroAnchor;
    @FXML
    GridPane homeGrid;
    @FXML
    GridPane diskGrid;
    @FXML
    TabPane tabHolder;
    @FXML
    TextField topBarPath;
    @FXML
    Accordion homeAccordion;
    @FXML
    TitledPane taskTitlePane;
    @FXML
    TitledPane diskTitlePane;
    @FXML
    Button topBarBrowseBtn;
    UserSettings uss = new UserSettings();

    public static String activeDir;

    String[] titles = {"Bulk Renamer", "Duplicate Finder", "File Organizer", "Archive Handler", "Media Upscale", "Onedrive", "Dropbox", "Google Drive", "Repo Manager", "Dir Tree"};
    String[] descriptions = {"Rename multiple files based on custom or default criteria", "Find and optionally delete duplicate files occupying useful space on your storage device",
        "Organize files based on custom or default criteria (move files of a certain type such as videos into a common folder etc.), helps to find files faster",
        "Compress/Archive and Extract/Unarchive files and folders into all archive formats, supports ultra compression",
        "Improve video/image quality using advanced image processing technology and OpenUpscaler (improve videos from 360p to 1080p or 4K)",
        "Link to one drive",
        "Connect with a drop box account",
        "Connect to GDDR",
        "Scan and manage local git repositories easily with GUI.", "Take a snapshot of your current directory tree. Similar to indexing."};
    String[] imagePaths = {"renamer.png", "duplicate.png", "organizer.png", "task_archiver.png", "upscaler.png", "onedrive.png", "Dropbox250.png", "gddr.png", "file_code.png", "ic_dir_branch.png"};

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        metroAnchor.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        homeGrid.getStyleClass().add(JMetroStyleClass.ALTERNATING_ROW_COLORS);
        homeAccordion.expandedPaneProperty().setValue(homeAccordion.getPanes().get(0));
        topBarBrowseBtn.setOnAction(evt -> {
            pickDir(topBarPath, "Pick Folder", Util.home, topBarPath.getScene().getWindow());
            activeDir = topBarPath.getText();
        });
        if (rb != null && rb.containsKey("dir")) {
            iUpdatePath(rb.getString("dir"));
            System.out.println("rb is not null - " + rb.getString("dir"));
        }
        //notify("process \"File-Studio\" started", false);
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
        MenuItem newWindow = new MenuItem("New Window");
        MenuItem exitMenu = new MenuItem("Exit");
        fileMenu.getItems().addAll(openDirMenu, createArchMenu, extractArchMenu, newWindow, exitMenu);
        openDirMenu.setOnAction(evt -> {
            pickDir(topBarPath, "Pick Folder", Util.home, topBarPath.getScene().getWindow());
            activeDir = topBarPath.getText();
        });
        exitMenu.setOnAction(value -> {
            //TODO: check if tasks are running and optionally ask (Do you want to exit?) with a Dont show this again checkbox
            System.exit(0);
        });
        //tool menu
        MenuItem ignoreMenu = new MenuItem("Ignore List");
        MenuItem watchMenu = new MenuItem("Watch List");
        tasksMenu.getItems().addAll(ignoreMenu, watchMenu);
        ignoreMenu.setOnAction(value -> {
            openSesame("ignore");
        });
        watchMenu.setOnAction(value -> {
            openSesame("watch");
        });
        //window menu
        MenuItem maximizeMenu = new MenuItem("Maximize");
        MenuItem restoreMenu = new MenuItem("Restore");
        MenuItem prefMenu = new MenuItem("Settings");
        windowMenu.getItems().addAll(maximizeMenu, restoreMenu, prefMenu);
        prefMenu.setOnAction(value -> {
            launchSettings();
        });
        //help menu [how to, source, check updates, about]
        MenuItem howMenu = new MenuItem("User Guide");
        MenuItem srcMenu = new MenuItem("View Source");
        MenuItem updatesMenu = new MenuItem("Check for Updates");
        MenuItem donateMenu = new MenuItem("Donate");
        MenuItem aboutMenu = new MenuItem("About");
        helpMenu.getItems().addAll(howMenu, srcMenu, updatesMenu, donateMenu, aboutMenu);
        srcMenu.setOnAction(value -> {
            browse("https://github.com/abraham-ny/File-Studio");
        });
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
                String icon = imagePaths[i];
                controller.setOnItemClicked(() -> {
                    try {
                        addTab(itemName, desc, icon);
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
        Platform.runLater(() -> {
            Scene scene = metroAnchor.getScene();

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
                        iUpdatePath(firstDir.getAbsolutePath());
                    }
                } else if (dboard.hasString()) {
                    iUpdatePath(dboard.getString());
                } else if (dboard.hasUrl()) {
                    iUpdatePath(dboard.getUrl());
                }
                evt.setDropCompleted(true);
                evt.consume();
            });
        });
    }

    void addTab(String name, String desc, String icon) throws IOException {
        Tab nT = new Tab(name);
        switch (name) {
            case "Bulk Renamer":
                BulkRenamerController.path = topBarPath.getText();
                AnchorPane renamerParent = FXMLLoader.load(getClass().getResource("BulkRenamer.fxml"));
                nT.setContent(renamerParent);
                break;
            //File Organizer, Archive Handler, Media Upscale
            case "Duplicate Finder":
                DuplicateFinderController.mPath = topBarPath.getText();
                AnchorPane dupeParent = FXMLLoader.load(getClass().getResource("modules/DuplicateFinder.fxml"));
                nT.setContent(dupeParent);
                break;
            case "File Organizer":
                OrganizerController.mPath = topBarPath.getText();
                AnchorPane orgParent = FXMLLoader.load(getClass().getResource("modules/Organizer.fxml"));
                nT.setContent(orgParent);
                break;
            case "Archive Handler":
                AnchorPane archParent = FXMLLoader.load(getClass().getResource("modules/Archiver.fxml"));
                nT.setContent(archParent);
                break;
            case "Media Upscale":
                AnchorPane upscaleParent = FXMLLoader.load(getClass().getResource("modules/Upscaler.fxml"));
                nT.setContent(upscaleParent);
                break;
            case "Repo Finder":
                AnchorPane repoParent = FXMLLoader.load(getClass().getResource("modules/RepoFinder.fxml"));//repoman (repoman.exe)
                nT.setContent(repoParent);
                break;
            //"Onedrive", "Dropbox", "Google Drive"
            case "Onedrive":
            case "Dropbox":
            case "Google Drive":
                AnchorPane serviceParent = FXMLLoader.load(getClass().getResource("modules/LoadingService.fxml"));
                nT.setContent(serviceParent);
                break;
            case "Dir Tree":
                AnchorPane treesParent = FXMLLoader.load(getClass().getResource("modules/DirTrees.fxml"));
                nT.setContent(treesParent);
                break;
        }
        Tooltip toolTip = new Tooltip(desc);
        nT.setTooltip(toolTip);
        ImageView ic = new ImageView();
        ic.setFitWidth(20);
        ic.setFitHeight(20);
        ic.maxWidth(ic.getFitWidth());
        ic.maxHeight(ic.getFitHeight());
        ic.setImage(new Image(getClass().getResourceAsStream(icon)));
        nT.setGraphic(ic);
        nT.setClosable(true);
        tabHolder.getTabs().add(nT);
        tabHolder.getSelectionModel().select(nT);
        ContextMenu contextMenu = new ContextMenu();
        MenuItem detachItem = new MenuItem("Detach");
        detachItem.setOnAction(event -> {
            detachTab(nT);
            tabHolder.getTabs().remove(nT);
        });
        MenuItem closeItem = new MenuItem("Close Others");
        closeItem.setOnAction(event -> {
            tabHolder.getTabs().remove(nT);
        });
        MenuItem closeOthers = new MenuItem("Close");
        closeItem.setOnAction(event -> {
            tabHolder.getTabs().retainAll(tabHolder.getTabs().get(0), nT);
            //tabHolder.getTabs().retainAll(tabHolder.getSelectionModel().getSelectedItem());
        });
        contextMenu.getItems().addAll(detachItem, closeOthers, closeItem);
        nT.contextMenuProperty().set(contextMenu);
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

    void iUpdatePath(String newPath) {
        topBarPath.setText(newPath);
        activeDir = topBarPath.getText();
        //new MetroPanelController().notify(newPath, false);
    }

    //DONE: Apply theme based on prefs
    private void detachTab(Tab tab) {
        Stage detachedStage = new Stage();
        detachedStage.setTitle(tab.getText() + " - FileStudio: Detached");
        VBox content = new VBox(tab.getContent());
        Scene detachedScene = new Scene(content, 500, 400);
        JMetro metro = new JMetro(uss.getStyle());
        metro.setScene(detachedScene);
        content.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        Image i = new Image(getClass().getResourceAsStream("filestudio.png"));
        detachedStage.getIcons().add(i);
        detachedStage.setScene(detachedScene);
        detachedStage.show();
    }

    void openSesame(String str) {
        try {
            AddListController.mode = str;
            Parent parent = FXMLLoader.load(getClass().getResource("modules/AddList.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            JMetro metro = new JMetro(uss.getStyle());
            metro.setScene(scene);
            Image i = new Image(getClass().getResourceAsStream("filestudio.png"));
            stage.getIcons().add(i);
            stage.setTitle(str);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MetroPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void launchSettings() {
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("SettingsUI.fxml"));
            Scene scene = new Scene(root);
            UserSettings uss = new UserSettings();
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
            stage.setTitle("FileStudio Settings");
            stage.initStyle(StageStyle.UNDECORATED);
            Image i = new Image(getClass().getResourceAsStream("FileStudioOtherIcon.png"));
            stage.getIcons().add(i);
            stage.show();
            SettingsUIController.setStage(stage);
        } catch (IOException e) {
            fslog("Fxml settings err Abu, " + e.getMessage() + e.getCause().toString());
        }
    }

}
