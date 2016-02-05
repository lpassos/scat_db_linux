/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.utils;

import gsd.utils.collection.CollectionsFormatter;
import gsd.utils.ipc.Environment;
import gsd.utils.ipc.ShellScript;
import gsd.utils.ipc.ScriptUtils;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import scat_linux.common.LinuxSubsystem;
import scat_linux.env.ScatDBProperties;

/**
 *
 * @author leonardo
 */
public class ShellScripts {

    
    private static List<String> runScript(String scriptName) 
            throws Exception {
        return runScript(scriptName, Environment.asStringArray(),
                ScatDBProperties.get("user.dir"), new String[0]) ;
    }        
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    private static List<String> runScript(String scriptName, String... args) 
            throws Exception {
        return runScript(scriptName, Environment.asStringArray(), 
                ScatDBProperties.get("user.dir"), args) ;
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    private static List<String> runScript(String scriptName, 
            String[] env, String dir, String... args) throws Exception {
                
         String script = ScriptUtils.find(
                    ScatDBProperties.getShellScriptsDir(), 
                    ShellScripts.class, 
                    scriptName) ;
         return ShellScript.run(
                    makeCommandCall(script, args), 
                    env, 
                    dir
                ) ;   
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    /* To take advantage of our previously created scripts, we create
     * a Java interface for them. This avoids rewriting code, and 
     * allows other parts of this program  (e.g., database population)
     * to directly call these scripts as they were ordinary functions.
     /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    private static String makeCommandCall(String cmd, String... args) {
        return cmd + " " + CollectionsFormatter.format(Arrays.asList(args), " ") ;
    }       
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public static File cleanCCode(String input) throws Exception {
        return cleanCCode(new File(input)) ;
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public static File cleanCCode(File file) throws Exception {    
        runScript("clean_cc.sh", file.getAbsolutePath()) ;
        return file ;        
    }   
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public static LinuxSubsystem mapFileToSubsystem(String fileName) throws Exception {
        
        List<String> res = 
                runScript("map_file_to_subsystem.sh", fileName) ;
                
        if (res.isEmpty() || res.size() > 1)
            throw new Exception("Error mapping " + fileName + " -> " 
                    + res) ;
        
        return LinuxSubsystem.parse(res.get(0)) ;        
    }
}
