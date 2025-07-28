/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filestudio;

/**
 *
 * @author Admin
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import filestudio.modules.DirectorySnapshot;
import filestudio.modules.DirectorySnapshot.DirNode;
import filestudio.modules.DirectorySnapshot.Meta;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SnapshotBuilder {
    /*public static void main(String[] args) {
        String rootPath = "C:\\Users\\Young\\Documents";
        DirNode tree = buildTree(new File(rootPath));
        Meta meta = gatherMetadata();

        DirectorySnapshot snapshot = new DirectorySnapshot();
        snapshot.meta = meta;
        snapshot.tree = tree;

        try (FileWriter writer = new FileWriter("directory_snapshot_full.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(snapshot, writer);
            System.out.println("Snapshot saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static DirNode buildTree(File dir) {
        DirNode node = new DirNode();
        node.name = dir.getName();
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                node.children = new ArrayList<>();
                for (File file : files) {
                    DirNode child = buildTree(file);
                    child.parent = node;
                    node.children.add(child);
                }
            }
        }
        return node;
    }

    private static Meta gatherMetadata() {
        Meta meta = new Meta();
        meta.owner = System.getProperty("user.name");
        meta.os = System.getProperty("os.name");
        meta.locale = Locale.getDefault().toString();
        meta.computerName = System.getenv("COMPUTERNAME");
        meta.snapshotTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());

        try {
            meta.ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            meta.ipAddress = "Unknown";
        }

        // Optional (could be filled from app settings or prompt)
        meta.email = "user@example.com";

        return meta;
    }*/
}