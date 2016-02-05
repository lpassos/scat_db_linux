/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.c.impl;

import java.io.File;
import scat_linux.file.IFileCleaner;
import scat_linux.utils.ShellScripts;

/**
 *
 * @author leonardo
 */
class DefaultCFileCleaner {
    
    private DefaultCFileCleaner() { }
    
    private static final IFileCleaner instance = new IFileCleaner() {

        @Override
        public File clean(File input) {
            try {
                ShellScripts.cleanCCode(input) ;
                return input ;
            } catch(Exception e) {
                throw new RuntimeException(e) ;
            }                
        }
    } ;

    public static IFileCleaner getInstance() {
        return instance;
    }        
}
