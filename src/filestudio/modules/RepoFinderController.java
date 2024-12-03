/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio.modules;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class RepoFinderController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    void findRepos(String idir) {
        File root = new File(idir);
        File realRoot = File.listRoots()[0];
        for (File dir : root.listFiles()) {
            if (dir.isDirectory() && ".git".equalsIgnoreCase(dir.getName())) {
                System.out.println("Found at : " + dir.getParent());
                return;
            } else if (dir.isDirectory()) {
                findRepos(dir.getPath());
            }
        }
    }

}
