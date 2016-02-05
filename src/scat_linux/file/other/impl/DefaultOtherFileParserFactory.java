/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.other.impl;

import scat_linux.file.IFileParser;
import scat_linux.file.IFileParserFactory;
import scat_linux.file.other.OtherFile;

/**
 *
 * @author leonardo
 */
class DefaultOtherFileParserFactory {
   
    private DefaultOtherFileParserFactory() { }
    
    private static IFileParserFactory<OtherFile> instance = new IFileParserFactory<OtherFile>() {

        @Override
        public IFileParser<OtherFile> createParser() {
            return DefaultOtherParser.getInstance() ;
        }                       
    } ;    

    public static IFileParserFactory<OtherFile> getInstance() {
        return instance;
    }           
}
