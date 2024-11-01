/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package filestudio;

import com.google.gson.JsonObject;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.imgscalr.Scalr;
//import net.minidev.json.JSONObject;

/**
 * This is the GUI entry point/main class
 *
 * @author Abraham Moruri
 */
public class FXMLDocumentController implements Initializable {

    public final static String ver = "1.2.3";
    @FXML
    Label userTitle;
    @FXML
    Label dirProperties;
    @FXML
    TextField wordToRemoveLabel;
    @FXML
    TextField wordToReplaceLabel;
    @FXML
    Label successOrNotRenamerLabel;
    @FXML
    ListView<String> diskList;
    @FXML
    ListView<String> histList;
    @FXML
    ListView<String> wordRemoverList;
    @FXML
    AnchorPane mainWindowHandle;
    @FXML
    Button selectFileDuplicatorBtn;
    @FXML
    TextField fileToDuplicateTextBox;
    @FXML
    ListView duplicatorListView;
    @FXML
    TextField numberOfCopiesTextBox;
    @FXML
    Button redoBtn;
    @FXML
    Button undoBtn;
    @FXML
    TextField organizerDirTextField;
    @FXML
    Label organizerPreviewText;
    @FXML
    TextField extractorFilePath;
    @FXML
    TextField extractorDestinationLabel;
    @FXML
    TextField imageUpscaleTextField;
    @FXML
    ProgressBar diskProgress;
    @FXML
    ImageView imageUpscaleImageView;
    @FXML
    Button settingsButton;
    @FXML
    TextField compressorPath;
    @FXML
    TextField compressorDest;
    @FXML
    ComboBox compressorType;
    @FXML
    TextField currentImageSizeTF;
    @FXML
    TextField newImageResTF;
    @FXML
    Button mainMenuBtn;
    @FXML
    TreeView dupeTree;
    @FXML
    TextField dupefinderInput;
    @FXML
    ProgressBar compressorProgressBar;
    @FXML
    Label topBarLabel;
    String[] types = {".zip", ".tar", ".gz", ".7z", ".rar", ".tar.sz", ".tar.gz", ".tar.deflate", ".tar.xz", ".tar.bz2"};
    String archFolder = "";
    public static String pd = "https://paypal.com/donate/?hosted_button_id=A88GCN8R382B6";
    String sfUrl = "https://sourceforge.net/projects/filestudio/";//source update url
    //@FXML Button autoGenerateWordToRemove;
    //@FXML ListView<DiskInfo> diskList;
    ObservableList<DiskInfo> disksListObservable = FXCollections.observableArrayList();
    ObservableList<String> diskStringList = FXCollections.observableArrayList();
    String activeDir = "";
    String ext = ".zip";
    List<File> wordRemoverFileList = new ArrayList<>();
    List<File> generatedList = new ArrayList<>();
    String[] docs, pics, vids, auds, exes, archs;
    Organizer iOrganizer = new Organizer();
    UserSettings uss = new UserSettings();
    String uimgpath;
    FLogger logger = new FLogger();
    static String audex = "regex:.*(?i:mp3|ogg|avi|wav|flacc|aud|m4a|m3u)";
    static String videx = "regex:.*(?i:mp4|mkv|webm|ts|wmp|mov)";
    static String picex = "regex:.*(?i:jpg|jpeg|png|gif|bmp|jpe|jfif|ico)";
    static String docex = "regex:.*(?i:pdf|doc|txt|pptx|xls|mhtml|html|ppt|mdb|accdb|docx)";
    static String archex = "regex:.*(?i:zip|rar|7z|aar|jar|gz|tar|xz|iso)";
    static String appex = "regex:.*(?i:exe|com|apk|bat|msi|iso|app|sh)";
    //private String[] menuOptions = {"Share", "Update", "Help", "About"};
    private static final String REPO_API_URL = "https://api.github.com/repos/abummoja/file-studio/releases/latest";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //checkForUpdates();
        userTitle.setText(Util.user + " | " + Util.os);
        File[] rootDrive = File.listRoots();
        for (File diskDrive : rootDrive) {
            disksListObservable.add(new DiskInfo(diskDrive.getAbsolutePath()));
        }
        for (DiskInfo mdiskInfo : disksListObservable) {
            diskStringList.add((mdiskInfo.getName() + "-" + Math.round((mdiskInfo.getFreeSpace() / 1024 / 1024) * 100) / 100 + " GB Free"));
        }
        diskList.setItems(diskStringList);
        diskList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        diskList.getSelectionModel().select(0);
        //diskList.addEventHandler(EventType.ROOT, eventHandler);
        for (String archType : types) {
            compressorType.getItems().add(archType);
        }
        ContextMenu ctxMnu = new ContextMenu();
        //MenuItem[] miArr = {};
        MenuItem shareMenu = new MenuItem("Share");
        MenuItem helpMenu = new MenuItem("Help");
        MenuItem updateMenu = new MenuItem("Check For Updates");
        MenuItem feedBackMnu = new MenuItem("Feedback");
        MenuItem aboutMnu = new MenuItem("About");
        ctxMnu.getItems().addAll(shareMenu, helpMenu, updateMenu, feedBackMnu, aboutMnu);
        mainMenuBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            ctxMnu.show(mainMenuBtn, event.getScreenX(), event.getScreenY());
        });
        for (MenuItem mi : ctxMnu.getItems()) {
            mi.addEventHandler(EventType.ROOT, eventHandler -> {
                logger.Log(mi.getText());
                switch (mi.getText()) {
                    case "Share":
                        String smailto = "mailto:?subject=" + encodeUrl("Advanced file handler.") + "&body=" + encodeUrl("I use FileStudio" + sfUrl);
                        try {
                            Desktop dt = Desktop.getDesktop();
                            dt.mail(new URI(smailto));
                        } catch (Exception e) {
                            showNotification("MAIL-SHARE", "FAILED");
                        }
                        break;
                    case "Help":
                        openBrowser(sfUrl);
                        break;
                    case "Check For Updates":
                        //pdateFunc();
                        try {
                            checkForUpdates();
                        } catch (Exception e) {
                            logger.Log("Network Error", "Failed to check for updates!", "Connect your computer to the internet");
                            //alert("Network Error", "Failed to check for updates!", "Connect your computer to the internet", Alert.AlertType.ERROR);
                        }
                        break;
                    case "About":
                        openBrowser(sfUrl);
                        break;
                    case "Feedback":
                        String mail = "abummoja3@gmail.com";
                        String mailto = String.format("mailto:%s?subject=" + encodeUrl("FILE-STUDIO-V1.3 DESKTOP") + "&body=" + encodeUrl("I use FileStudio"), mail);
                        try {
                            Desktop dt = Desktop.getDesktop();
                            dt.mail(new URI(mailto));
                        } catch (Exception e) {
                            showNotification("MAIL", "FAILED");
                        }
                        break;
                }
            });
        }
        compressorType.getSelectionModel().selectedItemProperty().addListener((Observable ov) -> {
            int selectedIndex = compressorType.getSelectionModel().getSelectedIndex();
            ext = types[selectedIndex];
            logger.Log(ext);
            //String ext = (String)compressorType.getItems()[compressorType.getSelectionModel().getSelectedIndex()]
        });
        diskList.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                DiskInfo di = disksListObservable.get(diskList.getSelectionModel().getSelectedIndex());
                ContextMenu diskMenu = new ContextMenu();
                String[] options = {"Open In Explorer", "Properties", "Rename", "Eject", "Format", "Scan For Errors", "Defragment", "Compress"};
                for (String s : options) {
                    MenuItem mi = new MenuItem(s);
                    mi.addEventHandler(EventType.ROOT, eventHandler -> {
                        processDisk(mi.getText(), di);
                    });
                    diskMenu.getItems().add(mi);
                }
                diskMenu.show(diskList, event.getScreenX(), event.getScreenY());
            } else {
                DiskInfo di = disksListObservable.get(diskList.getSelectionModel().getSelectedIndex());
                logger.Log(di.path);
                dirProperties.setText(di.path + "\nName: " + di.getName()
                        + "\nTotal Space: " + Math.round((di.getTotalSpace() / 1024 / 1024) * 100) / 100
                        + " GB\nFree Space: " + Math.round((di.getFreeSpace() / 1024 / 1024) * 100) / 100
                        + " GB\nDescription: " + di.getDescription());
                //double p = (double) ((Math.round((di.getTotalSpace() / 1024 / 1024) * 100) / 100) - (Math.round((di.getFreeSpace() / 1024 / 1024) * 100) / 100)) / 1000;
                double np = Math.round((di.getFreeSpace() / 1024 / 1024) * 100) / 100; //ree space in hundreds (GB)
                diskProgress.setProgress(np / 1000);//divide by 1000 to get "0.x" value for progress bar
            }
        });
        checkHistory();
        histList.getSelectionModel().selectedItemProperty().addListener(listener -> {
            String ss = histList.getSelectionModel().getSelectedItem();
            dirProperties.setText(ss);
            activeDir = ss;
            organizerDirTextField.setText(activeDir);
            compressorPath.setText(activeDir);
            compressorDest.setText(new File(ss).getParent());
            dirProperties.setText(new File(ss).getAbsolutePath());
            dupefinderInput.setText(activeDir);
        });
        mainWindowHandle.setOnMousePressed(pressEvent -> {
            mainWindowHandle.setOnMouseDragged(dragEvent -> {
                mainWindowHandle.getScene().getWindow().setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                mainWindowHandle.getScene().getWindow().setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });

    }

    void processDisk(String action, DiskInfo disk) {
        switch (action) {
            case "Open In Explorer":
                try {
                    Runtime.getRuntime().exec("explorer " + disk.path + "\\");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Eject":
                eject(disk);
                break;
            case "Rename":
                String newName = JOptionPane.showInputDialog("Enter a new name for " + disk.getName(), JOptionPane.OK_CANCEL_OPTION);
                if (!newName.equals(null)) {
                    try {
                        rename(disk, newName);
                    } catch (Exception e) {
                        alert("Disk Renamer Error", "Failed to rename disk", e.getMessage(), Alert.AlertType.ERROR);
                    }
                } else {
                    alert("Error", "Please enter a valid name for the disk", "Name can not be blank", Alert.AlertType.WARNING);
                }
                break;
            case "Defragment":
                runCommand("defrag " + disk.path);
                break;
            case "Scan For Errors":
                runCommand("chkdsk " + disk.path);
            case "Compress":
                compressDisk(disk);
                break;

        }
    }

    private String encodeUrl(String text) {
        try {
            return java.net.URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.Log("URL ENCODER: " + e.getMessage());
            return text;
        }
    }

    @FXML
    void updateFunc() {
        //open browser to sourceforge url for download
        openBrowser(sfUrl);
    }

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
                        updateFunc();
                    } else if (result.get() == noBtn) {
                        //clode dlg
                        alert.close();
                    } else {
                        //return;
                        updateFunc();
                    }
                }
                // Download the filestudio.exe
                //downloadFileFromRelease(latestVersion, "filestudio.exe");
            } else {
                logger.Log("You are using the latest version: " + ver);
                alert("Updater", "Latest version is : " + latestVersion, "You are using the latest version.", Alert.AlertType.INFORMATION);
            }
        } catch (IOException e) {
            logger.Log("FAILED TO CHECK FOR UPDATES!");
            //e.printStackTrace();
            alert("Network Error", "Failed to check for updates!", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

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

    @FXML
    void donateFunc() {
        //launch browser with paypal link to donation page.
        openBrowser(pd);
    }

    public void upscaleImage() {
        upscalePic(imageUpscaleTextField.getText());
    }

    private void openBrowser(String url) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (URISyntaxException ex) {
                donateAlert(url);
                Logger
                        .getLogger(FXMLDocumentController.class
                                .getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                donateAlert(url);
                Logger
                        .getLogger(FXMLDocumentController.class
                                .getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            donateAlert(url);
        }
    }

    void donateAlert(String url) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Browser Error.");
        alert.setHeaderText("Couldn't Open Browser");
        alert.setContentText("Please check that you have a browser or permissions.");
        ButtonType yesBtn = new ButtonType("Copy Link To Clipboard");
        ButtonType noBtn = new ButtonType("Close");
        alert.getButtonTypes().setAll(yesBtn, noBtn);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == yesBtn) {
                //copy to clipboard
                StringSelection sel = new StringSelection(url);
                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                cb.setContents(sel, null);
                showNotification("FileStudio", "Link copied to clipboard!");
            } else if (result.get() == noBtn) {
                //clode dlg
                alert.close();
            } else {
                //return;
                alert.close();
            }
        }
    }

    void showNotification(String title, String msg) {
        logger.Log(title + msg);
//        if (SystemTray.isSupported()) {
//            SystemTray sysTray = SystemTray.getSystemTray();
//            java.awt.Image img = Toolkit.getDefaultToolkit().createImage(getClass().getResourceAsStream("FileStudioOtherIcon.png").toString());
//            TrayIcon ti = new TrayIcon(img, "File Studio");
//            ti.setImageAutoSize(true);
//            ti.setToolTip("Automation Tool");
//            try {
//                sysTray.add(ti);
//                ti.displayMessage(title, msg, TrayIcon.MessageType.INFO);
//            } catch (AWTException e) {
//                System.out.println("ABU-NOTIF: " + e.getMessage());
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("Systray unsupported");
//        }
    }

    @FXML
    void launchUpscaler() {
        ProcessBuilder pb = new ProcessBuilder("OpenUpscaler.exe");
        pb.directory(new File(System.getProperty("user.dir")));
        try {
            Process proc = pb.start();
        } catch (IOException ex) {
            alert("PB -r proc IOE", "Failed to launch upscaler.", ex.getMessage(), Alert.AlertType.ERROR);
            logger.Log("PB -r proc IOE", ex.getMessage());
        }
    }

    @FXML
    void launchSettings() {
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("SettingsUI.fxml"));
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
            stage.initStyle(StageStyle.UNDECORATED);
            Image i = new Image(getClass().getResourceAsStream("FileStudioOtherIcon.png"));
            stage.getIcons().add(i);
            stage.show();
            SettingsUIController.setStage(stage);
        } catch (IOException e) {
            logger.Log("Fxml settings err Abu, " + e.getMessage() + e.getCause().toString());
        }
    }

    public void updateSettingsAccess() {
        boolean isSettingsButtonViewable = settingsButton.isVisible();
        if (isSettingsButtonViewable == true) {
            settingsButton.setVisible(!isSettingsButtonViewable);
            settingsButton.setManaged(!isSettingsButtonViewable);
        } else {
            settingsButton.setVisible(isSettingsButtonViewable);
            settingsButton.setManaged(isSettingsButtonViewable);
        }
    }

    public static void iUpdateSettingsAccess() {
        FXMLDocumentController fxd = new FXMLDocumentController();
        fxd.updateSettingsAccess();
    }

    // ported from HTML Edit IDE
    public String readFile(String filePathRead) throws IOException {
        // Path p1 = Paths.get(filePathRead);
        // BufferedReader reader = new BufferedReader(new FileReader(filePathRead));
        String fileContent = "";
        try {
            java.util.Scanner scanner = new java.util.Scanner(new File(filePathRead));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                fileContent = fileContent + "\n" + line;
                //do stuff
            }
        } catch (FileNotFoundException e) {
        }
        return fileContent;
    }

    public void close() {
        //check first if operations are running before closing.
        Platform.exit();
    }

    public void minimize() {
        //mINIMIZE THE WINDOW
        showNotification("FileStudio:CustomWindow", "Minimized custom GUI");
        try {
            Stage st = (Stage) userTitle.getScene().getWindow();
            st.setIconified(true);
        } catch (Exception e) {
        }
    }
    //dir chooser not file chooser

    // THE MAIN FILE CHOOSER
    public void showFileChooser() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setInitialDirectory(new File("C://Users/" + Util.user));
        File selectedFolder = dirChooser.showDialog(userTitle.getScene().getWindow());
        if (selectedFolder != null && selectedFolder.exists()) {
            dirProperties.setText(selectedFolder.getAbsolutePath());
            activeDir = selectedFolder.getAbsolutePath();
            organizerDirTextField.setText(activeDir);
            compressorPath.setText(activeDir);
            dupefinderInput.setText(activeDir);
            topBarLabel.setText(activeDir + " - File Studio " + ver);
            compressorDest.setText(selectedFolder.getParent());
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
    }

    //[Finished] Method to get 'recent directories' list on Welcome Screen
    public void checkHistory() {
        histList.getItems().clear();
        try {
            //load json data into listview.
            /**
             * mdata = readFile(history.getAbsolutePath()); Gson gson = new
             * Gson(); HistoryItem[] historyArr = gson.fromJson(mdata,
             * HistoryItem[].class); for (HistoryItem item : historyArr) {
             * histList.getItems().add(item.path); }*
             */
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
            histList.getItems().add("error while loading");
        }
    }

    public void removeWord() {
        try {
            FileRenamer renamer = new FileRenamer();
            String status = renamer.removeWordFromList(wordRemoverFileList, wordToRemoveLabel.getText(), wordToReplaceLabel.getText());
            successOrNotRenamerLabel.setText(status);
            wordRemoverList.getItems().clear();
        } catch (Exception f) {
        }
    }

    public void search() {
        //check if the String containing path to active dir is empty or null
        if (activeDir != null && !activeDir.equals(" ")) {
            String keyWord = wordToRemoveLabel.getText();
            /*@reoved check if the search text is longer than 3 chars to start searching*/
            File directory = new File(activeDir);
            File[] directoryToSearch = directory.listFiles();
            //clear the list and listview as user types
            wordRemoverFileList.clear();
            wordRemoverList.getItems().clear();
            //then update the list and listview with dir contents matching search criteria
            for (File file : directoryToSearch) {
                if (file.getName().contains(keyWord)) {
                    wordRemoverFileList.add(file);
                    wordRemoverList.getItems().add(file.getName());
                }
            }
        }
    }

    //UNFINISHED&BUGGY method to automatically detect the most common word in a list of files
    public void autoGenerateSearchPhrase() {
        //WordPredictor predictor = new WordPredictor();
        if (activeDir != null && !" ".equals(activeDir)) {
            try {
                System.out.println(WordPredictor.findMostRepeatedWord(activeDir));
                wordToRemoveLabel.setText(WordPredictor.findMostRepeatedWord(activeDir));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void selectFile() {
        File iDir;
        if (activeDir != null) {
            iDir = new File(activeDir);
        } else {
            iDir = new File(System.getProperty("user.home"));
        }
        FileChooser filePicker = new FileChooser();
        filePicker.setTitle("Select A File To Duplicate");
        filePicker.setInitialDirectory(iDir);
        File toDuplicate = filePicker.showOpenDialog(selectFileDuplicatorBtn.getScene().getWindow());
        //if file is selected
        if (toDuplicate != null) {
            //open file
            fileToDuplicateTextBox.setText(toDuplicate.getAbsolutePath());
            duplicatorListView.getItems().add(toDuplicate.getName());
        }
    }

    public void generateCopies() {
        File toMultiply = new File(fileToDuplicateTextBox.getText());
        File parentDir = new File(toMultiply.getParent());
        FileMultiplier multiplier = new FileMultiplier();
        try {
            generatedList = multiplier.multiply(toMultiply.getAbsolutePath(), parentDir.getAbsolutePath(), Integer.parseInt(numberOfCopiesTextBox.getText()));
            for (File f : generatedList) {
                duplicatorListView.getItems().add(f.getName());
            }
            undoBtn.setDisable(false);
            redoBtn.setDisable(true);
        } catch (IOException ex) {
            logger.Log("Err Abu, " + ex.getMessage());
            Logger
                    .getLogger(FXMLDocumentController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void undoDuplicator() {
        File original = new File(fileToDuplicateTextBox.getText());
        for (File f : generatedList) {
            try {
                f.delete();
                duplicatorListView.getItems().clear();
                duplicatorListView.getItems().add(original.getName());
                redoBtn.setDisable(false);
                undoBtn.setDisable(true);
            } catch (Exception e) {
                //show dialog (failed)
            }
        }
    }

    public void redoDuplicator() {
        generateCopies();
    }

    public void processDir() {
        String dirToOrganize = organizerDirTextField.getText();
        if (dirToOrganize.equals("") || dirToOrganize.equals(null)) {
            //warn user
            showNotification("FileStudio:Organizer", "Null directory!");
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
            showNotification("FileStudio:Organizer", "Finished processing dir.");
        } catch (IOException ex) {
            logger.Log("ProcessDir : " + ex.getMessage());
            Logger
                    .getLogger(FXMLDocumentController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void organizeDir() {
        showNotification("FileStudio:Organizer", "Organizing dir...");
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
            showNotification("FileStudio:Organizer", "Organized!");
        } catch (IOException ex) {
            logger.Log("OrganizeDir: " + ex.getMessage());
            //orgCmplete.setText(ex.getMessage());
            //Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void readContents(String zPath) throws ZipException, FileNotFoundException, IOException {
        //THIS FUNC IS UNDER CONSTRUCTION (ERROR PRONE) AND UNFUNCTIONAL
        File fl = new File(zPath);
        ZipFile zipFile = new ZipFile(fl);
        ZipInputStream zipInput = new ZipInputStream(new FileInputStream(fl));
        ZipArchiveEntry entry = new ZipArchiveEntry((ZipEntry) zipFile.getEntries());
        byte[] content = new byte[(int) entry.getSize()];
        for (int i = 0; i < entry.getSize(); i++) {
            zipInput.read(content, i, content.length - i);
            logger.Log("Read file...");
        }
    }

    public void selectArchive() {
        FileChooser filePicker = new FileChooser();
        filePicker.setTitle("Select Archive To Extract");
        //filePicker.setInitialDirectory(new File(activeDir));
        FileChooser.ExtensionFilter archiveFilter = new FileChooser.ExtensionFilter("Archives", "*.zip", "*.rar", "*.tar", "*.gz", "*.7z", "*.bz2", "*.xz", "*.lzma", "*.cab", "*.iso", "*.dmg");
        filePicker.getExtensionFilters().add(archiveFilter);
        File toExtract = filePicker.showOpenDialog(userTitle.getScene().getWindow());
        String archName = "extracted";
        if (toExtract != null) {
            extractorFilePath.setText(toExtract.getPath());
            archName = toExtract.getName();
            int extIndex = archName.lastIndexOf(".");
            if (extIndex != -1) {
                archName = archName.substring(0, extIndex);
            }
            archName = archName.replaceAll("[\\\\/:*?\"<>|]", "_");
        } else {
            return;
        }
        archFolder = archName;
        extractorDestinationLabel.setText(toExtract.getParent() + "\\" + archFolder);
//        try {
//            //readContents(toExtract.getPath().toString());
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void selectFolderToExtractTo() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedFolder = dirChooser.showDialog(userTitle.getScene().getWindow());
        if (selectedFolder != null) {
            extractorDestinationLabel.setText(selectedFolder.getPath());
        } else {
            return;
        }
    }

    public void extractFile() {
        //NOTE: this method only calls the extractor class but does not actually extract files.
        if (extractorFilePath.getText() != null && extractorDestinationLabel.getText() != null) {
            if (new File(extractorFilePath.getText()).exists()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("layout_archiver.fxml"));
                try {
                    Parent root = loader.load();
                    ArchiverController archController = loader.getController();
                    archController.setCurrentPathAndDest(extractorFilePath.getText(), extractorDestinationLabel.getText());
                    Stage extractorStage = new Stage();
                    switch (uss.theme) {
                        case "dark":
                            root.getStylesheets().add("filestudio/style.css");
                            break;
                        case "light":
                            root.getStylesheets().add("filestudio/light.css");
                            break;
                        default:
                            root.getStylesheets().add("filestudio/style.css");
                    }
                    extractorStage.setScene(new Scene(root));
                    extractorStage.initStyle(StageStyle.UNDECORATED);
                    extractorStage.show();
                } catch (IOException ex) {
                    logger.Log("extractFile " + ex.getMessage());
                    Logger
                            .getLogger(FXMLDocumentController.class
                                    .getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                alert("Error", "Failed To Extract Files", "The File Does Not Exist!", Alert.AlertType.ERROR);
                logger.Log("Error", "Failed To Extract Files", "The File Does Not Exist!");
            }
        } else {
            alert("Error", "Failed To Extract Files", "Enter the correct file path to extract", Alert.AlertType.ERROR);
            logger.Log("Error", "Failed To Extract Files", "Enter the correct file path to extract");
        }
    }

    public void selectImage() {
        FileChooser imagePicker = new FileChooser();
        imagePicker.setTitle("Select Image to Upscale");
        //filePicker.setInitialDirectory(new File(activeDir));
        FileChooser.ExtensionFilter archiveFilter = new FileChooser.ExtensionFilter("Pictures", "*.png", "*.jpg", "*.gif", "*.jpeg", "*.ico", "*.jfif");
        imagePicker.getExtensionFilters().add(archiveFilter);
        File toExtract = imagePicker.showOpenDialog(userTitle.getScene().getWindow());
        if (toExtract != null && toExtract.exists()) {
            imageUpscaleTextField.setText(toExtract.getPath());
            try {
                Image i = new Image(new FileInputStream(toExtract.getPath()));
                imageUpscaleImageView.setImage(i);
                BufferedImage bi = ImageIO.read(toExtract);
                int width = bi.getWidth();
                int height = bi.getHeight();
                currentImageSizeTF.setText(width + "x" + height);
                //[FIXED][Fri,Mar-8-2024]Last run failed with --> Can't open image: java.lang.IllegalArgumentException: Invalid URL: unknown protocol: c
            } catch (Exception e) {
                logger.Log("Can't open image: " + e);
            }
        }
    }

    public void openGallery() {
        String imagePath = imageUpscaleTextField.getText();
        String command = "explorer.exe \"" + imagePath + "\"";
        String[] cmd = {"explorer.exe", imagePath};
        Process proc = null;
        int exitcode;
        if (imagePath != null && new File(imagePath).exists()) {
            try {
                ProcessBuilder pb = new ProcessBuilder(cmd);
                pb.redirectErrorStream(true);
                proc = pb.start();
                //proc = Runtime.getRuntime().exec(command);
                exitcode = proc.waitFor();

            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (InterruptedException ex) {
                Logger.getLogger(FXMLDocumentController.class
                        .getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (proc != null) {
                    proc.destroy();
                }
            }
        } else {
            alert("Preview Failed!", "The file does not exist or is corrupt", imagePath, Alert.AlertType.ERROR);
        }
    }

    public void revealImageInExplorer() {
        File theImage = new File(imageUpscaleTextField.getText());
        String command = "explorer.exe \"" + theImage.getParent() + "\"";
        Process proc;
        int exitcode;
        if (theImage != null)
            try {
            proc = Runtime.getRuntime().exec(command);
            exitcode = proc.waitFor();

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (InterruptedException ex) {
            Logger.getLogger(FXMLDocumentController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return;
    }

    public void compressorThread() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //extractorProgressBar.setProgress(extractorProgressBar.getProgress() + 0.1);
                if (compressorProgressBar.getProgress() != 1.0) {
                    compressorProgressBar.setProgress(compressorProgressBar.getProgress() + 0.1);
                }
            }
        }, 0, 1000);
        new Thread(() -> {
            try {
                compressorStart();
            } catch (IOException ioe) {
                alert("Compressor", "An error is stopping the task from executing normally!", ioe.getMessage(), Alert.AlertType.ERROR);
            }
        }).start();
    }

    public void compressorStart() throws IOException {
        ArchiveExtractor aext = new ArchiveExtractor();
        String theDir = compressorPath.getText();
        File theDirObj = new File(theDir);
        theDir = theDirObj.getAbsolutePath();
        System.out.println("ABU: compressor start is running " + theDir);
        String outputDir = compressorDest.getText() + "\\" + theDirObj.getName() + ext;
        String multiFormFirstFile = compressorDest.getText() + "\\" + theDirObj.getName() + ".tar";
        //System.out.println("TheDir: " + theDir);
        //System.out.println("Compressing to: " + outputDir);
        //notify(compressorNotif, "Almost There...");
        if (new File(compressorPath.getText()).exists()) {
            switch (ext) {
                case ".zip":
                    aext.archive(theDir, outputDir);
                    break;
                case ".7z":
                    aext.archive(theDir, outputDir);
                    break;
                case ".gz":
                    aext.archive(theDir, outputDir);
                    break;
                case ".rar":
                    aext.archive(theDir, outputDir);
                    break;
                case ".tar":
                    aext.archive(theDir, outputDir);
                    break;
                case ".tar.xz":
                    aext.archive(theDir, multiFormFirstFile);
                    //String tarFileToXZ = multiFormFirstFile + ".xz";
                    try {
                        aext.createTarXZFile(multiFormFirstFile, outputDir);
                    } catch (Exception ex) {
                        System.out.println("ABU-XZ-MF: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                    Files.delete(Paths.get(multiFormFirstFile));
                    break;
                //TODO: the ".tar.---" should be fed a tar file in place of "theDir" since they take tar and archive to second format
                //Already fixed the above TODO by archiving(firstFile) then archiving the archived first file.
                case ".tar.gz":
                    aext.archive(theDir, multiFormFirstFile);
                    aext.createTarGZipFile(multiFormFirstFile, outputDir);
                    Files.delete(Paths.get(multiFormFirstFile));
                    break;
                case ".tar.deflate":
                    aext.archive(theDir, multiFormFirstFile);
                    aext.createTarDeflateFile(multiFormFirstFile, outputDir);
                    Files.delete(Paths.get(multiFormFirstFile));
                    break;
                case ".tar.sz":
                    aext.archive(theDir, multiFormFirstFile);
                    aext.createTarSnappyFile(multiFormFirstFile, outputDir);
                    Files.delete(Paths.get(multiFormFirstFile));
                    break;
                case ".tar.bz2":
                    aext.archive(theDir, multiFormFirstFile);
                    aext.createTarBZip2File(multiFormFirstFile, outputDir);
                    Files.delete(Paths.get(multiFormFirstFile));
                    break;
            }
            //notify(compressorNotif, "Done!");
        } else {
            alert("Compression Error", "Could not compress content", "The file or folder you are trying to compress does not exist!", Alert.AlertType.ERROR);
            logger.Log("Compression Error", "Could not compress content", "The file or folder you are trying to compress does not exist!");
        }
    }

    public void compressorChangeDir() {
        showFileChooser();
    }

    public void compressorChangeDest() {
        //todo: change the path to output zip
        //show file chooser with correct file type
        showFileChooser();
    }

    public void viewUpscaledImage() {
        //String imagePath = imageUpscaleTextField.getText();
        String command = "explorer.exe \"" + uimgpath + "\"";
        Process proc;
        int exitcode;
        if (uimgpath != null && (new File(uimgpath).exists()))
            try {
            proc = Runtime.getRuntime().exec(command);
            exitcode = proc.waitFor();

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (InterruptedException ex) {
            Logger.getLogger(FXMLDocumentController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return;
    }

    public static BufferedImage upscaleImage(BufferedImage originalImage, int newWidth, int newHeight) {
        // Use Imgscalr to resize the image
        return Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, newWidth, newHeight);
    }

    private void upscalePic(String text) {
        //notify(imgUpNotif, "Please wait...");
        try {
            // Load the original image from file
            File inputFile = new File(text);
            BufferedImage originalImage = ImageIO.read(inputFile);

            // Define the new dimensions (e.g., scale up by 2x)
            int newWidth = originalImage.getWidth() * 2;
            int newHeight = originalImage.getHeight() * 2;

            // Upscale the image using Imgscalr
            BufferedImage upscaledImage = upscaleImage(originalImage, newWidth, newHeight);
            int width = upscaledImage.getWidth();
            int height = upscaledImage.getHeight();
            newImageResTF.setText(width + "x" + height);
            // Save the upscaled image to a new file
            File outputFile = new File(inputFile.getParent() + "\\" + inputFile.getName() + "_upscaled.jpg");
            uimgpath = outputFile.getPath();
            ImageIO.write(upscaledImage, "jpg", outputFile);
            //notify(imgUpNotif, "Finished Upscaling!");
            //System.out.println("Image upscaled successfully!");
        } catch (IOException e) {
            //notify(imgUpNotif, "Failed to upscale");
            logger.Log("ABU-UPSCALER: " + e.getMessage());
            e.printStackTrace();
        }
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void notify(TextField tf, String msg) {
        tf.setText(msg);
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
                alert.close();
            } else {
                //return;
                alert.close();
            }
        }
    }
    List<CheckBoxTreeItem<String>> selectedItems;

    public void scan() {
        File directory = new File(dupefinderInput.getText());
        if (directory.exists() && directory.isDirectory()) {
            boolean hashType = false;
            Map<String, List<String>> duplicateList = new HashMap<String, List<String>>();
            try {
                Finder.find(duplicateList, directory, hashType);									// FIND DUPLICATE FILES
            } catch (Exception exception) {
                exception.printStackTrace();
                alert("Duplicate Finder", "Error scanning for duplicates!", exception.getMessage(), Alert.AlertType.ERROR);
            }
            //dupeTree.setSelectionModel();
            CheckBoxTreeItem<String> rootitem = new CheckBoxTreeItem<>("Duplicates");
            rootitem.setExpanded(true);
            dupeTree.setRoot(rootitem);
            dupeTree.setCellFactory(CheckBoxTreeCell.<String>forTreeView());
            dupeTree.setShowRoot(true);
            for (List<String> list : duplicateList.values()) {
                //after scan
                if (list.size() > 1) {
                    int pi = 0;
                    CheckBoxTreeItem<String> parent = new CheckBoxTreeItem<>(new File(list.get(0)).getName());
                    for (String name : list) {
                        //add name tolist
                        pi++;
                        CheckBoxTreeItem child = new CheckBoxTreeItem(name);
                        parent.getChildren().add(child);
                        if (pi >= 2) {
                            child.setSelected(true);
                        } else {
                            child.setSelected(false);
                        }
                    }
                    parent.setExpanded(true);
                    rootitem.getChildren().add(parent);
                }
                selectedItems = getSelectedItems(rootitem);

            }
        } else {
            alert("PathScanner.exe[embedded] - Invalid Path", "The path was not found!", dupefinderInput.getText(), Alert.AlertType.ERROR);
        }
    }

    public void deleteDupes() {
        System.out.println(dupeTree.getSelectionModel().getSelectedItems());
        for (TreeItem<String> itm : selectedItems) {
            File iFile = new File(itm.getValue());
            if (iFile.exists()) {
                iFile.delete();
            } else {
                alert("InternalFS.o[embedded]", "The file " + itm.getValue() + " was not found!", "Will proceed with other available files.", Alert.AlertType.INFORMATION);
            }
        }
        try {
            scan();//rescan the dir to update the list
        } catch (Exception any) {
            alert("DupeFinder[embedded].exe - Issue", "Not an error, just a bug", "Failed to refresh list", Alert.AlertType.INFORMATION);
        }
        //alert("Delete duplicates", "The feature you tried to access is in development", "Will be available in next update", Alert.AlertType.INFORMATION);
    }

    private List<CheckBoxTreeItem<String>> getSelectedItems(CheckBoxTreeItem<String> rootitem) {
        List<CheckBoxTreeItem<String>> selectedItems = new ArrayList<>();
        if (rootitem.isSelected()) {
            selectedItems.add(rootitem);
        }
        for (TreeItem<String> child : rootitem.getChildren()) {
            selectedItems.addAll(getSelectedItems((CheckBoxTreeItem<String>) child));
        }
        return selectedItems;
    }

    private void eject(DiskInfo disk) {
        Task<Void> task = new Task() {
            @Override
            protected Void call() throws IOException, InterruptedException {
                // Create a diskpart script file
                File script = File.createTempFile("ejectDiskScript", ".txt");
                try (FileWriter writer = new FileWriter(script)) {
                    writer.write("select volume " + disk.path.charAt(0) + "\n");
                    writer.write("remove\n");
                }

                // Run diskpart with the script
                ProcessBuilder processBuilder = new ProcessBuilder("diskpart", "/s", script.getAbsolutePath());
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String finalLine = line;
                        Platform.runLater(() -> alert("Eject", finalLine + "\n", disk.getDescription(), Alert.AlertType.INFORMATION));
                    }
                }
                process.waitFor();
                script.delete(); // Clean up script file
                return null;
            }
        };

        new Thread(task).start();
    }

    private void rename(DiskInfo disk, String newLabel) {
        Task<Void> task = new Task() {
            @Override
            protected Void call() throws IOException, InterruptedException {
                // Execute label command to rename the disk
                String command = "label " + disk.path + " " + newLabel;
                ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String finalLine = line;
                        Platform.runLater(() -> alert("Rename", finalLine + "\n", disk.getDescription(), Alert.AlertType.INFORMATION));
                    }
                }
                process.waitFor();
                return null;
            }
        };

        new Thread(task).start();
    }

    private void compressDisk(DiskInfo disk) {
        Task<Void> task = new Task() {
            @Override
            protected Void call() throws IOException, InterruptedException {
                // Enable compression on the entire drive using compact
                String command = "compact /c /s:" + disk.path + "\\*";
                ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String finalLine = line;
                        Platform.runLater(() -> alert("Disk Compression", finalLine + "\n", disk.getDescription(), Alert.AlertType.INFORMATION));
                    }
                }
                process.waitFor();
                return null;
            }
        };

        new Thread(task).start();
    }

    private void runCommand(String command) {
        //outputArea.clear();
        Task<Void> task = new Task() {
            @Override
            protected Void call() throws IOException {
                ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String finalLine = line;
                        // Append each line to the TextArea
                        javafx.application.Platform.runLater(() -> alert("Exec", finalLine + "\n", command, Alert.AlertType.INFORMATION));
                    }
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

}
