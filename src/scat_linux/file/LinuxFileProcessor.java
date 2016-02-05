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
final public class LinuxFileProcessor<T> {

    private IFileCleanerFactory cleanerFactory ;
    private IFileParserFactory<T> parserFactory ;
    
    public LinuxFileProcessor(IFileCleanerFactory cleaner, IFileParserFactory<T> parser) {
        this.cleanerFactory = cleaner ;
        this.parserFactory = parser ;
    }
    
    public final T process(File input) {
        return parserFactory.createParser().parse(
                cleanerFactory.createCleaner().clean(input)) ;
    }    
}
