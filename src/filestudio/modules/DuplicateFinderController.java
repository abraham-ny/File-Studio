/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio.modules;

import filestudio.Finder;
import filestudio.GlobalVars;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class DuplicateFinderController implements Initializable, GlobalVars {

    public static String mPath;
    @FXML
    TreeView dupeTree;
    @FXML
    TextField dirPathTbx;
    @FXML
    CheckBox ecoCheck;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (mPath.equals("")) {
            mPath = System.getProperty("user.home");
        } else if (new File(mPath).isDirectory()) {
            dirPathTbx.setText(mPath);
            scan();

        }
    }

    List<CheckBoxTreeItem<String>> selectedItems;

    /**
     * Scans the current path for duplicate files and adds them to the tree view
     * while automatically selecting duplicates (non first items in a tree
     * group).
     */
    public void scan() {
        File directory = new File(dirPathTbx.getText());
        if (directory.exists() && directory.isDirectory()) {
            boolean hashType = ecoCheck.isSelected();
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
            alert("PathScanner.exe[embedded] - Invalid Path", "The path was not found!", dirPathTbx.getText(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Deletes selected files (in the tree view) from the file system. Files do
     * not go to recycle bin.
     */
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

    /**
     * Deselects selected items in the tree view
     */
    public void clearSelection() {
        for (CheckBoxTreeItem<String> cbt : selectedItems) {
            cbt.setSelected(false);
        }
        //dupeTree.getSelectionModel().clearSelection();
    }

    public void pickFolder() {
        pickDir(dirPathTbx, "Pick a Folder : Duplicate Finder", filestudio.Util.home, dirPathTbx.getScene().getWindow());
        //alert("Click scan to update the list", false);
    }
}
