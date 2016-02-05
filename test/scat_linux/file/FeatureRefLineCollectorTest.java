/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import scat_linux.common.Reference;
import static scat_linux.common.TestResource.get;

@RunWith(Parameterized.class)
public class FeatureRefLineCollectorTest extends FeatureRefCollectorTest {        
    
    public FeatureRefLineCollectorTest(final File input) {
        super(new IRefCollector() {

            Set<Reference> refs = FeatureRefLineCollector.get(input) ;
            
            @Override
            public Set<Reference> getRefs() {
                return refs ;
            }

            @Override
            public File getFile() {
                return input ;
            }
        })  ;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> files() {
        return Arrays.asList(
            new Object[]{get("input1")},
            new Object[]{get("input2")},
            new Object[]{get("input3")},
            new Object[]{get("input4")},
            new Object[]{get("input5")}
        ) ;
    }       
}