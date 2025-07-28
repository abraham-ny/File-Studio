/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filestudio.modules;

import com.google.gson.JsonObject;
import java.util.List;

/**
 * A tiny implementation to allow easier traversing of existing tree files
 * @author Abraham
 */
public class TreeFileSystem {
    
//    public List<TFile> listFiles(TFile file){
//        if(!file.isDir){
//            return null;
//        }
//        JsonObject jo = new JsonObject();
//        jo = file.name;
//        return file;
//    }
    
    public class TFile{
        String name;
        boolean isDir = false;
        public TFile(){
            
        }
        public TFile(String name, boolean isDir){
            this.isDir = isDir;
            this.name = name;
        }
        public String getName(){
            return name;
        }
        public boolean isDir(){
            return isDir;
        }
        public void setName(String str){
            this.name = str;
        }
    }
}
