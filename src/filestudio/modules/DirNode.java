/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filestudio.modules;

import java.util.List;
/**
 *
 * @author Admin
 */


public class DirNode {
    public String name;
    public List<DirNode> children;

    // Not in JSON, just for tracking navigation
    public DirNode parent;

    public boolean isDirectory() {
        return children != null && !children.isEmpty();
    }
}