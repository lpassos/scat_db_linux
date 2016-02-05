/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.kconfig.impl;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static scat_linux.common.TestResource.*;

import static org.junit.Assert.* ;

@RunWith(Parameterized.class)
public class DefaultKconfigFileParserTest {

    private File input ;
    private boolean err ;
    
    public DefaultKconfigFileParserTest(File input, boolean err) {
        this.input = input ;
        this.err = err ;
    }

    @Test
    public void testParse() {
        Exception error = null ;
        try {
            DefaultKconfigFileParser.getInstance().parse(input) ;
        } catch(Exception e) {
            error = e ;
        }
        
        if (err)
            assertTrue(errorMsg(input), error != null) ;           
        else
            assertTrue(errorMsg(input), error == null) ;
    }
    
    @Parameterized.Parameters
    public static Collection<Object[]> files() {
        Collection<File> ok = getAll("^.*\\.Kconfig$") ;
        Collection<File> err = getAll("^.*\\.Kconfig.err$") ;
        
        List<Object[]> input = new LinkedList<>() ;
        
        for (File fOk: ok) {
            input.add(new Object[]{fOk, false}) ;
        }
        
        for (File fErr: err) {
            input.add(new Object[]{fErr, true}) ;
        }        
        
        return input;
    }      
}
