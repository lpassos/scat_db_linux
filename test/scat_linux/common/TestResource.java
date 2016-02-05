/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.common;

import gsd.utils.io.FileUtils;
import gsd.utils.string.Separator;
import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import org.junit.Ignore;
import test_utils.TestUtils;

/**
 *
 * @author leonardo
 */

@Ignore
public class TestResource{ 
    
    public static String execMsg(Class t, String testName, File input) {
        return "------------------------------------------------\n" +          
               "Running " + t.getName() + "." + testName + "\n" + 
               "    input: " + input + "\n" ;
    }
    
    public static String errorMsg(File input) {
        return input + " failed" ;
    }
    
    public static String errorMsg(File input, Object found, Object expected) {
        return input + " failed: expecting" + expected + ", but found " + found;
    }    
    
    public static String testFilesDir() {
        return "test" + Separator.DIR_PATH + "scat_linux" + Separator.DIR_PATH +
                "test_files" ;
    } 
    
    public static File get(String file) {
        return new File(testFilesDir() + Separator.DIR_PATH + file) ;
    }
    
    public static String getTestName(Class c, String testName) {
        return c.getName() + "." + testName ;
    }
    
    public static Collection<File> getAll(String fileNameRegex) {
       return FileUtils.find(testFilesDir(), fileNameRegex) ;
    }
    
    public static <T> Collection<Object[]> toJUnitParameterArray(Collection<T> c) {
        Collection<Object[]> array = new LinkedList<>() ;
        for(T element : c)
            array.add(new Object[] {element}) ;
        return array ;        
    }
}
