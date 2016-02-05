/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.other;

import scat_linux.file.IFileCleanerFactory;
import scat_linux.file.IFileParserFactory;
import scat_linux.file.LinuxFileProcessorFactory;
import scat_linux.file.other.impl.DefaultOtherFileProcessorFactory;

/**
 *
 * @author leonardo
 */
final public class OtherFileProcessorFactory extends LinuxFileProcessorFactory<OtherFile> {

    public OtherFileProcessorFactory
            (IFileCleanerFactory cleanerFactory, IFileParserFactory<OtherFile> parserFactory) {
        setCleanerFactory(cleanerFactory);
        setParserFactory(parserFactory);
    }

    public static OtherFileProcessorFactory getDefault() {
        return DefaultOtherFileProcessorFactory.getInstance();
    }        
}
