/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static scat_linux.common.TestResource.*;


@RunWith(Parameterized.class)
public class LineFileCleanerFactoryTest extends FileCleanerFactoryTest {

    public LineFileCleanerFactoryTest(File input, String commentMarker) {
        super(input, new LineFileCleanerFactory(commentMarker));
    }
            
    @Parameterized.Parameters
    public static Collection<Object[]> files() {
        return Arrays.asList(
            new Object[]{get("input1"), "///"},
            new Object[]{get("input2"), "#"},
            new Object[]{get("input3"), "#"},
            new Object[]{get("input4"), "#"},
            new Object[]{get("input5"), "#"}
        ) ;
    }         
}