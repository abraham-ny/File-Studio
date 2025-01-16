/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filestudio;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *
 * @author Admin
 */
public class UserSettings {

    static String unm = System.getProperty("user.name");
    final static Preferences pref = Preferences.userNodeForPackage(UserSettings.class);
    //for easy tracking in case of errors, move the files to 'FileStudio' folder in their respective dir.
    public String mp3dir = pref.get("mp3dir", "C:\\Users\\" + unm + "\\Music\\FileStudio");
    public String mp4dir = pref.get("mp4dir", "C:\\Users\\" + unm + "\\Videos\\FileStudio");
    public String picdir = pref.get("picdir", "C:\\Users\\" + unm + "\\Pictures\\FileStudio");
    public String archdir = pref.get("archdir", "C:\\Users\\" + unm + "\\Archives\\FileStudio");
    public String appsdir = pref.get("appsdir", "C:\\Users\\" + unm + "\\Apps");
    public String docsdir = pref.get("docsdir", "C:\\Users\\" + unm + "\\Documents\\FileStudio");
    public String theme = pref.get("theme", "light");
    public String useMetro = pref.get("metro", "yes");
    public String[] dirs = {mp3dir, mp4dir, picdir, archdir, appsdir, docsdir};
    public boolean isSettingsPageOpen = false;

    public UserSettings() {
        // empty constructor
    }

    public void setDir(String toUpdate, String val) {
        pref.put(toUpdate, val);//update the prefs.
    }

    public void resetAll() {
        //reset to default
        setDir("mp3dir", "C:\\Users\\" + unm + "\\Music\\FileStudio");
        setDir("mp4dir", "C:\\Users\\" + unm + "\\Videos\\FileStudio");
        setDir("picdir", "C:\\Users\\" + unm + "\\Pictures\\FileStudio");
        setDir("archdir", "C:\\Users\\" + unm + "\\Archives\\FileStudio");
        setDir("appsdir", "C:\\Users\\" + unm + "\\Apps");
        setDir("docsdir", "C:\\Users\\" + unm + "\\Documents\\FileStudio");
        setDir("theme", "light");//setDir can update any pref value including theme string
        setDir("metro", "yes");
        saveSettings();
        createDir(dirs);
    }

    public void saveSettings() {
        try {
            pref.flush();
            pref.sync();
        } catch (BackingStoreException ex) {
            System.out.println("ABU Settings ERR: " + ex.getMessage());
            Logger.getLogger(UserSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getDir(String typee) {
        createDir(dirs);
        switch (typee) {
            case "mp3":
                return mp3dir;
            case "mp4":
                return mp4dir;
            case "pic":
                return picdir;
            case "aar":
                return archdir;
            case "app":
                return appsdir;
            case "doc":
                return docsdir;
            default:
                break;
        }
        return null;
    }

    public void createDir(String[] paths) {
        for (String p : paths) {
            File f = new File(p);
            if (!f.exists()) {
                f.mkdir();
            } else {
                System.out.println("Directory exists!");
            }
        }
    }
}
