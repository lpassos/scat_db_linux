/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.kconfig.impl;

import scat_linux.file.IFileParser;
import scat_linux.file.IFileParserFactory;
import scat_linux.file.kconfig.KconfigFile;

/**
 *
 * @author leonardo
 */
class DefaultKconfigFileParserFactory {
    
    private DefaultKconfigFileParserFactory() { }
    
    private static IFileParserFactory<KconfigFile> instance = new IFileParserFactory<KconfigFile>() {

        @Override
        public IFileParser<KconfigFile> createParser() {
            return DefaultKconfigFileParser.getInstance() ;
        }
    } ;

    public static IFileParserFactory<KconfigFile> getInstance() {
        return instance;
    }        
}
