package filestudio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import java.util.zip.ZipInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Controller class for handling archive extraction operations in the GUI.
 * Provides functionality to extract various archive formats and update the UI accordingly.
 * Implements Initializable for JavaFX controller initialization.
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
     * Sets the current file label to the path.
     *
     * @param url The location used to resolve relative paths for the root object, or null if unknown.
     * @param rb The resources used to localize the root object, or null if not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        extractorCurrentFileLabel.setText(path);
    }

    /**
     * Sets the current file path and destination directory for extraction.
     *
     * @param path The path of the archive file to extract.
     * @param dest The destination directory where files will be extracted.
     */
    public void setCurrentPathAndDest(String path, String dest) {
        this.path = path;
        this.destination = dest;
        extractorCurrentFileLabel.setText(path);
    }

    /**
     * Determines the type of archive file based on its extension.
     *
     * @return An integer code representing the archive type:
     *         1=zip, 2=rar, 3=tar, 4=gz, 5=7z, 6=bz2, 7=xz, 8=lzma, 9=cab, 10=iso, 11=dmg, 0=unknown.
     */
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

    /**
     * Starts a new extraction process for the current archive file.
     * Updates UI elements and runs extraction in a background thread.
     */
    public void newExtractor() {
        ArchiveExtractor aex = new ArchiveExtractor();
        File zipFile = new File(extractorCurrentFileLabel.getText());
        File targetDir = new File(destination);
        log("extracting: " + zipFile.getPath());
        log("to: " + targetDir.getPath());
        extractorArchiveLabel.setText("Almost There...");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
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

    /**
     * Extracts the current archive file.
     * Handles exceptions and determines file type for extraction.
     */
    public void extract() {
        try {
            xTractZip(new File(extractorCurrentFileLabel.getText()));
        } catch (IOException ex) {
            System.out.println("B4 XTRACT: " + ex.getMessage());
            Logger.getLogger(ArchiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
        int type = determineFileType();
        // Extraction logic based on type can be added here.
    }

    /**
     * Extracts a ZIP archive file asynchronously, updating progress.
     *
     * @param zipFile The ZIP file to extract.
     */
    public void extractZip(File zipFile) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
                    long totalSize = zipFile.length();
                    long extractedSize = 0;

                    ZipEntry entry;
                    while ((entry = zipInputStream.getNextEntry()) != null) {
                        File outputFile = new File(zipFile.getParent(), entry.getName());
                        if (entry.isDirectory()) {
                            File tDir = new File(entry.getName());
                            tDir.mkdirs();
                        }
                        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                                extractedSize += bytesRead;
                                updateProgress(extractedSize, totalSize);
                            }
                        }
                    }
                } catch (IOException e) {
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

    /**
     * Extracts a ZIP archive file using Apache Commons Compress.
     *
     * @param zipFile The ZIP file to extract.
     * @throws IOException If an I/O error occurs during extraction.
     */
    public void xTractZip(File zipFile) throws IOException {
        File targetDir = new File(zipFile.getParent());
        try (ArchiveInputStream i = new ZipArchiveInputStream(new FileInputStream(zipFile))) {
            ZipArchiveEntry entry = null;
            while ((entry = (ZipArchiveEntry) i.getNextEntry()) != null) {
                if (!i.canReadEntryData(entry)) {
                    continue;
                }
                String name = fileName(targetDir, entry);
                File f = new File(name);
                if (entry.isDirectory()) {
                    f.mkdirs();
                    if (!f.isDirectory() && !f.mkdirs()) {
                        throw new IOException("Failed to create directory " + f);
                    }
                } else {
                    File parent = f.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }
                    try (java.io.OutputStream o = Files.newOutputStream(f.toPath())) {
                        IOUtils.copy(i, o);
                    }
                }
            }
        }
    }

    /**
     * Returns the normalized absolute file path for a ZIP entry.
     * Prevents directory traversal attacks by validating the path.
     *
     * @param targetDir The target directory for extraction.
     * @param entry The ZIP archive entry.
     * @return The absolute path for the extracted file.
     * @throws IOException If the entry path is invalid or unsafe.
     */
    private String fileName(File targetDir, ZipArchiveEntry entry) throws IOException {
        File ent = new File(targetDir, new File(entry.getName()).toPath().normalize().toString());
        if (!ent.toPath().startsWith(targetDir.toPath())) {
            throw new IOException("Bad zip entry: " + entry.getName());
        }
        return ent.getAbsolutePath();
    }

    /**
     * Logs a message to the extractor log output area.
     *
     * @param log The message to log.
     */
    void log(String log) {
        extractorLogOutput.setText(extractorLogOutput.getText() + "\n" + log);
    }

    /**
     * Minimizes or closes the extractor window.
     */
    public void minimize() {
        try {
            Stage st = (Stage) extractorCurrentFileLabel.getScene().getWindow();
            st.close();
        } catch (Exception e) {
            // Ignore exceptions during minimize
        }
    }
}
