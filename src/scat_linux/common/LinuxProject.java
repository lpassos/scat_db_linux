/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.common;

import kconfig_data.common.ProjectInfo;

/**
 *
 * @author leonardo
 */
public class LinuxProject {
    
    private static ProjectInfo instance ;
    
    static {
        try {
            instance = new ProjectInfo("linux") ;
        } catch(Exception e) {
            throw new RuntimeException(e) ;
        }
    }
    
    public static ProjectInfo getInstance() {
        return instance ;
    }
}
