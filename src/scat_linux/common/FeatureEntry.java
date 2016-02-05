/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.common;

import java.io.File;

/**
 *
 * @author leonardo
 */
public class FeatureEntry {
    
    private File filePath ;
    private String name ;
    
    public FeatureEntry(String name, String filePath) {
        this(name, new File(filePath)) ;
    }

    public FeatureEntry(String name, File filePath) {
        this.filePath = filePath;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public File getFilePath() {
        return filePath;
    }  
    
    public static FeatureEntry parse(String s) {
        String[] fields = s.split("(\\s|:)") ;
        return new FeatureEntry(fields[fields.length - 1], fields[0]) ;
    }

    @Override
    public String toString() {
        return filePath.getPath() + ":" + getName() ;
    }        
}
