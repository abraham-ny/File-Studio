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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class OrganizerController implements Initializable, GlobalVars {

    Organizer iOrganizer = new Organizer();
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

    }

    public void processDir() {
        String dirToOrganize = dirPathTbx.getText();
        if (dirToOrganize.equals("") || dirToOrganize.equals(null)) {
            //warn user
            //showNotification("FileStudio:Organizer", "Null directory!");
            return;
        }
        try {
            auds = iOrganizer.iterateAndFilter(dirToOrganize, audex);
            organizerPreviewText.setText("Found: \n" + auds.length + " audio files\n");
            Organizer.clearList();
            vids = iOrganizer.iterateAndFilter(dirToOrganize, videx);
            organizerPreviewText.setText(organizerPreviewText.getText() + vids.length + " videos\n");
            Organizer.clearList();
            pics = iOrganizer.iterateAndFilter(dirToOrganize, picex);
            organizerPreviewText.setText(organizerPreviewText.getText() + pics.length + " pictures\n");
            Organizer.clearList();
            docs = iOrganizer.iterateAndFilter(dirToOrganize, docex);
            organizerPreviewText.setText(organizerPreviewText.getText() + docs.length + " documents\n");
            Organizer.clearList();
            exes = iOrganizer.iterateAndFilter(dirToOrganize, appex);
            organizerPreviewText.setText(organizerPreviewText.getText() + exes.length + " apps\n");
            Organizer.clearList();
            archs = iOrganizer.iterateAndFilter(dirToOrganize, archex);
            organizerPreviewText.setText(organizerPreviewText.getText() + archs.length + " compressed (archived) files.");
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

    }

    //TODO: when destinationBtn is clicked, launch settins with dest params to allow user change organiser settings
    public void launchDestinationSettings() {

    }
}
