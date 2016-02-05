/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.kconfig.impl;

import git.Git;
import gsd.utils.io.FileUtils;
import java.io.File;
import java.util.List;
import org.junit.Test;

import static scat_linux.common.TestResource.*;

import static org.junit.Assert.* ;
import scat_linux.common.LinuxProject;
import scat_linux.file.kconfig.KconfigFile;

public class DefaultKconfigFileParserFileRepoTest {
   
    @Test
    public void testParseAll() throws Exception {       
        
        Git git = LinuxProject.getInstance().getGitRepo() ;
        
        List<String> releases = 
                LinuxProject.getInstance().getOrderedStableReleases() ;       
        
        String currentBranch = git.getCurrentBranch() ;
                       
        for(String release : releases) {
            
            System.out.println("\nChecking out release " + release) ;
            
            git.checkout(release);

            Exception error = null ;
            
            for(File kconfig : 
                    FileUtils.find(git.getRepository(), ".*Kconfig.*")) {
                
                try {
                    System.out.println(execMsg(this.getClass(), "testParseAll", kconfig));
                    System.out.println("  Testing " + kconfig.getCanonicalPath() + "\n") ;
                    
                    KconfigFile parsedFile = 
                            DefaultKconfigFileParser.getInstance().parse(kconfig) ;
                    
                    assertTrue(errorMsg(kconfig), parsedFile.getRefs() != null) ;
                    
                    System.out.println("  Nbr. of refs: " + parsedFile.getRefs().size()) ;
                    
                } catch(Exception e) {
                    error = e ;
                    e.printStackTrace(); 
                }
                
                assertTrue(errorMsg(kconfig), error == null) ;
            }
        }
        
        /* Restores current branch */
        if (currentBranch != null) {
            git.checkout(currentBranch);
        }
            
    }   
}
