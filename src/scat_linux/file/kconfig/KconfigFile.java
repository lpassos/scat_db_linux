/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.kconfig;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import scat_linux.common.Reference;
import scat_linux.file.LinuxFile;

/**
 *
 * @author leonardo
 */
abstract public class KconfigFile extends LinuxFile {
    
    private List<KconfigEntry> model ;
    
    protected KconfigFile(File file, Set<Reference> refs, List<KconfigEntry> model) {
        super(file, refs);
        this.model = model ;
    }           
    
    @Override
    public String fileType() {
        return "kconfig" ;
    }

    public List<KconfigEntry> getModel() {
        return model;
    }       
            
    public static KconfigFile process(KconfigFileProcessorFactory factory, File input) {
        return factory.createProcessor().process(input) ;
    }
    
    public static KconfigFile process(File input) {
        return KconfigFileProcessorFactory.getDefault().createProcessor().process(input) ;
    } 
    
    private static final Pattern KCONFIG_NAME_PATTERN = Pattern.compile(".*Kconfig.*") ;     
    
    public static boolean matches(File f) {
        return KCONFIG_NAME_PATTERN.matcher(f.getName()).matches() ;
    }    
}
