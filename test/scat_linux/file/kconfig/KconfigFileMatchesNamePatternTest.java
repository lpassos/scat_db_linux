/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.kconfig;

import scat_linux.file.kconfig.KconfigFile;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import scat_linux.file.MatchesNamePatternTest;

import static scat_linux.common.TestResource.*  ;


@RunWith(Parameterized.class)
public class KconfigFileMatchesNamePatternTest extends MatchesNamePatternTest {
    
    public KconfigFileMatchesNamePatternTest(File input, boolean negate) {
        super(KconfigFile.class, input, negate) ;
    }  
    
    @Parameterized.Parameters
    public static Collection<Object[]> files() {
        return Arrays.asList(
            new Object[]{get("kconfig_files.list"), false},
            new Object[]{get("makefiles.list"), true},
            new Object[]{get("c_files.list"), true},
            new Object[]{get("others.list"), true}
        ) ;
    }     
}