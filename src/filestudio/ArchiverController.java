/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipInputStream;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
//import org.apache.commons.compress.archivers.zip.

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class ArchiverController implements Initializable {

    String path;
    String destination;
    String archex = "regex:.*(?i:zip|rar|7z|aar|jar|gz|tar)";
    @FXML
    Label extractorCurrentFileLabel;
    @FXML
    ProgressBar extractorProgressBar;
    @FXML
    TextArea extractorLogOutput;
    @FXML
    Label extractorArchiveLabel;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        extractorCurrentFileLabel.setText(path);
    }

    public void setCurrentPathAndDest(String path, String dest) {
        this.path = path;
        this.destination = dest;
        extractorCurrentFileLabel.setText(path);
    }

    public int determineFileType() {
        Pattern pattern = Pattern.compile(archex);
        Matcher matcher = pattern.matcher(extractorCurrentFileLabel.getText());
        if (matcher.matches()) {
            String extension = matcher.group(1);
            switch (extension.toLowerCase()) {
                case "zip":
                    return 1;
                case "rar":
                    return 2;
                case "tar":
                    return 3;
                case "gz":
                    return 4;
                case "7z":
                    return 5;
                case "bz2":
                    return 6;
                case "xz":
                    return 7;
                case "lzma":
                    return 8;
                case "cab":
                    return 9;
                case "iso":
                    return 10;
                case "dmg":
                    return 11;
                default:
                    return 0;//unknown archive type
            }
        } else {
            return 0;
        }
    }

    //TODO: check format (like in compressor) if multiformat use specific func else if zip or tar use unarchive
    public void newExtractor() {
        ArchiveExtractor aex = new ArchiveExtractor();
        File zipFile = new File(extractorCurrentFileLabel.getText());
        File targetDir = new File(destination);
        log("extracting: " + zipFile.getPath());
        log("to: " + targetDir.getPath());
        //[FIXED] cannot extract multiform archive ".tar.xxx"
        //[DONE]TODO: determine if is multiformat then extract approprietely else just unarchive()
        extractorArchiveLabel.setText("Almost There...");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //extractorProgressBar.setProgress(extractorProgressBar.getProgress() + 0.1);
                if (extractorProgressBar.getProgress() != 1.0) {
                    extractorProgressBar.setProgress(extractorProgressBar.getProgress() + 0.1);
                }
            }
        }, 0, 1000);
        new Thread(() -> {
            aex.unarchive(zipFile.getPath(), targetDir.getPath());
        }).start();
        extractorArchiveLabel.setText("Done!");
        log("Done!");
    }

    public void extract() {
        try {
            xTractZip(new File(extractorCurrentFileLabel.getText()));
        } catch (IOException ex) {
            System.out.println("B4 XTRACT: " + ex.getMessage());
            Logger.getLogger(ArchiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
        int type = determineFileType();
        //File theFile = new File(path);
        /*switch (type) {
            case 1:
                //zip
                System.out.println("File is zip");
                extractZip(new File(extractorCurrentFileLabel.getText()));
                break;
            case 2:
                //rar
                break;
            case 3:
                //tar
                break;
            case 4:
                //gz
                break;
            case 5:
                //7z
                break;
            case 6:
                //bz2
                break;
            case 7:
                //xz
                break;
            case 8:
                //lzma
                break;
            case 9:
                //cab
                break;
            case 10:
                //iso
                break;
            case 11:
                //dmg
                break;
            case 0:
                //err
                break;
        }*/
    }

    public void extractZip(File zipFile) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
                    long totalSize = zipFile.length();
                    long extractedSize = 0;

                    ZipEntry entry;
                    while ((entry = zipInputStream.getNextEntry()) != null) {
                        System.out.println("Dest ABU: " + destination + zipFile.getParent() + entry.getName().lastIndexOf("\\"));
                        File outputFile = new File(zipFile.getParent(), entry.getName());
                        //todo :add check if is file
                        if (entry.isDirectory()) {
                            File tDir = new File(entry.getName());
                            tDir.mkdirs();
                            System.out.println("ABU, Extracting dir: " + entry.getName() + " to " + zipFile.getParent());
                        }
                        System.out.println("ABU, Extracting: " + entry.getName() + " to " + zipFile.getParent());
                        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                                extractedSize += bytesRead;
                                System.out.println("while loop - progressing... Abu");
                                updateProgress(extractedSize, totalSize);
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("2-ABU: " + e.getMessage());
                    e.printStackTrace();
                }
                return null;
            }
        };

        extractorProgressBar.progressProperty().bind(task.progressProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void xTractZip(File zipFile) throws IOException {
        File targetDir = new File(zipFile.getParent());
        try (ArchiveInputStream i = new ZipArchiveInputStream(new FileInputStream(zipFile))) {
            ZipArchiveEntry entry = null;
            while ((entry = (ZipArchiveEntry) i.getNextEntry()) != null) {
                if (!i.canReadEntryData(entry)) {
                    // log something?
                    continue;
                }
                //File eName = new File(targetDir.getPath(), entry);
                String name = fileName(targetDir, entry);
                System.out.println("AB PATH: " + name);
                File f = new File(name);
                if (entry.isDirectory()) {
                    f.mkdirs();
                    if (!f.isDirectory() && !f.mkdirs()) {
                        throw new IOException("D failed to create directory " + f);
                    }
                } else {
                    File parent = f.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("P failed to create directory " + parent);
                    }
                    try (OutputStream o = Files.newOutputStream(f.toPath())) {
                        IOUtils.copy(i, o);
                    }
                }
            }
        }
    }

    private String fileName(File targetDir, ZipArchiveEntry entry) {
        File ent = new File(targetDir, new File(entry.getName()).toPath().normalize().toString());
        if (!ent.toPath().startsWith(targetDir.toPath())) {
            try {
                throw new IOException("Bad zip entry: " + entry.getName());
            } catch (IOException ex) {
                Logger.getLogger(ArchiverController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ent.getAbsolutePath();
        //return targetDir.getAbsolutePath() + "\\" + ent.getName();
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void log(String log) {
        extractorLogOutput.setText(extractorLogOutput.getText() + "\n" + log);
    }

    public void minimize() {
        try {
            Stage st = (Stage) extractorCurrentFileLabel.getScene().getWindow();
            st.close();
        } catch (Exception e) {
            return;
        }
    }
}
