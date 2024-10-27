/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filestudio;

import java.io.File;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Admin
 */
public class DiskInfo {

    public String path;//disk name also
    File disk = new File("C:");

    public DiskInfo(String path) {
        this.path = path;
        this.disk = new File(path);
    }

    public double getTotalSpace() {
        return (double) disk.getTotalSpace() / 1024;
    }

    public double getFreeSpace() {
        return (double) disk.getFreeSpace() / 1024;
    }

    public double getUsableSpace() {
        return (double) disk.getUsableSpace() / 1073741824;
    }

    public String getName() {
        return FileSystemView.getFileSystemView().getSystemDisplayName(disk);
    }

    public String getDescription() {
        return FileSystemView.getFileSystemView().getSystemTypeDescription(disk);
    }

    public Icon getIcon() {
        return FileSystemView.getFileSystemView().getSystemIcon(disk);
    }
}
