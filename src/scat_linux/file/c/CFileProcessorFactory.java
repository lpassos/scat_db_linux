/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.c;

import scat_linux.file.IFileCleanerFactory;
import scat_linux.file.IFileParserFactory;
import scat_linux.file.LinuxFileProcessorFactory;
import scat_linux.file.c.impl.DefaultCFileProcessorFactory;

/**
 *
 * @author leonardo
 */
final public class CFileProcessorFactory extends LinuxFileProcessorFactory<CFile> {

    public CFileProcessorFactory
            (IFileCleanerFactory cleanerFactory, IFileParserFactory<CFile> parserFactory) {
        setCleanerFactory(cleanerFactory);
        setParserFactory(parserFactory);
    }

    public static CFileProcessorFactory getDefault() {
        return DefaultCFileProcessorFactory.getInstance();
    }        
}
