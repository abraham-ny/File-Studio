/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio.modules;

import filestudio.FLogger;
import filestudio.GlobalVars;
import filestudio.Organizer;
import filestudio.UserSettings;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class OrganizerController implements Initializable, GlobalVars {

    Organizer iOrganizer = new Organizer();
    public static String mPath;
    String[] docs, pics, vids, auds, exes, archs;
    FLogger logger = new FLogger();
    UserSettings uss = new UserSettings();
    static String audex = "regex:.*(?i:mp3|ogg|avi|wav|flacc|aud|m4a|m3u)";
    static String videx = "regex:.*(?i:mp4|mkv|webm|ts|wmp|mov)";
    static String picex = "regex:.*(?i:jpg|jpeg|png|gif|bmp|jpe|jfif|ico)";
    static String docex = "regex:.*(?i:pdf|doc|txt|pptx|xls|mhtml|html|ppt|mdb|accdb|docx)";
    static String archex = "regex:.*(?i:zip|rar|7z|aar|jar|gz|tar|xz|iso)";
    static String appex = "regex:.*(?i:exe|com|apk|bat|msi|iso|app|sh)";
    @FXML
    Label organizerPreviewText;
    @FXML
    TextField dirPathTbx;
    @FXML
    Button browseBtn;
    @FXML
    TextField regexInput;
    @FXML
    CheckBox regexCheck;
    @FXML
    TreeView organizerTree;
    @FXML
    Button scanBtn;
    @FXML
    Button organizeBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dirPathTbx.setText(mPath);
    }

    public void processDir() {
        CheckBoxTreeItem<String> rootitem = new CheckBoxTreeItem<>("File Categories");
        rootitem.setExpanded(true);
        organizerTree.setRoot(rootitem);
        organizerTree.setCellFactory(CheckBoxTreeCell.<String>forTreeView());
        organizerTree.setShowRoot(true);
        String dirToOrganize = dirPathTbx.getText();
        if (dirToOrganize.equals("") || dirToOrganize.equals(null)) {
            alert("FS-PROCDIR[FNF](null)", "Folder not found", "Please check the folder path again.", Alert.AlertType.WARNING);
            return;
        }
        try {
            auds = iOrganizer.iterateAndFilter(dirToOrganize, audex);
            organizerPreviewText.setText("Found: \n" + auds.length + " audio files\n");
            TreeItem<String> musicTree = new TreeItem<>("Music");
            //rootitem.getChildren().add(musicTree);
            for (String s : auds) {
                musicTree.getChildren().add(new TreeItem<>(s));
            }
            Organizer.clearList();
            vids = iOrganizer.iterateAndFilter(dirToOrganize, videx);
            organizerPreviewText.setText(organizerPreviewText.getText() + vids.length + " videos\n");
            TreeItem<String> vidTree = new TreeItem<>("Videos");
            //rootitem.getChildren().add(vidTree);
            for (String s : vids) {
                vidTree.getChildren().add(new TreeItem<>(s));
            }
            Organizer.clearList();
            pics = iOrganizer.iterateAndFilter(dirToOrganize, picex);
            organizerPreviewText.setText(organizerPreviewText.getText() + pics.length + " pictures\n");
            TreeItem<String> picTree = new TreeItem<>("Pictures");
            //rootitem.getChildren().add(picTree);
            for (String s : pics) {
                picTree.getChildren().add(new TreeItem<>(s));
            }
            Organizer.clearList();
            docs = iOrganizer.iterateAndFilter(dirToOrganize, docex);
            organizerPreviewText.setText(organizerPreviewText.getText() + docs.length + " documents\n");
            TreeItem<String> docTree = new TreeItem<>("Documents");
            //rootitem.getChildren().add(docTree);
            for (String s : docs) {
                docTree.getChildren().add(new TreeItem<>(s));
            }
            Organizer.clearList();
            exes = iOrganizer.iterateAndFilter(dirToOrganize, appex);
            organizerPreviewText.setText(organizerPreviewText.getText() + exes.length + " apps\n");
            TreeItem<String> appTree = new TreeItem<>("Apps");
            //rootitem.getChildren().add(musicTree);
            for (String s : exes) {
                appTree.getChildren().add(new TreeItem<>(s));
            }
            Organizer.clearList();
            archs = iOrganizer.iterateAndFilter(dirToOrganize, archex);
            organizerPreviewText.setText(organizerPreviewText.getText() + archs.length + " compressed (archived) files.");
            TreeItem<String> zipTree = new TreeItem<>("Archived/Compressed Files");
            //rootitem.getChildren().add(zipTree);
            for (String s : archs) {
                zipTree.getChildren().add(new TreeItem<>(s));
            }
            rootitem.getChildren().addAll(musicTree, vidTree, picTree, docTree, appTree, zipTree);
            Organizer.clearList();
            //->showNotification("FileStudio:Organizer", "Finished processing dir.");
        } catch (IOException ex) {
            logger.Log("ProcessDir : " + ex.getMessage());
            Logger
                    .getLogger(OrganizerController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }
    }

    //TODO: add sublists to tree as leaves under file types branches (Music, Videos etc.)
    public void organizeDir() {
        //->showNotification("FileStudio:Organizer", "Organizing dir...");
        try {
            iOrganizer.moveFi(uss.getDir("aar"), archs);
            //orgCmplete.setText("Moved: " + zips.length + " archives");
            iOrganizer.moveFi(uss.getDir("mp4"), vids);
            //iOrganizerorgCmplete.setText(orgCmplete.getText() + "\nMoved: " + vids.length + " videos");
            iOrganizer.moveFi(uss.getDir("mp3"), auds);
            //orgCmplete.setText(orgCmplete.getText() + "\nMoved: " + auds.length + " Music");
            iOrganizer.moveFi(uss.getDir("doc"), docs);
            //iOrganizerorgCmplete.setText(orgCmplete.getText() + "\nMoved: " + docs.length + " Documents");
            iOrganizer.moveFi(uss.getDir("app"), exes);
            //iOrganizerorgCmplete.setText(orgCmplete.getText() + "\nMoved: " + iapps.length + " executables");
            iOrganizer.moveFi(uss.getDir("pic"), pics);
            //orgCmplete.setText(orgCmplete.getText() + "\nMoved: " + pics.length + " pics");
            //orgCmplete.setText("Success!");
            //->showNotification("FileStudio:Organizer", "Organized!");
        } catch (IOException ex) {
            logger.Log("OrganizeDir: " + ex.getMessage());
            //orgCmplete.setText(ex.getMessage());
            //Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //TODO: on ignorelistbtn click, launch ignore list window
    public void launchIgnoreListApp() {
        listManager("ignore");
    }

    //TODO: when destinationBtn is clicked, launch settins with dest params to allow user change organiser settings
    public void launchDestinationSettings() {
        alert("FileStudio: NODEST", "In Development", "Come back after an update", Alert.AlertType.INFORMATION);
    }

    public void pdir() {
        pickDir(dirPathTbx, "Pick a Folder : Duplicate Finder", filestudio.Util.home, dirPathTbx.getScene().getWindow());
    }
}
