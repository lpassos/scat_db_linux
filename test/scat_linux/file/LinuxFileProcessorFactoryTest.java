/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file;

import gsd.utils.io.FileUtils;
import java.io.File;
import java.util.Set;
import org.junit.Ignore;
import scat_linux.common.Reference;


@Ignore
public class LinuxFileProcessorFactoryTest<T extends LinuxFile> extends FeatureRefCollectorTest {    

    private static class ParserFactoryToRefCollector<T extends LinuxFile> 
        implements IRefCollector {
        
        T processedFile ;
        File originalFile ;
        
        public ParserFactoryToRefCollector(
                File originalFile, 
                LinuxFileProcessorFactory<T> processorFactory) throws Exception {
            
            processedFile = processorFactory.createProcessor().process(
                    FileUtils.createTempCopy(originalFile)) ;  
            
            this.originalFile = originalFile ;
        }

        @Override
        public Set<Reference> getRefs() {
            return processedFile.getRefs() ;         
        }

        @Override
        public File getFile() {
            return originalFile ;
        }
    }

    
    public LinuxFileProcessorFactoryTest(File f, LinuxFileProcessorFactory<T> processorFactory) 
        throws Exception {
            super(new ParserFactoryToRefCollector<>(f, processorFactory));
    }     
    
    protected LinuxFile getProcessedFile() {
        return ((ParserFactoryToRefCollector<? extends LinuxFile>) super.collector).processedFile ;
    }
}
