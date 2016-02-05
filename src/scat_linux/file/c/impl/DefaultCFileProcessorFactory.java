/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.c.impl;

import scat_linux.file.c.CFileProcessorFactory;

/**
 *
 * @author leonardo
 */
public class DefaultCFileProcessorFactory {
    
    private DefaultCFileProcessorFactory() { }
    
    private static CFileProcessorFactory instance = 
            new CFileProcessorFactory(DefaultCFileCleanerFactory.getInstance(), 
                                      DefaultCFileParserFactory.getInstance()) ;   
    
    public static CFileProcessorFactory getInstance() {
        return instance ;
    }
}
