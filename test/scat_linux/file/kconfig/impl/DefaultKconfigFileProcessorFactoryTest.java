/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.kconfig.impl;

import java.io.File;
import java.util.Collection;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static scat_linux.common.TestResource.*;
import scat_linux.file.kconfig.KconfigFile;
import scat_linux.file.LinuxFileProcessorFactoryTest;

@RunWith(Parameterized.class)
public class DefaultKconfigFileProcessorFactoryTest extends LinuxFileProcessorFactoryTest<KconfigFile> { 
    
    public DefaultKconfigFileProcessorFactoryTest(File input) throws Exception {
        super(input, DefaultKconfigFileProcessorFactory.getInstance());
    }            
    
    @Parameterized.Parameters
    public static Collection<Object[]> files() {
        return toJUnitParameterArray(getAll(".*\\.Kconfig$")) ;
    }        
}