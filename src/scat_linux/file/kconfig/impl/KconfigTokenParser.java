/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.kconfig.impl;

import scat_linux.file.kconfig.KconfigToken;
import scat_linux.file.kconfig.KconfigTokenType;

/**
 *
 * @author leonardo
 */
public class KconfigTokenParser {
            
    public static KconfigToken parse(String inputLine) throws Exception {
        
        int previous ;
        int next ;
        
        previous = 0 ;        
        next = inputLine.indexOf(';', previous) ;                                
        
        int t = Integer.parseInt(inputLine.substring(previous, next)) ;        
        KconfigTokenType tokenType = KconfigTokenType.fromId(t) ;
        
        previous = next + 1 ;        
        next = inputLine.indexOf(';', previous) ;                                
                
        long lineNbr = Long.parseLong(inputLine.substring(previous, next)) ;
        
        previous = next + 1 ;   
        
        String value = inputLine.substring(previous) ;
        
        return new KconfigToken(value, tokenType, lineNbr) ;
    }       
}
