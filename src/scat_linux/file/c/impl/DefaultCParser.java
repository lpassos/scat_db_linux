/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.c.impl;

import gsd.utils.io.FileUtils;
import java.io.File;
import scat_linux.file.c.CFile;
import scat_linux.file.IFileParser;
import scat_linux.file.FeatureRefLineCollector;

/**
 *
 * @author leonardo
 */
class DefaultCParser {
    
    private DefaultCParser() { }
    
    private static IFileParser<CFile> instance = new IFileParser<CFile>() {

        @Override
        public CFile parse(File input) {
            CFile res = null ;

            try {
                res = new CFile(input, 
                        FileUtils.countLines(input), 
                        FeatureRefLineCollector.get(input)) { } ;

            } catch(Exception e) {
                throw new RuntimeException(e) ;
            }
            return res ;
        }                
    } ;

    public static IFileParser<CFile> getInstance() {
        return instance;
    }           
}
