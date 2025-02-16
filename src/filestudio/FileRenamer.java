/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filestudio;

import java.io.File;
import java.util.List;

/**
 *
 * @author Admin
 */
public class FileRenamer {

    public FileRenamer() {
        //empty constructor
    }

    public String removeWordFromList(List<File> fileList, String ReplaceFrom, String ReplaceWith) {
        File[] filesList = {};
        //filesList = new ArrayList<File>(fileList);
        filesList = fileList.toArray(filesList);
        try {
            // This method will rename all files in a folder by chaning ReplaceFrom string with ReplaceWith string
            for (int i = 0; i < filesList.length; i++) {
                String newName = (filesList[i].toString().replaceAll(ReplaceFrom, ReplaceWith)).trim();
                filesList[i].renameTo(new File(newName));
            }
            return "Successfully renamed " + filesList.length + " files.";
        } catch (Exception e) {
            return (e.getMessage());
        }
    }
}
