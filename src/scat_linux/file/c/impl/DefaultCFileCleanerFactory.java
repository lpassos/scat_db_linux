/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.c.impl;

import scat_linux.file.IFileCleaner;
import scat_linux.file.IFileCleanerFactory;

/**
 *
 * @author leonardo
 */
class DefaultCFileCleanerFactory {
    
    private DefaultCFileCleanerFactory() { }
                 
    private static final IFileCleanerFactory instance = new IFileCleanerFactory() {               
        @Override
        public IFileCleaner createCleaner() {
            return DefaultCFileCleaner.getInstance();
        }        
    } ;

    public static IFileCleanerFactory getInstance() {
        return instance;
    }        
}
