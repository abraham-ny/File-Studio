/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filestudio;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class Organizer {

    private static final Logger logger = Logger.getLogger(Organizer.class.getName());

    // Recursive method to get files by extension (regex) including subdirectories
    public List<String> iterateAndFilter(String mpath, String ext) throws IOException {
        List<String> fileList = new ArrayList<>();
        Path dir = Paths.get(mpath);
        File checkIfExists = new File(mpath);
        if (!checkIfExists.exists() || !checkIfExists.isDirectory()) {
            logger.warning("Directory does not exist or is not a directory: " + mpath);
            return fileList;
        }
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(ext);
        try {
            Files.walk(dir)
                .filter(path -> matcher.matches(path.getFileName()) && Files.isRegularFile(path))
                .forEach(path -> {
                    logger.log(Level.FINE, "Matched file: {0}", path.toString());
                    fileList.add(path.toString());
                });
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error walking directory: " + mpath, e);
            throw e;
        }
        logger.log(Level.INFO, "Found {0} files matching {1} in {2}", new Object[]{fileList.size(), ext, mpath});
        return fileList;
    }

    // Move a single file from source to destination directory
    public void moveFile(String sourceFilePath, String destDirPath) throws IOException {
        File sourceFile = new File(sourceFilePath);
        File destDir = new File(destDirPath);
        if (!destDir.exists() || !destDir.isDirectory()) {
            throw new IOException("Destination is not a valid directory: " + destDirPath);
        }
        File destFile = new File(destDir, sourceFile.getName());
        boolean success = sourceFile.renameTo(destFile);
        if (!success) {
            String msg = "Failed to move file " + sourceFilePath + " to " + destFile.getAbsolutePath();
            logger.severe(msg);
            throw new IOException(msg);
        } else {
            logger.log(Level.INFO, "Moved file {0} to {1}", new Object[]{sourceFilePath, destFile.getAbsolutePath()});
        }
    }

    // Move multiple files to destination directory
    public void moveFilesBatch(String destDirPath, List<String> files) throws IOException {
        int movedCount = 0;
        for (String filePath : files) {
            try {
                moveFile(filePath, destDirPath);
                movedCount++;
            } catch (IOException e) {
                logger.log(Level.WARNING, "Failed to move file: " + filePath, e);
            }
        }
        logger.log(Level.INFO, "Moved {0} files to {1}", new Object[]{movedCount, destDirPath});
    }
}
