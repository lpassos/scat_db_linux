/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.c.impl;

import scat_linux.file.c.CFile;
import scat_linux.file.IFileParser;
import scat_linux.file.IFileParserFactory;

/**
 *
 * @author leonardo
 */
class DefaultCFileParserFactory {
   
    private DefaultCFileParserFactory() { }
    
    private static IFileParserFactory<CFile> instance = new IFileParserFactory<CFile>() {

        @Override
        public IFileParser<CFile> createParser() {
            return DefaultCParser.getInstance() ;
        }                       
    } ;    

    public static IFileParserFactory<CFile> getInstance() {
        return instance;
    }           
}
