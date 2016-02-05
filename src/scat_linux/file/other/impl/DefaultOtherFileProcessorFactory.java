/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.other.impl;

import scat_linux.file.DummyFileCleanerFactory;
import scat_linux.file.other.OtherFileProcessorFactory;

/**
 *
 * @author leonardo
 */
public class DefaultOtherFileProcessorFactory {
    
    private DefaultOtherFileProcessorFactory() { }
    
    private static OtherFileProcessorFactory instance = 
            new OtherFileProcessorFactory(DummyFileCleanerFactory.getInstance(), 
                                      DefaultOtherFileParserFactory.getInstance()) ;   
    
    public static OtherFileProcessorFactory getInstance() {
        return instance ;
    }
}
