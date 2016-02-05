/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file;

import scat_linux.file.kconfig.KconfigToken;

/**
 *
 * @author leonardo
 */
public class ParseException {
    public static void raiseUnexpectedInput(KconfigToken token) throws Exception {
        raiseExceptionOnInput("unexpected input", token) ;
    } 
    
    public static void raiseUnexpectedInput(long lineNbr) throws Exception {
        raiseExceptionOnInput("unexpected input", lineNbr) ;
    }     
    
    public static void raiseExceptionOnInput(String msg, KconfigToken token) throws Exception {
        throw new Exception(msg + " (" + token.getLineNbr() + "): " + token.getValue()) ;
    }  
    
    public static void raiseExceptionOnInput(String msg, long lineNbr) throws Exception {
        throw new Exception(msg + " (" + lineNbr + ")") ;
    }     
}
