/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.c.impl;

import java.io.File;
import java.util.Collection;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import scat_linux.file.FileCleanerFactoryTest;

import static scat_linux.common.TestResource.*;

@RunWith(Parameterized.class)
public class DefaultCFileCleanerFactoryTest extends FileCleanerFactoryTest { 
    
    public DefaultCFileCleanerFactoryTest(File input) {
        super(input, DefaultCFileCleanerFactory.getInstance());
    }        
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */ 
    
    @Parameterized.Parameters
    public static Collection<Object[]> files() {
        return toJUnitParameterArray(getAll(".*\\.c")) ;
    }        
}