/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.c;

import java.io.File;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import scat_linux.common.Reference;
import scat_linux.file.LinuxFile;

/**
 *
 * @author leonardo
 */
abstract public class CFile extends LinuxFile {
    
    private long sloc ;
    
    protected CFile(File file, long sloc, Set<Reference> refs) {
        super(file, refs) ;
        this.sloc = sloc ;
    }           

    @Override
    public String fileType() {
        return "c_source" ;
    }
            
    public static CFile process(CFileProcessorFactory factory, File input) {
        return factory.createProcessor().process(input) ;
    }
    
    public static CFile process(File input) {
        return CFileProcessorFactory.getDefault().createProcessor().process(input) ;
    } 
        
    private static final Pattern IFDEF = Pattern.compile(
        "\\s*#\\s*(if|ifdef|ifndef|elif)\\s+.*" 
    ) ;
    
    private static final Pattern CFILE_NAME_PATTERN = Pattern.compile(
        "^.*\\.(agh|asp|c|ec|fuc|H|h|hh|hpp|inc|ini|inl|pgc|uc|ucode)$"
    ) ;    
    
    public static boolean matches(File f) {
        return CFILE_NAME_PATTERN.matcher(f.getName()).matches() ;
    }  
    
    public static boolean isIfDef(String line) {
         Matcher m = IFDEF.matcher(line) ;
         return m.find() && m.hitEnd();
     }    

    public long getSloc() {
        return sloc;
    }         
}
