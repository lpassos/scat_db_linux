/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.kconfig.impl;

import scat_linux.file.DummyFileCleanerFactory;
import scat_linux.file.kconfig.KconfigFileProcessorFactory;

/**
 *
 * @author leonardo
 */
public class DefaultKconfigFileProcessorFactory {
    private static KconfigFileProcessorFactory instance = 
            new KconfigFileProcessorFactory(
                DummyFileCleanerFactory.getInstance(), 
                DefaultKconfigFileParserFactory.getInstance()
    ) ;   
    
    public static KconfigFileProcessorFactory getInstance() {
        return instance ;
    }    
}
