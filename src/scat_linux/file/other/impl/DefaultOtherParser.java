/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.other.impl;

import java.io.File;
import scat_linux.file.IFileParser;
import scat_linux.file.other.OtherFile;

/**
 *
 * @author leonardo
 */
class DefaultOtherParser {
    
    private DefaultOtherParser() { }
    
    private static IFileParser<OtherFile> instance = new IFileParser<OtherFile>() {

        @Override
        public OtherFile parse(File input) {
            OtherFile res = null ;

            try {
                res = new OtherFile(input, 
                        null) { } ;

            } catch(Exception e) {
                throw new RuntimeException(e) ;
            }
            return res ;
        }                
    } ;

    public static IFileParser<OtherFile> getInstance() {
        return instance;
    }           
}
