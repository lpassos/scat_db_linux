/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;
import scat_linux.common.Reference;
import static scat_linux.common.TestResource.errorMsg;
import static scat_linux.common.TestResource.execMsg;

@Ignore
public class FeatureRefCollectorTest {
    
    protected IRefCollector collector ;

    public FeatureRefCollectorTest(IRefCollector collector) {
        this.collector = collector ;
    } 
    
    @Test
    public void testGetRefs() {
        
        System.out.println(execMsg(this.getClass(), "testGetRefs", collector.getFile())) ;  
        
        File expectedFile = new File(collector.getFile().getAbsolutePath() + ".refs") ;
        Exception error = null ;
        
        try {
            Set<Reference> expectedRefs = loadExpectedRefs(expectedFile) ;
            Set<Reference> refsFound = collector.getRefs() ;
            
            if (! refsFound.equals(expectedRefs)) {
                Set<Reference> s1 = new HashSet<>(refsFound) ;
                s1.removeAll(expectedRefs);
                
                System.out.println("Nbr. of missing refs (found, but not expected): " + s1.size()) ;
                System.out.println("Missing refs (found, but not expected): " + s1) ;                                
                
                Set<Reference> s2 = new HashSet<>(expectedRefs) ;
                s2.removeAll(refsFound);
                
                System.out.println("\n\nNbr of missing refs (expected, but not found): " + s2.size()) ;                                
                System.out.println("Missing refs (expected, but not found): " + s2) ;                
            }
            
            assertTrue(errorMsg(collector.getFile()), refsFound.equals(expectedRefs)) ;   
            
        } catch(Exception e) {
            error = e ;
            e.printStackTrace() ;
        }
        assertTrue(errorMsg(collector.getFile()), error == null) ;
    }    
   
    public Set<Reference> loadExpectedRefs(File expectedFile) throws Exception {

        HashSet<Reference> refs = new HashSet<>() ;

        if (expectedFile.exists()) {

            int lineCount = 1 ;
            try(BufferedReader reader = new BufferedReader(new FileReader(expectedFile))) {                                

                String line ;
                while((line = reader.readLine()) != null) {

                    int currentDelimPos = line.indexOf(';');

                    int lineNbr = Integer.parseInt(line.substring(0, currentDelimPos));
                    line = line.substring(currentDelimPos + 1) ;

                    currentDelimPos = line.indexOf(';');
                    String referredFeature = line.substring(0, currentDelimPos) ;
                    line = line.substring(currentDelimPos + 1) ;

                    String context = line ;

                    refs.add(new Reference(lineNbr, referredFeature, context)) ;
                    
                    lineCount++ ;
                }
            }
            catch(Exception e) {
                throw new Exception("Error reading line " + lineCount + " from " + 
                        expectedFile.getAbsolutePath(), e) ;
            }
        }
        return refs ;               
    }         
 
}
