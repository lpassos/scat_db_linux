/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.kconfig.impl;

import java.util.Collection;

/**
 *
 * @author leonardo
 */
public class Value {
    
    public static <T> Collection<T> checkAndAdd(String input, T value, Collection<T> res, boolean force) {

        if (value != null)
            res.add(value) ;
        
        else if (force)        
            throw new RuntimeException("Cannot consume value from input " + input) ;

        return res ;
    }  
    
    public static <T> T check(String input, T value, boolean force) {
        if (value == null && force)
            throw new RuntimeException("Cannot consume value from input " + input) ;
        
        return value ;
    }      
}
