/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file;

import scat_linux.file.other.OtherFile;
import scat_linux.file.c.CFile;
import scat_linux.file.build.BuildFile;
import scat_linux.file.kconfig.KconfigFile;
import gsd.utils.io.IOUtils;
import java.io.File;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;
import static scat_linux.common.TestResource.errorMsg;
import static scat_linux.common.TestResource.execMsg;

@Ignore
public class MatchesNamePatternTest {
    
    protected File input ;
    protected boolean negate ;
    protected Class fileType ;

    public MatchesNamePatternTest(Class fileType, File input, boolean negate) {     
        this.input = input ;
        this.negate = negate;
        this.fileType = fileType ;
    }    
    
    @Test
    public void testMatchesNamePattern() {          
           
        System.out.println(execMsg(this.getClass(), "testMatchesNamePattern", input)) ;     
        
        Exception error = null ;       
        try {
            
            for(String fileName : IOUtils.readAll(input).split("\n")) {
                
                File f = new File(fileName) ;
                
                if (fileType.equals(KconfigFile.class)) {
                    
                    if (negate)
                        assertFalse(errorMsg(input), 
                                KconfigFile.matches(f)) ;
                    else
                        assertTrue(errorMsg(input), 
                                KconfigFile.matches(f)) ;
                    
                    continue ;
                }
                
                if (fileType.equals(BuildFile.class)) {
                    
                    if (negate)
                        assertFalse(errorMsg(input), 
                                BuildFile.matches(f)) ;
                    else
                        assertTrue(errorMsg(input), 
                                BuildFile.matches(f)) ;
                    
                    continue ;
                }
                
                if (fileType.equals(CFile.class)) {
                    
                    if (negate)
                        assertFalse(errorMsg(input), 
                                CFile.matches(f)) ;
                    else
                        assertTrue(errorMsg(input), 
                                CFile.matches(f)) ;                    
                    
                    continue ;
                }
                
                if (fileType.equals(OtherFile.class)) {
                    
                    if (negate)
                        assertFalse(errorMsg(input), 
                                OtherFile.matchesNamePattern(f)) ;
                    else
                        assertTrue(errorMsg(input), 
                                OtherFile.matchesNamePattern(f)) ;                    
                    
                    continue ;
                }    
                
                throw new Exception("Unknown file type" + fileType.getName()) ;
                
            }
            
        } catch(Exception e) {
          e.printStackTrace(); 
          error = e ;
        }          
        assertTrue(errorMsg(input), error == null) ;
    }     
}
