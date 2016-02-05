/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.common;

/**
 *
 * @author leonardo
 */
public enum LinuxSubsystem { 
    
    CORE("core"),
    FS("fs"),
    DRIVER("driver"),
    NET("net"),
    FIRMWARE("firmware"),
    ARCH("arch"),
    MISC("misc") ;
    
    public final String str ;
    
    LinuxSubsystem(String str) {
        this.str = str ;
    }    
       
    public static LinuxSubsystem parse(String subsystemName) throws Exception {
        for(LinuxSubsystem s : LinuxSubsystem.values())
            if (s.str.equals(subsystemName)) {
                return s ;
            }
        throw new Exception("Unknown subsystem " + subsystemName) ;
    }
    
    @Override
    public String toString() {
        return str ;
    }
}
