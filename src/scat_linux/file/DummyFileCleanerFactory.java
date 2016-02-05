/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file;

import java.io.File;

/**
 *
 * @author leonardo
 */
public class DummyFileCleanerFactory implements IFileCleanerFactory {

    private static IFileCleaner dummyFileCleaner = new IFileCleaner() {

        @Override
        public File clean(File input) {
            return input ;
        }
    } ;
    
    private static DummyFileCleanerFactory instance = new DummyFileCleanerFactory() ;
    
    @Override
    public IFileCleaner createCleaner() {
        return dummyFileCleaner ;
    }    

    public static DummyFileCleanerFactory getInstance() {
        return instance;
    }
}
