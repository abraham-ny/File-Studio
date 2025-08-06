/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class SplashController implements Initializable, GlobalVars {

    @FXML
    ProgressBar progressBar;
    @FXML
    Label progressText;
    @FXML
    AnchorPane anchorPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        progressBar.progressProperty().bind(setup.progressProperty());
        setup.messageProperty().addListener((obs, old, newVal) -> {
            progressText.setText(newVal);
        });
        setup.setOnSucceeded(e -> {
            //on task succeeded.
            alert("Setup complete!", setup.getValue(), "Great features await!", AlertType.INFORMATION);
        });
        
        new Thread(setup).start();

    }

    Task<String> setup = new Task<String>() {

        @Override
        protected String call() throws Exception {
            //File appDir = new File(FileStudio.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            File appDir = new File(System.getProperty("user.home"));
            File logDir = new File(appDir, "tres");
            if(!logDir.exists()){
                logDir.mkdirs();
            }
            File setupLogFile = new File(logDir, "fs-init.json");
            
            String rootPath = System.getProperty("user.home");
            DirNode tree = buildTree(new File(rootPath));
            Meta meta = gatherMetadata();
            updateMessage("Initializing fast setup..");
            //updateProgress(1.0);
            DirectorySnapshot snapshot = new DirectorySnapshot();
            snapshot.meta = meta;
            snapshot.tree = tree;

            try (FileWriter writer = new FileWriter(setupLogFile)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(snapshot, writer);
                updateMessage("Done!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            return hasInternet() ? "Online" : "offline";
        }
        
        public boolean hasInternet(){
            try{
                URL setuprl = new URL("https://clients3.google.com/generate_204");
                updateMessage("Almost there.");
                HttpURLConnection conn = (HttpURLConnection) setuprl.openConnection();
                conn.setConnectTimeout(4000);
                conn.setReadTimeout(4000);
                conn.setRequestMethod("GET");
                conn.connect();
                return conn.getResponseCode()==204;
            }catch(Exception ex){
                return false;
            }
        }

        public DirNode buildTree(File dir) {
            DirNode node = new DirNode();
            node.name = dir.getName();
            updateMessage("Running fs-setup");
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    updateMessage("Building superfast index...");
                    node.children = new ArrayList<>();
                    for (File file : files) {
                        updateMessage("Indexing : "+file.getPath());
                        DirNode child = buildTree(file);
                        child.parent = node;
                        node.children.add(child);
                    }
                }
            }
            return node;
        }

        public Meta gatherMetadata() {
            Meta meta = new Meta();
            meta.owner = System.getProperty("user.name");
            meta.os = System.getProperty("os.name");
            meta.locale = Locale.getDefault().toString();
            meta.computerName = System.getenv("COMPUTERNAME");
            meta.snapshotTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());
            
            updateMessage(meta.owner);
            
            try {
                meta.ipAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (Exception e) {
                meta.ipAddress = "Unknown";
                updateMessage("no account");
            }
            meta.email = "user@filestudio.desk";

            return meta;
        }

        public Meta meta;
        public DirNode tree;

        class Meta {

            public String owner;
            public String os;
            public String locale;
            public String computerName;
            public String ipAddress;
            public String email;
            public String snapshotTime;
        }

        class DirNode {

            public String name;
            public List<DirNode> children;

            // transient = not serialized, used for navigation
            public transient DirNode parent;

            public boolean isDirectory() {
                return children != null && !children.isEmpty();
            }
        }
        class DirectorySnapshot {

        public Meta meta;
        public DirNode tree;
        }

    };

    //utility classes
    /*class DirectorySnapshot {

        public Meta meta;
        public DirNode tree;

        public class Meta {

            public String owner;
            public String os;
            public String locale;
            public String computerName;
            public String ipAddress;
            public String email;
            public String snapshotTime;
        }

        public class DirNode {

            public String name;
            public List<DirNode> children;

            // transient = not serialized, used for navigation
            public transient DirNode parent;

            public boolean isDirectory() {
                return children != null && !children.isEmpty();
            }
        }
    }*/
}
