/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file;

/**
 *
 * @author leonardo
 */
public abstract class LinuxFileProcessorFactory<T extends LinuxFile> {
    
    private IFileCleanerFactory cleanerFactory ;
    private IFileParserFactory<T>  parserFactory ;

    protected LinuxFileProcessorFactory() { }
    
    public void setCleanerFactory(IFileCleanerFactory cleaner) {
        this.cleanerFactory = cleaner;
    }

    public void setParserFactory(IFileParserFactory<T> parser) {
        this.parserFactory = parser;
    }
    
    public final LinuxFileProcessor<T> createProcessor() {
        return new LinuxFileProcessor<>(cleanerFactory, parserFactory) ;
    }
}
