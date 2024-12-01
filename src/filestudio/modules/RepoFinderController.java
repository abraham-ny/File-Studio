/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio.modules;

import java.net.URL;
import java.util.ResourceBundle;
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

    import java.io.File ;
    import java.util.concurrent.RecursiveTask ;
    import java.util.concurrent.ForkJoinPool ;
    import java.util.ArrayList ;
    import java.util.List ;

    public class GitRepoScanner {

        public static void main(String[] args) {
            String rootPath = "./"; // Change this to your root folder
            ForkJoinPool pool = new ForkJoinPool();
            RepoScannerTask task = new RepoScannerTask(new File(rootPath));

            List<String> repos = pool.invoke(task);
            repos.forEach(repo -> System.out.println("Found Git Repo: " + repo));
        }
    }

    class RepoScannerTask extends RecursiveTask<List<String>> {

        private final File folder;

        public RepoScannerTask(File folder) {
            this.folder = folder;
        }

        @Override
        protected List<String> compute() {
            List<String> foundRepos = new ArrayList<>();
            List<RepoScannerTask> tasks = new ArrayList<>();

            File[] files = folder.listFiles();
            if (files == null) {
                return foundRepos;
            }

            for (File file : files) {
                if (file.isDirectory()) {
                    if (".git".equalsIgnoreCase(file.getName())) {
                        foundRepos.add(folder.getAbsolutePath());
                        return foundRepos; // Stop further scanning of this branch
                    } else {
                        tasks.add(new RepoScannerTask(file));
                    }
                }
            }

            for (RepoScannerTask task : invokeAll(tasks)) {
                foundRepos.addAll(task.join());
            }

            return foundRepos;
        }
    }
}
