/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filestudio.modules;

/**
 *
 * @author Admin
 */
import java.util.List;

public class DirectorySnapshot {

    public Meta meta;
    public DirNode tree;

    public class Meta {

        public String owner;
        public String os;
        public String locale;
        public String computerName;
        public String ipAddress;
        public String email;
        public String snapshotTime;
    }

    public class DirNode {

        public String name;
        public List<DirNode> children;

        // transient = not serialized, used for navigation
        public transient DirNode parent;

        public boolean isDirectory() {
            return children != null && !children.isEmpty();
        }
    }
}
