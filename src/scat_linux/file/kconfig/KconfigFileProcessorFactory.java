/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.kconfig;

import scat_linux.file.IFileCleanerFactory;
import scat_linux.file.IFileParserFactory;
import scat_linux.file.LinuxFileProcessorFactory;
import scat_linux.file.kconfig.impl.DefaultKconfigFileProcessorFactory;

/**
 *
 * @author leonardo
 */
final public class KconfigFileProcessorFactory extends LinuxFileProcessorFactory<KconfigFile> {

    public KconfigFileProcessorFactory
            (IFileCleanerFactory cleanerFactory, IFileParserFactory<KconfigFile> parserFactory) {
        setCleanerFactory(cleanerFactory);
        setParserFactory(parserFactory);
    }

    public static KconfigFileProcessorFactory getDefault() {
        return DefaultKconfigFileProcessorFactory.getInstance();
    }        
}
