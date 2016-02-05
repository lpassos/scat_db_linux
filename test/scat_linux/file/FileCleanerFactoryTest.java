/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file;

import gsd.utils.io.FileUtils;
import gsd.utils.io.IOUtils;
import java.io.File;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;
import static scat_linux.common.TestResource.errorMsg;
import static scat_linux.common.TestResource.execMsg;

@Ignore
public class FileCleanerFactoryTest{ 
    
    private File input ;  
    private IFileCleanerFactory cleanerFactory ;

    public FileCleanerFactoryTest(File input, IFileCleanerFactory cleanerFactory) {
        this.input = input;
        this.cleanerFactory = cleanerFactory ;
    }        
    
    @Test
    public void testClean() {
                
        System.out.println(execMsg(this.getClass(), "testClean", input)) ;
        
        Exception error = null ;
        try {        
            File backInputFile = FileUtils.createTempCopy(input) ;
            IFileCleaner cleaner = cleanerFactory.createCleaner() ;

            cleaner.clean(backInputFile);            
            File expectedFile = new File(input.getAbsolutePath() + ".cleaned") ;      
            
            String resultContent = "" ;
            String[] expectedResultLines = { "" } ;
            
            if (expectedFile.exists()) {
                resultContent = IOUtils.readAll(backInputFile) ; 
                expectedResultLines = IOUtils.readAll(expectedFile).split("\n") ;                
            }            
            
            if (resultContent.trim().length() == 0)
                assertTrue(errorMsg(input), 
                        expectedResultLines.length == 1 && expectedResultLines[0].length() == 0) ;                
            
            else {
                String[] resultLines = resultContent.split("\n") ;
                assertTrue(errorMsg(input), resultLines.length > 0) ;
            
                assertTrue(errorMsg(input), 
                        resultLines.length == expectedResultLines.length) ;
                
                for(int i = 0; i < resultLines.length; i++) {
                    assertTrue(errorMsg(input, resultLines[i], expectedResultLines[i]),
                               resultLines[i].equals(expectedResultLines[i]));
                }                 
            }
            
        } catch(Exception e) {
            e.printStackTrace();
            error = e ;
        }              
        assertTrue(errorMsg(input), error == null) ;
    }      
}
