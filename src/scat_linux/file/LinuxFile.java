/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file;

import java.io.File;
import java.util.Set;
import scat_linux.common.Reference;

/**
 *
 * @author leonardo
 */
public abstract class LinuxFile {        
    
    private Set<Reference> refs ;
    private File file ;
    
    protected LinuxFile(File file, Set<Reference> refs) {
        this.file = file ;
        this.refs = refs ;
    }
      
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public final Set<Reference> getRefs() {
        return refs ;
    }
            
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public abstract String fileType();       

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public File getFile() {
        return file;
    }     
}

