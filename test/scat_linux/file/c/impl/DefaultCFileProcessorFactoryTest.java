/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.c.impl;

import gsd.utils.io.IOUtils;
import java.io.File;
import java.util.Collection;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static scat_linux.common.TestResource.*;
import scat_linux.file.c.CFile;
import scat_linux.file.LinuxFileProcessorFactoryTest;

@RunWith(Parameterized.class)
public class DefaultCFileProcessorFactoryTest extends LinuxFileProcessorFactoryTest<CFile> { 
    
    public DefaultCFileProcessorFactoryTest(File input) throws Exception {
        super(input, DefaultCFileProcessorFactory.getInstance());
    }            
    
    @Parameterized.Parameters
    public static Collection<Object[]> files() {
        return toJUnitParameterArray(getAll(".*\\.c")) ;
    } 
    
    @Test
    public void testSLoc() {

        Exception error = null ;
        try {
            long expectedSLoc = 
                  ((CFile) super.getProcessedFile()).getSloc();

            File testFile = super.collector.getFile() ;
            
            Long sLocFound = Long.parseLong(IOUtils.readAll(
                    testFile.getCanonicalPath() + ".sloc").trim()) ;
            
            assertTrue(errorMsg(testFile), sLocFound == expectedSLoc) ;
                    
            
        } catch(Exception e) {
            error = e ;            
            e.printStackTrace();            
        }
        assertTrue(errorMsg(collector.getFile()), error == null) ;
    }      
}