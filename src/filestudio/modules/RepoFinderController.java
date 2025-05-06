/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package filestudio.modules;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.scene.control.TextField;

public class RepoFinderController implements Initializable {

    @FXML
    private TextField txtDirectory;

    @FXML
    private Button btnScan;

    @FXML
    private ListView<File> repoListView;

    @FXML
    private Button btnAction1;

    @FXML
    private Button btnAction2;

    @FXML
    private Button btnAction3;

    private ObservableList<File> repoList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        repoListView.setItems(repoList);
        repoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        btnScan.setOnAction(e -> {
            String dir = txtDirectory.getText();
            if (dir != null && !dir.isEmpty()) {
                findRepos(dir);
            }
        });

        repoListView.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {
            @Override
            public ListCell<File> call(ListView<File> param) {
                ListCell<File> cell = new ListCell<File>() {
                    @Override
                    protected void updateItem(File item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getAbsolutePath());
                        }
                    }
                };

                // Context menu for delete repo
                ContextMenu contextMenu = new ContextMenu();
                MenuItem deleteItem = new MenuItem("Delete Repo");
                deleteItem.setOnAction(event -> {
                    File repo = cell.getItem();
                    if (repo != null) {
                        deleteRepo(repo);
                    }
                });
                contextMenu.getItems().add(deleteItem);

                cell.setContextMenu(contextMenu);

                return cell;
            }
        });

        repoListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateRepoActionButtons(newSelection);
            } else {
                disableRepoActionButtons();
            }
        });

        disableRepoActionButtons();
    }

    public void findRepos(String idir) {
        repoList.clear();
        Task<List<File>> task = new Task<List<File>>() {
            @Override
            protected List<File> call() throws Exception {
                List<File> foundRepos = new ArrayList<>();
                scanRepos(new File(idir), foundRepos);
                return foundRepos;
            }
        };

        task.setOnSucceeded(e -> {
            repoList.setAll(task.getValue());
        });

        new Thread(task).start();
    }

    private void scanRepos(File dir, List<File> foundRepos) {
        if (dir == null || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                File gitDir = new File(file, ".git");
                if (gitDir.exists() && gitDir.isDirectory()) {
                    foundRepos.add(file);
                } else {
                    scanRepos(file, foundRepos);
                }
            }
        }
    }

    private void updateRepoActionButtons(File repo) {
        boolean hasDanglingCommits = false;
        boolean hasUnpushedChanges = false;
        boolean hasOtherGitActions = false;

        try {
            hasDanglingCommits = checkDanglingCommits(repo);
            hasUnpushedChanges = checkUnpushedChanges(repo);
            hasOtherGitActions = checkOtherGitActions(repo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnAction1.setDisable(!hasDanglingCommits);
        btnAction2.setDisable(!hasUnpushedChanges);
        btnAction3.setDisable(!hasOtherGitActions);

        btnAction1.setOnAction(e -> fixDanglingCommits(repo));
        btnAction2.setOnAction(e -> pushChanges(repo));
        btnAction3.setOnAction(e -> commitChanges(repo));
    }

    private void fixDanglingCommits(File repo) {
        // Placeholder: Implement fixing dangling commits
        System.out.println("Fixing dangling commits in " + repo.getAbsolutePath());
    }

    private void pushChanges(File repo) {
        // Placeholder: Implement pushing changes
        System.out.println("Pushing changes in " + repo.getAbsolutePath());
    }

    private void commitChanges(File repo) {
        // Placeholder: Implement committing changes
        System.out.println("Committing changes in " + repo.getAbsolutePath());
    }

    private boolean checkDanglingCommits(File repo) throws IOException, InterruptedException {
        // Check for dangling commits using git fsck --dangling
        ProcessBuilder pb = new ProcessBuilder("git", "fsck", "--dangling");
        pb.directory(repo);
        Process process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            String output = new String(process.getInputStream().readAllBytes());
            return output.contains("commit");
        }
        return false;
    }

    private boolean checkUnpushedChanges(File repo) throws IOException, InterruptedException {
        // Check for unpushed commits using git cherry
        ProcessBuilder pb = new ProcessBuilder("git", "cherry");
        pb.directory(repo);
        Process process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            String output = new String(process.getInputStream().readAllBytes());
            return !output.isBlank();
        }
        return false;
    }

    private boolean checkOtherGitActions(File repo) throws IOException, InterruptedException {
        // Check for uncommitted changes using git status --porcelain
        ProcessBuilder pb = new ProcessBuilder("git", "status", "--porcelain");
        pb.directory(repo);
        Process process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            String output = new String(process.getInputStream().readAllBytes());
            return !output.isBlank();
        }
        return false;
    }

    private void disableRepoActionButtons() {
        btnAction1.setDisable(true);
        btnAction2.setDisable(true);
        btnAction3.setDisable(true);
    }

    private void deleteRepo(File repo) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Repository");
        alert.setHeaderText("Are you sure you want to delete this repository?");
        alert.setContentText(repo.getAbsolutePath());

        Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            boolean success = deleteDirectory(repo);
            if (success) {
                repoList.remove(repo);
            } else {
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Failed to delete repository");
                errorAlert.setContentText("Could not delete: " + repo.getAbsolutePath());
                errorAlert.showAndWait();
            }
        }
    }

    private boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            if (children != null) {
                for (File child : children) {
                    boolean success = deleteDirectory(child);
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }
}
