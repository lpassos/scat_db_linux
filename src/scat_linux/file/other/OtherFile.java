/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.other;

import java.io.File;
import java.util.Set;
import scat_linux.common.Reference;
import scat_linux.file.LinuxFile;

/**
 *
 * @author leonardo
 */
abstract public class OtherFile extends LinuxFile {
    
    protected OtherFile(File file, Set<Reference> refs) {
        super(file, refs) ;
    }           

    @Override
    public String fileType() {
        return "other" ;
    }
            
    public static OtherFile process(OtherFileProcessorFactory factory, File input) {
        return factory.createProcessor().process(input) ;
    }
    
    public static OtherFile process(File input) {
        return OtherFileProcessorFactory.getDefault().createProcessor().process(input) ;
    }    
    
    public static boolean matchesNamePattern(File f) {
        return true ;
    }
}
